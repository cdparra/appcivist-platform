package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import models.audit.AuditContribution;
import models.location.Location;
import play.data.validation.Constraints.Required;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.annotation.Index;
import com.avaje.ebean.annotation.Where;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import enums.AuditEventTypes;
import enums.ContributionTypes;
import enums.ResourceSpaceTypes;

@Entity
@JsonInclude(Include.NON_EMPTY)
@Where(clause = "removed=false")
public class Contribution extends AppCivistBaseModel {

	@Id
	@GeneratedValue
	private Long contributionId;
	@Index
	@JsonIgnore
	private UUID uuid = UUID.randomUUID();
	@Transient
	private String uuidAsString;
	@Required
	private String title;
	@Required
	@Column(name = "text", columnDefinition = "text")
	private String text;
	@Enumerated(EnumType.STRING)
	@Required
	private ContributionTypes type;
	@JsonIgnore
	@Index
	@Column(name = "text_index", columnDefinition = "text")
	private String textIndex;
	@OneToOne(cascade = CascadeType.ALL)
	@Index
	private Location location;
	private String budget;
	@ManyToMany(cascade = CascadeType.ALL)
	@Where(clause = "${ta}.active=true")
	@JsonIgnoreProperties({ "providers", "roles", "permissions", "sessionKey",
			"identifier" })
	private List<User> authors = new ArrayList<User>();
	@Transient
	private User firstAuthor;
	@Transient
	private String firstAuthorName;
	@Transient
	private Long assemblyId;

	// @ManyToMany(cascade = CascadeType.ALL)
	// @Where(clause="${ta}.removed=false")
	// @JsonIgnoreProperties({ "supportedMembership", "managementType",
	// "resources", "forum", "containingSpaces", "themes", "configs",
	// "forumPosts", "brainstormingContributions", "proposals" })
	@JsonManagedReference
	@Transient
	private List<WorkingGroup> workingGroupAuthors = new ArrayList<WorkingGroup>();
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "contributions", cascade = CascadeType.ALL)
	private List<ResourceSpace> containingSpaces;
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private ResourceSpace resourceSpace = new ResourceSpace(
			ResourceSpaceTypes.CONTRIBUTION);
	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference
	private ContributionStatistics stats = new ContributionStatistics();

	/*
	 * Transient properties that take their values from the associated resource
	 * space
	 */
	@Transient
	private List<Theme> themes;
	@Transient
	private List<Resource> attachments;
	@Transient
	private List<Hashtag> hashtags = new ArrayList<Hashtag>();
	@Transient
	private List<Contribution> comments = new ArrayList<Contribution>();
	@Transient
	private List<ComponentMilestone> associatedMilestones = new ArrayList<ComponentMilestone>();

	/*
	 * The following fields are specific to each type of contribution
	 */

	/*
	 * Fields specific to the type ACTION_ITEM
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date actionDueDate;
	private Boolean actionDone = false;
	private String action;

	// Fields Fields specific to the type ASSESSMENT
	private String assessmentSummary;

	// Fields specific to the type PROPOSAL and ASSESSMENT
	@OneToOne(cascade = CascadeType.ALL)
	private Resource extendedTextPad;

	// Fields specific to the type PROPOSAL
	@Transient
	private List<Contribution> assessments;

	/*
	 * @Transient existing entities in resource space
	 */
	@Transient
	private List<Hashtag> existingHashtags;
	@Transient
	private List<WorkingGroup> existingResponsibleWorkingGroups;
	@Transient
	private List<Contribution> existingContributions;
	@Transient
	private List<Resource> existingResources;
	@Transient
	private List<Theme> existingThemes;

	/**
	 * The find property is an static property that facilitates database query
	 * creation
	 */
	public static Finder<Long, Contribution> find = new Finder<>(
			Contribution.class);
	public static Finder<Long, ResourceSpace> containingSpacesFinder = new Finder<>(
			ResourceSpace.class);
	public static Finder<Long, WorkingGroup> workingGroupFinder = new Finder<>(
			WorkingGroup.class);

	public Contribution(User creator, String title, String text,
			ContributionTypes type) {
		super();
		this.authors.add(creator);
		this.title = title;
		this.text = text;
		this.type = type;
	}

	public Contribution() {
		super();
	}

	/*
	 * Getters and Setters
	 */

	public Long getContributionId() {
		return contributionId;
	}

	public void setContributionId(Long contributionId) {
		this.contributionId = contributionId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getUuidAsString() {
		return uuid.toString();
	}

	public void setUuidAsString(String uuidAsString) {
		this.uuid = UUID.fromString(uuidAsString);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ContributionTypes getType() {
		return type;
	}

	public void setType(ContributionTypes type) {
		this.type = type;
	}

	public List<User> getAuthors() {
		return authors;
	}

	@Transient
	public User getFirstAuthor() {
		return authors != null && authors.size() > 0 ? authors.get(0) : null;
	}

	@Transient
	public void setFirstAuthor(User u) {
		this.firstAuthor = u;
	}

	@Transient
	public String getFirstAuthorName() {
		User fa = getFirstAuthor();
		return fa != null ? fa.getName() : null;
	}

	@Transient
	public void setFirstAuthorName(String name) {
		this.firstAuthorName = name;
	}

	@Transient
	public Long getAssemblyId() {
		return assemblyId;
	}

	@Transient
	public void setAssemblyId(Long aid) {
		this.assemblyId = aid;
	}

	public void setAuthors(List<User> authors) {
		this.authors = authors;
	}

	public void addAuthor(User author) {
		this.authors.add(author);
	}

	/**
	 * When we create a contribution, the "workingGroupAuthors" list contains
	 * "existing working groups" that will take on the development of the
	 * contribution When we read an existing contribution, the
	 * "workingGroupAuthors" is queried from WorkingGroups finder that can match
	 * the contribution id with the existing resources for that WorkingGroup
	 */
	public List<WorkingGroup> getWorkingGroupAuthors() {
		if (this.workingGroupAuthors == null
				|| this.workingGroupAuthors.size() == 0)
			this.workingGroupAuthors = listWorkingGroupAuthors(this.contributionId);
		return this.workingGroupAuthors;
	}

	public void setWorkingGroupAuthors(List<WorkingGroup> workingGroupAuthors) {
		this.workingGroupAuthors = workingGroupAuthors;
	}

	public void addWorkingGroupAuthor(WorkingGroup workingGroupAuthor) {
		this.workingGroupAuthors.add(workingGroupAuthor);
		workingGroupAuthor.addContribution(this);
		workingGroupAuthor.update();
		workingGroupAuthor.refresh();
	}

	public List<Theme> getThemes() {
		this.themes = resourceSpace.getThemes();
		return this.themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
		this.resourceSpace.setThemes(themes);
	}

	public void addTheme(Theme t) {
		this.resourceSpace.addTheme(t);
	}

	public List<Resource> getAttachments() {
		this.attachments = resourceSpace.getResources();
		return this.attachments;
	}

	public void setAttachments(List<Resource> attachments) {
		this.attachments = attachments;
		this.resourceSpace.setResources(attachments);
	}

	public void addAttachment(Resource attach) {
		this.resourceSpace.addResource(attach);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public List<Hashtag> getHashtags() {
		this.hashtags = resourceSpace.getHashtags();
		return this.hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
		this.resourceSpace.setHashtags(hashtags);
	}

	public void addHashtag(Hashtag h) {
		this.resourceSpace.addHashtag(h);
	}

	public Long getResourceSpaceId() {
		return this.resourceSpace != null ? this.resourceSpace
				.getResourceSpaceId() : null;
	}

	public void setResourceSpaceId(Long id) {
		if (this.resourceSpace != null
				&& this.resourceSpace.getResourceSpaceId() == null)
			this.resourceSpace.setResourceSpaceId(id);
	}

	public List<Contribution> getComments() {
		this.comments = resourceSpace
				.getContributionsFilteredByType(ContributionTypes.COMMENT);
		return this.comments;
	}

	public void addComment(Contribution c) {
		if (c.getType() == ContributionTypes.COMMENT)
			this.resourceSpace.addContribution(c);
	}

	public String getReadOnlyPadUrl() {
		return extendedTextPad != null ? extendedTextPad.getUrlAsString()
				: null;
	}

	// TODO see if setting contributions on resource space is better through
	// updating the space directly
	@JsonIgnore
	public void setComments(List<Contribution> comments) {
		this.comments = comments;
		this.resourceSpace.setContributionsFilteredByType(comments,
				ContributionTypes.COMMENT);
	}

	public void setAssessments(List<Contribution> assessments) {
		this.assessments = assessments;
		this.resourceSpace.setContributionsFilteredByType(assessments,
				ContributionTypes.ASSESSMENT);
	}

	public List<ComponentMilestone> getAssociatedMilestones() {
		this.associatedMilestones = this.resourceSpace.getMilestones();
		return this.associatedMilestones;
	}

	public void setAssociatedMilestones(
			List<ComponentMilestone> associatedMilestones) {
		this.associatedMilestones = associatedMilestones;
		this.resourceSpace.setMilestones(associatedMilestones);
	}

	@JsonIgnore
	public ResourceSpace getResourceSpace() {
		return resourceSpace;
	}

	public void setResourceSpace(ResourceSpace resourceSpace) {
		this.resourceSpace = resourceSpace;
	}

	public ContributionStatistics getStats() {
		return stats;
	}

	public void setStats(ContributionStatistics stats) {
		this.stats = stats;
	}

	public Date getActionDueDate() {
		return actionDueDate;
	}

	public void setActionDueDate(Date actionDueDate) {
		this.actionDueDate = actionDueDate;
	}

	public Boolean getActionDone() {
		return actionDone;
	}

	public void setActionDone(Boolean actionDone) {
		this.actionDone = actionDone;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAssessmentSummary() {
		return assessmentSummary;
	}

	public void setAssessmentSummary(String assessmentSummary) {
		this.assessmentSummary = assessmentSummary;
	}

	public Resource getExtendedTextPad() {
		return extendedTextPad;
	}

	public void setExtendedTextPad(Resource extendedTextPad) {
		this.extendedTextPad = extendedTextPad;
	}

	public List<Contribution> getAssessments() {
		this.assessments = this.resourceSpace
				.getContributionsFilteredByType(ContributionTypes.ASSESSMENT);
		return this.assessments;
	}

	public void addAssessment(Contribution assessment) {
		// if(assessment.getType()==ContributionTypes.ASSESSMENT)
		// this.resourceSpace.addContribution(assessment);
	}

	@JsonIgnore
	public List<Hashtag> getExistingHashtags() {
		return existingHashtags;
	}

	public void setExistingHashtags(List<Hashtag> existingHashtags) {
		this.existingHashtags = existingHashtags;
	}

	@JsonIgnore
	public List<WorkingGroup> getExistingResponsibleWorkingGroups() {
		return existingResponsibleWorkingGroups;
	}

	public void setExistingResponsibleWorkingGroups(
			List<WorkingGroup> existingResponsibleWorkingGroups) {
		this.existingResponsibleWorkingGroups = existingResponsibleWorkingGroups;
	}

	@JsonIgnore
	public List<Contribution> getExistingContributions() {
		return existingContributions;
	}

	public void setExistingContributions(
			List<Contribution> existingContributions) {
		this.existingContributions = existingContributions;
	}

	@JsonIgnore
	public List<Resource> getExistingResources() {
		return existingResources;
	}

	public void setExistingResources(List<Resource> existingResources) {
		this.existingResources = existingResources;
	}

	@JsonIgnore
	public List<Theme> getExistingThemes() {
		return existingThemes;
	}

	public void setExistingThemes(List<Theme> existingThemes) {
		this.existingThemes = existingThemes;
	}

	/*
	 * Basic Data Operations
	 */
	public static Contribution create(User creator, String title, String text,
			ContributionTypes type) {
		Contribution c = new Contribution(creator, title, text, type);
		c.save();
		c.update();
		return c;
	}

	public static List<Contribution> findAll() {
		List<Contribution> contribs = find.all();
		return contribs;
	}

	public static void create(Contribution c) {

		// 1. Check first for existing entities in ManyToMany relationships.
		// Save them for later update
		// List<User> authors = c.getAuthors();
		List<Theme> themes = c.getThemes(); // new themes are never created from
											// contributions
		c.setThemes(new ArrayList<>());
		List<ComponentMilestone> associatedMilestones = c
				.getAssociatedMilestones(); // new milestones are never created
											// from contributions
		c.setAssociatedMilestones(new ArrayList<>());

		List<Hashtag> existingHashtags = c.getExistingHashtags();
		List<WorkingGroup> existingWorkingGroups = c
				.getExistingResponsibleWorkingGroups();
		List<Contribution> existingContributions = c.getExistingContributions();
		List<Resource> existingResources = c.getExistingResources();
		List<Theme> existingThemes = c.getExistingThemes();

		// 2. Check if there are working group associated and save the groups in
		// a list to add the contribution to them later
		List<WorkingGroup> workingGroupAuthors = c.getWorkingGroupAuthors(); // new
																				// working
																				// groups
																				// are
																				// never
																				// created
																				// from
																				// contributions

		c.save();

		// 3. Add existing entities in relationships to the manytomany resources
		// then update
		ResourceSpace cResSpace = c.getResourceSpace();
		if (themes != null && !themes.isEmpty())
			cResSpace.getThemes().addAll(themes);
		if (associatedMilestones != null && !associatedMilestones.isEmpty())
			cResSpace.getMilestones().addAll(associatedMilestones);
		if (existingWorkingGroups != null && !existingWorkingGroups.isEmpty())
			cResSpace.getWorkingGroups().addAll(existingWorkingGroups);
		if (existingHashtags != null && !existingHashtags.isEmpty())
			cResSpace.getHashtags().addAll(existingHashtags);
		if (existingContributions != null && !existingContributions.isEmpty())
			cResSpace.getContributions().addAll(existingContributions);
		if (existingResources != null && !existingResources.isEmpty())
			cResSpace.getResources().addAll(existingResources);
		if (existingThemes != null && !existingThemes.isEmpty())
			cResSpace.getThemes().addAll(existingThemes);

		cResSpace.update();

		// 4. Add contribution to working group authors
		for (WorkingGroup workingGroup : workingGroupAuthors) {
			workingGroup.addContribution(c);
			workingGroup.update();
			workingGroup.refresh();
		}

		c.refresh();
	}

	public static Contribution read(Long contributionId) {
		return find.ref(contributionId);
	}

	public static Contribution createObject(Contribution c) {
		c.save();
		return c;
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static void delete(Contribution c) {
		c.delete();
	}

	public static void softDelete(Long id) {
		Contribution c = find.ref(id);
		c.setRemoved(true);
		c.setRemoval(new Date());
		c.update();
	}

	public static void softDelete(Contribution c) {
		c.setRemoved(true);
		c.setRemoval(new Date());
		c.update();
	}

	public static void softRecovery(Long id) {
		Contribution c = find.ref(id);
		c.setRemoved(false);
		c.setRemoval(null);
		c.update();
	}

	public static void softRecovery(Contribution c) {
		c.setRemoved(false);
		c.setRemoval(null);
		c.update();
	}

	public static Contribution update(Contribution c) {
		c.update();
		c.refresh();
		return c;
	}

	/*
	 * Other Queries
	 */

	public static Contribution readByUUID(UUID contributionUUID) {
		return find.where().eq("uuid", contributionUUID).findUnique();
	}

	public static Integer readByTitle(String title) {
		ExpressionList<Contribution> contributions = find.where().eq("title",
				title);
		return contributions.findList().size();
	}

	public static List<Contribution> findAllByContainingSpace(Long sid) {
		List<Contribution> contribs = find.where()
				.eq("containingSpaces.resourceSpaceId", sid).findList();
		return contribs;
	}

	public static List<Contribution> findAllByContainingSpaceAndQuery(Long sid,
			String query) {
		List<Contribution> contribs = find.where()
				.eq("containingSpaces.resourceSpaceId", sid)
				.ilike("textIndex", "%" + query + "%").findList();
		return contribs;
	}

	public static List<Contribution> findAllByContainingSpaceAndUUID(UUID uuid) {
		List<Contribution> contribs = find.where()
				.eq("containingSpaces.uuid", uuid).findList();
		return contribs;
	}

	public static List<Contribution> readContributionsOfSpace(
			Long resourceSpaceId) {
		return find.where()
				.eq("containingSpaces.resourceSpaceId", resourceSpaceId)
				.findList();
	}

	public static Contribution readByIdAndType(Long resourceSpaceId,
			Long contributionId, ContributionTypes type) {
		return find.where()
				.eq("containingSpaces.resourceSpaceId", resourceSpaceId)
				.eq("contributionId", contributionId).eq("type", type)
				.findUnique();
	}

	public static List<Contribution> readListByContainingSpaceAndType(
			Long resourceSpaceId, ContributionTypes type) {
		return find.where()
				.eq("containingSpaces.resourceSpaceId", resourceSpaceId)
				.eq("type", type).findList();
	}

	public static void deleteContributionByIdAndType(Long contributionId,
			ContributionTypes cType) {
		find.where().eq("contributionId", contributionId).eq("type", cType)
				.findUnique().delete();
	}

	public static List<Contribution> readByCreator(User u) {
		return find.where().eq("authors.userId", u.getUserId()).findList();
	}

	public static List<Contribution> findAllByContainingSpaceAndType(
			ResourceSpace rs, String t) {
		return find.where().eq("containingSpaces", rs)
				.eq("type", t.toUpperCase()).findList();
	}

	public static List<Contribution> findAllByContainingSpaceAndTypeAndQuery(
			ResourceSpace rs, String t, String query) {
		return find.where().eq("containingSpaces", rs)
				.eq("type", t.toUpperCase())
				.ilike("textIndex", "%" + query + "%").findList();
	}
	
	public static List<Contribution> findAllByContainingSpaceIdAndType(
			ResourceSpace rs, String t) {
		return find.where().eq("containingSpaces.resourceSpaceId", rs)
				.eq("type", t.toUpperCase()).findList();
	}

	public static List<Contribution> findAllByContainingSpaceIdAndTypeAndQuery(
			ResourceSpace rs, String t, String query) {
		return find.where().eq("containingSpaces.resourceSpaceId", rs)
				.eq("type", t.toUpperCase())
				.ilike("textIndex", "%" + query + "%").findList();
	}

	/* Single Contribution queries */

	public static Contribution readIssueOfSpace(Long resourceSpaceId,
			Long contributionId) {
		return readByIdAndType(resourceSpaceId, contributionId,
				ContributionTypes.ISSUE);
	}

	public static Contribution readIdeaOfSpace(Long resourceSpaceId,
			Long contributionId) {
		return readByIdAndType(resourceSpaceId, contributionId,
				ContributionTypes.IDEA);
	}

	public static Contribution readQuestionOfSpace(Long resourceSpaceId,
			Long contributionId) {
		return readByIdAndType(resourceSpaceId, contributionId,
				ContributionTypes.QUESTION);
	}

	public static Contribution readCommentOfSpace(Long resourceSpaceId,
			Long contributionId) {
		return readByIdAndType(resourceSpaceId, contributionId,
				ContributionTypes.COMMENT);
	}

	/* List Contribution queries */

	public static Contribution readForumPostOfSpace(Long resourceSpaceId,
			Long contributionId) {
		return readByIdAndType(resourceSpaceId, contributionId,
				ContributionTypes.FORUM_POST);
	}

	public static Contribution readProposalOfSpace(Long resourceSpaceId,
			Long contributionId) {
		return readByIdAndType(resourceSpaceId, contributionId,
				ContributionTypes.PROPOSAL);
	}

	public static List<Contribution> readIssuesOfSpace(Long resourceSpaceId) {
		return readListByContainingSpaceAndType(resourceSpaceId,
				ContributionTypes.ISSUE);
	}

	public static List<Contribution> readIdeasOfSpace(Long resourceSpaceId) {
		return readListByContainingSpaceAndType(resourceSpaceId,
				ContributionTypes.IDEA);
	}

	public static List<Contribution> readQuestionsOfSpace(Long resourceSpaceId) {
		return readListByContainingSpaceAndType(resourceSpaceId,
				ContributionTypes.QUESTION);
	}

	public static List<Contribution> readCommentsOfSpace(Long resourceSpaceId) {
		return readListByContainingSpaceAndType(resourceSpaceId,
				ContributionTypes.COMMENT);
	}

	@PrePersist
	private void onCreate() {
		// 3. Check if there is not a type
		if (this.type == null)
			this.type = ContributionTypes.COMMENT;
		AuditContribution ac = new AuditContribution(this);
		ac.setAuditEvent(AuditEventTypes.CREATION);
		ac.setAuditUserId(this.getContextUserId());
		ac.save();
	}

	@PreRemove
	private void onDelete() {
		AuditContribution ac = new AuditContribution(this);
		ac.setAuditEvent(AuditEventTypes.DELETE);
		ac.setAuditUserId(this.getContextUserId());
		ac.save();
	}

	@PreUpdate
	private void onUpdateContribution() {
		// 1. Update text index if needed
		String newTextIndex = this.title + "\n" + this.text;
		if (this.textIndex != null && !this.textIndex.equals(newTextIndex))
			this.textIndex = newTextIndex;

		// 4. Add auditing
		AuditContribution ac = new AuditContribution(this);
		ac.setAuditEvent(AuditEventTypes.UPDATE);
		// get user from context?
		ac.setAuditUserId(this.getContextUserId());
		ac.save();
	}

	@PostUpdate
	private void afterUpdate() {
		// 2. Update replies stats
		int numberComments = this.resourceSpace.getContributionsFilteredByType(
				ContributionTypes.COMMENT).size();
		if (numberComments != this.stats.getReplies())
			this.stats.setReplies(new Long(numberComments));
		this.stats.update();
	}

	public static boolean isUserAuthor(User u, Long contributionId) {
		return find.where().eq("contributionId", contributionId)
				.eq("authors.userId", u.getUserId()).findUnique() != null
				|| find.where()
						.eq("contributionId", contributionId)
						.eq("workingGroupAuthors.members.user.userId",
								u.getUserId()).findUnique() != null;
	}

	public static List<WorkingGroup> listWorkingGroupAuthors(Long contributionId) {
		List<WorkingGroup> wgs = workingGroupFinder.where()
				.eq("resources.contributions.contributionId", contributionId)
				.findList();
		return wgs;
	}

}
