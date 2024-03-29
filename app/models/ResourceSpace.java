package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.avaje.ebean.annotation.Index;
import com.avaje.ebean.annotation.Where;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import enums.CampaignTemplatesEnum;
import enums.ContributionTypes;
import enums.ResourceSpaceTypes;
import enums.ResourceTypes;

@Entity
@JsonInclude(Include.NON_EMPTY)
public class ResourceSpace extends AppCivistBaseModel {

	@Id
	@GeneratedValue
	private Long resourceSpaceId;
	@Index
	private UUID uuid = UUID.randomUUID();
	@Transient
	private String uuidAsString;
	@Enumerated(EnumType.STRING)
	private ResourceSpaceTypes type = ResourceSpaceTypes.ASSEMBLY;
	private UUID parent;

	/**
	 * Configuration parameters, e.g., modules and functionalities of modules
	 * that are enabled, things specific to one assembly, future plugins or
	 * extended services configurations. 
	 * 
	 * When creating entities with an associated resourceSpace, bare in mind 
	 * the following rules about what can be contained in the lists below
	 * configs 		=> new 
	 * themes 			=> existing and new
	 * campaigns       => existing  
	 * components 		=> existing and new
	 * milestones 		=> new
	 * workingGroups   => existing
	 * contributions   => existing and new
	 * assemblies      => existing 
	 * resources       => new
	 * hashtags        => existing and new
	 * 
	 * The ones marked "existing" means that they will have an special array to
	 * refer to the existing related entities and therefore issue an upudate on 
	 * the resource space rather than a save
	 */
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_config")
	@Where(clause="${ta}.removed=false")
	private List<Config> configs = new ArrayList<Config>();

	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "resource_space_theme")
	@JsonIgnoreProperties({ "categoryId" })
	@Where(clause="${ta}.removed=false")
	private List<Theme> themes = new ArrayList<Theme>();

	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "resource_space_campaign")
	@JsonManagedReference
	@Where(clause="${ta}.removed=false")
	private List<Campaign> campaigns = new ArrayList<Campaign>();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "resource_space_campaign_components")
	@Where(clause="${ta}.removed=false")
	private List<Component> components = new ArrayList<Component>();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_campaign_milestones")
	@Where(clause="${ta}.removed=false")
	private List<ComponentMilestone> milestones = new ArrayList<ComponentMilestone>();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_working_groups")
	@Where(clause="${ta}.removed=false")
	private List<WorkingGroup> workingGroups = new ArrayList<WorkingGroup>();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_contributions")
	@Where(clause="${ta}.removed=false")
	private List<Contribution> contributions = new ArrayList<Contribution>();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_assemblies")
	@Where(clause="${ta}.removed=false")
	private List<Assembly> assemblies = new ArrayList<Assembly>();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_resource")
	@Where(clause="${ta}.removed=false")
	private List<Resource> resources = new ArrayList<Resource>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_hashtag")
	@Where(clause="${ta}.removed=false")
	private List<Hashtag> hashtags = new ArrayList<Hashtag>();
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_voting_ballots")
	@Where(clause="${ta}.removed=false")
	private List<VotingBallot> ballots = new ArrayList<VotingBallot>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "resource_space_templates")
	@Where(clause="${ta}.removed=false")
	private List<ContributionTemplate> templates = new ArrayList<ContributionTemplate>();

	
	/*
	 * OneToOne relationships with
	 * - Assembly {resources, forum}
	 * - Working Groups {resources, forum}
	 * - Campaigns {resources}
	 * - Contributions {resourceSpace}
	 * - Component {resourceSpace}
	 */
		
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "resources")
	@JsonIgnore
	private Assembly assemblyResources;
		
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "forum")
	@JsonIgnore
	private Assembly assemblyForum;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "resources")
	@JsonIgnore
	private WorkingGroup workingGroupResources;
		
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "forum")
	@JsonIgnore
	private WorkingGroup workingGroupForum;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "resourceSpace")
	@JsonIgnore
	private Contribution contribution;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "resources")
	@JsonIgnore
	private Campaign campaign;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "resourceSpace")
	@JsonIgnore
	private Component component;
	
	
	/**
	 * The find property is an static property that facilitates database query
	 * creation
	 */
	public static Finder<Long, ResourceSpace> find = new Finder<>(
			ResourceSpace.class);

	/**
	 * Empty constructor
	 */
	public ResourceSpace() {
		super();
		this.uuid = UUID.randomUUID();
	}

	public ResourceSpace(ResourceSpaceTypes type) {
		super();
		this.type = type;
		this.uuid = UUID.randomUUID();
	}

	public ResourceSpace(ResourceSpaceTypes type, UUID parent) {
		super();
		this.type = type;
		this.parent = parent;
		this.uuid = UUID.randomUUID();
	}

	public ResourceSpace(ResourceSpaceTypes type, List<Theme> themes,
			List<Campaign> campaigns, List<Config> configs) {
		super();
		this.type = type;
		this.themes = themes;
		this.campaigns = campaigns;
		this.configs = configs;
	}

	public UUID getResourceSpaceUuid() {
		return uuid;
	}

	public void setResourceSpaceUuid(UUID assemblyResourceSetId) {
		this.uuid = assemblyResourceSetId;
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
		this.uuidAsString = uuidAsString;
		this.uuid = UUID.fromString(uuidAsString);
	}

	public ResourceSpaceTypes getType() {
		return type;
	}

	public void setType(ResourceSpaceTypes type) {
		this.type = type;
	}

	public UUID getParent() {
		return parent;
	}

	public void setParent(UUID parent) {
		this.parent = parent;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> interests) {
		this.themes = interests;
	}

	public void addTheme(Theme theme) {
		if (!this.themes.contains(theme)) 
			this.themes.add(theme);
	}

	public void removeTheme(Theme category) {
		this.themes.remove(category);
	}

	public void copyThemesFrom(ResourceSpace target) {
		List<Theme> themes = target.getThemes();
		for (Theme theme : themes) {
			this.addTheme(theme);
		}
		this.save();
		this.refresh();
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public void addCampaign(Campaign c) {
		if(!this.campaigns.contains(c))
			this.campaigns.add(c);
	}

	public List<Config> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

	public void addConfig(Config c) {
		if(!this.configs.contains(c))
			this.configs.add(c);
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public void addComponent(Component c) {
		if(!this.components.contains(c))
			this.components.add(c);
	}

	public List<ComponentMilestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<ComponentMilestone> milestones) {
		this.milestones = milestones;
	}

	public List<WorkingGroup> getWorkingGroups() {
		return workingGroups;
	}

	public void setWorkingGroups(List<WorkingGroup> workingGroups) {
		this.workingGroups = workingGroups;
	}

	public void addWorkingGroup(WorkingGroup wg) {
		if(!this.workingGroups.contains(wg))
			this.workingGroups.add(wg);
	}

	public List<Contribution> getContributions() {
		return contributions;
	}

	public void setContributions(List<Contribution> contributions) {
		this.contributions = contributions;
	}

	public void addContribution(Contribution c) {
		this.contributions.add(c);
	}

	public List<Assembly> getAssemblies() {
		return assemblies;
	}

	public void setAssemblies(List<Assembly> assemblies) {
		this.assemblies = assemblies;
	}

	public void addAssembly(Assembly a) {
		if (!this.assemblies.contains(a)) {
			this.assemblies.add(a);
		}
	}

	public Long getResourceSpaceId() {
		return resourceSpaceId;
	}

	public void setResourceSpaceId(Long resourceSpaceId) {
		this.resourceSpaceId = resourceSpaceId;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public void addResource(Resource resource) {
		this.resources.add(resource);
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public void addHashtag(Hashtag h) {
		this.hashtags.add(h);
	}

	public List<VotingBallot> getBallots() {
		return ballots;
	}

	public void setBallots(List<VotingBallot> ballots) {
		this.ballots = ballots;
	}

	public void addBallot(VotingBallot b) {
		this.ballots.add(b);
	}

	public List<ContributionTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<ContributionTemplate> templates) {
		this.templates = templates;
	}

	public void addTemplates(ContributionTemplate ct) {
		this.templates.add(ct);
	}
	
	public Assembly getAssemblyResources() {
		return assemblyResources;
	}

	public void setAssemblyResources(Assembly assemblyResources) {
		this.assemblyResources = assemblyResources;
	}

	public Assembly getAssemblyForum() {
		return assemblyForum;
	}

	public void setAssemblyForum(Assembly assemblyForum) {
		this.assemblyForum = assemblyForum;
	}

	public WorkingGroup getWorkingGroupResources() {
		return workingGroupResources;
	}

	public void setWorkingGroupResources(WorkingGroup workingGroupResources) {
		this.workingGroupResources = workingGroupResources;
	}

	public WorkingGroup getWorkingGroupForum() {
		return workingGroupForum;
	}

	public void setWorkingGroupForum(WorkingGroup workingGroupForum) {
		this.workingGroupForum = workingGroupForum;
	}

	public Contribution getContribution() {
		return contribution;
	}

	public void setContribution(Contribution contribution) {
		this.contribution = contribution;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	/*
	 * Basic Data Queries
	 */
	public static void create(ResourceSpace resourceSet) {
		resourceSet.save();
		resourceSet.refresh();
	}

	public static ResourceSpace read(Long resourceSetId) {
		return find.ref(resourceSetId);
	}

	public static ResourceSpace readByUUID(UUID resourceSetUuid) {
		return find.where().eq("uuid", resourceSetUuid).findUnique();
	}

	public static ResourceSpace createObject(ResourceSpace resourceSet) {
		resourceSet.save();
		return resourceSet;
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static void deleteByUUID(UUID resourceSetUuid) {
		find.where().eq("uuid", resourceSetUuid).findUnique().delete();
	}

	public static ResourceSpace update(ResourceSpace resourceSet) {
		resourceSet.update();
		resourceSet.refresh();
		return resourceSet;
	}

	public void setDefaultValues() {
		Campaign defaultCampaign = new Campaign();
		defaultCampaign.setTitle("Default Campaign");
		CampaignTemplate ct = CampaignTemplate.findByName(CampaignTemplatesEnum.PARTICIPATORY_BUDGETING);
		defaultCampaign.setTemplate(ct);

		List<ComponentDefinition> components = ct.getDefComponents();

		for (ComponentDefinition definition : components) {
			Component component = new Component(defaultCampaign, definition);
			defaultCampaign.getResources().getComponents().add(component);
		}
		this.getCampaigns().add(defaultCampaign);
	}

	public static List<ResourceSpace> findByTheme(String theme) {
		return find.where().like("themes.title", "%" + theme + "%").findList();
	}

	public List<Contribution> getContributionsFilteredByType(
			ContributionTypes type) {
		return this.contributions.stream()
				.filter(p -> p.getType() == type)
				.collect(Collectors.toList());
	}
	
	public void setContributionsFilteredByType(List<Contribution> contributions, ContributionTypes type) {
		// 1. Filter the contributions of "type" from the contribution list
		List<Contribution> filteredContributions = this.contributions.stream()
				.filter(p -> p.getType() != type)
				.collect(Collectors.toList());
		
		// 2. Add the new list of contributions of "type"
		filteredContributions.addAll(contributions);

		// 3. Update the list of contributions
		setContributions(filteredContributions);
	}

	public List<Resource> getResourcesFilteredByType(ResourceTypes type) {
		return this.resources.stream()
				.filter(r -> r.getResourceType() == type )
				.collect(Collectors.toList());
	}
	
	/**
	 * Filter campaign by status where status can be "ongoing", "past" or "upcoming"
	 * @param status
	 * @return
	 */
	public List<Campaign> getCampaignsFilteredByStatus(String status) {
		if (status!=null) {
			switch (status) {
			case "ongoing":
				return this.campaigns.stream().filter(p -> p.getActive()).collect(Collectors.toList());
			case "past":
				return this.campaigns.stream().filter(p -> p.getPast()).collect(Collectors.toList());
			case "upcoming":
				return this.campaigns.stream().filter(p -> p.getUpcoming()).collect(Collectors.toList());
			default:
				return this.campaigns;
			}
		}
		return this.campaigns;
	}
	
	public Config getConfigByKey(String key) {
		List<Config> matchingConfigs = this.configs.stream().filter(p -> p.getKey().equals(key)).collect(Collectors.toList());
		if (matchingConfigs != null && !matchingConfigs.isEmpty()) {
			return matchingConfigs.get(0);
		} 
		return null;
	}
}
