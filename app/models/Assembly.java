package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.MaxLength;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.location.Geo;
import models.location.Location;
import utils.GlobalData;
import enums.ManagementTypes;
import enums.Visibility;

@Entity
@JsonInclude(Include.NON_NULL)
public class Assembly extends AppCivistBaseModel {
	@Id
	@GeneratedValue
	private Long assemblyId;

	/**
	 * Properties specific to the Assembly
	 */

	private User creator;
	@MaxLength(value=200)
	private String name; 
	@MaxLength(value=120)
	private String shortname;
	private String description;
	private String icon = GlobalData.APPCIVIST_ASSEMBLY_DEFAULT_ICON;
	private String cover = GlobalData.APPCIVIST_ASSEMBLY_DEFAULT_COVER;
	private String url; 
	private Visibility visibiliy = Visibility.PUBLIC; // assemblies are public by default
    private ManagementTypes managementType = ManagementTypes.COORDINATED; // assemblies are coordinated by default

	// Relationships
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JsonIgnoreProperties({"categoryId"})
	private List<Category> interestCategories = new ArrayList<Category>();

	@OneToMany(mappedBy = "assembly", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Campaign> campaigns = new ArrayList<Campaign>();

	@OneToMany(mappedBy = "assembly", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Config> assemblyConfigs = new ArrayList<Config>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"hashtagId"})
	private List<Hashtag> hashtags = new ArrayList<Hashtag>();

	/**
	 * Experimental properties (things we might want to have)
	 */
	// A location specification that is more precise, based on the GeoJSON
	// standard
	// Basically, with this location object, we can specify whether an entity is
	// located in a specific geo point, route/line or area.
	@OneToOne
	private Location location;

	// If assemblies decide to collaborate on campaigns, then we might want to
	// have
	// the notion of "shared campaigns
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Campaign> sharedCampaigns = new ArrayList<Campaign>();
	
	/**
	 * The find property is an static property that facilitates database query creation
	 */
	public static Finder<Long, Assembly> find = new Finder<Long, Assembly>(
			Long.class, Assembly.class);

	/**
	 * Empty constructor
	 */
	public Assembly() {
		super();
	}

	/**
	 * Basic assembly constructor (with the most basic elements of an assembly)
	 * @param assemblyTitle
	 * @param assemblyDescription
	 * @param assemblyCity
	 */
	public Assembly(String assemblyTitle, String assemblyDescription,
			String assemblyCity) {
		super();
		this.name = assemblyTitle;
		this.description = assemblyDescription;
		this.location = new Location(null, assemblyCity, null, null, null);
	}

	public Assembly(User creator, String name, String description, String city,
			String state, String country, String icon, Visibility visibility) {
		super();
		this.creator = creator;
		this.name = name;
		this.description = description;
		this.location = new Location(null, city, state, null, country);
		this.icon = icon;
		this.visibiliy = visibility;
	}

	/** 
	 * Assembly constructor, including the basic lists of categories (interests), hashtags
	 * and configuration key/value pairs
	 * @param lang 
	 * @param creator
	 * @param name
	 * @param description
	 * @param city
	 * @param state
	 * @param country
	 * @param icon
	 * @param interests
	 * @param hashtags
	 * @param configurations
	 */
	public Assembly(String lang, User creator, String name, String description, String city,
			String state, String country, String icon, 
			Visibility visibility,
			List<Category> interests,
			List<Hashtag> hashtags,
			List<Config> assemblyConfigs) {
		super(lang);
		this.creator = creator;
		this.name = name;
		this.description = description;
		this.location = new Location(null, city, state, null, country);
		this.icon = icon;
		this.visibiliy = visibility;
		this.interestCategories = interests;
		this.hashtags = hashtags;
		this.assemblyConfigs = assemblyConfigs;
	}
	
	/*
	 * Getters and Setters
	 */

	public Long getAssemblyId() {
		return assemblyId;
	}

	public void setAssemblyId(Long assemblyId) {
		this.assemblyId = assemblyId;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Visibility getVisibiliy() {
		return visibiliy;
	}

	public void setVisibiliy(Visibility visibiliy) {
		this.visibiliy = visibiliy;
	}

	public ManagementTypes getManagementType() {
		return managementType;
	}

	public void setManagementType(ManagementTypes membershipRole) {
		this.managementType = membershipRole;
	}

	public List<Category> getInterestCategories() {
		return interestCategories;
	}

	public void setInterestCategories(List<Category> interests) {
		this.interestCategories = interests;
	}
	
	public void addInterestCategory(Category category) {
        this.interestCategories.add(category);
    }
 
    public void removeInterestCategory(Category category) {
        this.interestCategories.remove(category);
    }

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public List<Config> getAssemblyConfigs() {
		return assemblyConfigs;
	}

	public void setAssemblyConfigs(List<Config> assemblyConfigs) {
		this.assemblyConfigs = assemblyConfigs;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Campaign> getSharedCampaigns() {
		return sharedCampaigns;
	}

	public void setSharedCampaigns(List<Campaign> sharedCampaigns) {
		this.sharedCampaigns = sharedCampaigns;
	}

	/*
	 * Basic Data Queries
	 */
	
	/**
	 * Returns all the assemblies in our system
	 * @return
	 */
	public static List<Assembly>  findAll() {
		List<Assembly> assemblies = find.all();
		return assemblies;
	}

	public static void create(Assembly assembly) {
		if (assembly.getAssemblyId() != null
				&& (assembly.getUrl() == null || assembly.getUrl() == "")) {
			assembly.setUrl(GlobalData.APPCIVIST_ASSEMBLY_BASE_URL + "/"
					+ assembly.getAssemblyId());
		}

		assembly.save();
		assembly.refresh();

		if (assembly.getUrl() == null || assembly.getUrl() == "") {
			assembly.setUrl(GlobalData.APPCIVIST_ASSEMBLY_BASE_URL + "/"
					+ assembly.getAssemblyId());
		}
	}

	public static Assembly read(Long assemblyId) {
		return find.ref(assemblyId);
	}

	public static Assembly createObject(Assembly assembly) {
		assembly.save();
		return assembly;
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static Assembly update(Assembly a) {
		a.update();
		a.refresh();
		return a;
	}

	/*
	 * Other Queries
	 * 
	 * TODO: 
	 * [done] Create an assembly
		Create an assembly with a name, a description and a assembly logo/icon. 
		Provide initial configuration
		Establish if the assembly is public/private
		Establish if members can create new issues
		Establish if members can join by request or only by invitation
		Associate hashtags
		Invite users to become members of the assembly
		Configure an assembly
			can members create new issues?
			update hashtags
		Publish the assembly (make assembly public to all users) or keep private
			update proposal template
		Export issues from one assembly into another 
		Export proposals from one assembly into an issue of another assembly
		Import issues from another assembly
		Import proposals from another assembly into an issue [what working group will be assigned to them?]
		Import proposal template from another assembly		
		Subscribe to another assembly
		Publish issues (if assembly is public, all issues are automatically published, i.e., public)	
		Publish proposals (if assembly is public, all proposals are automatically published unless the working group unpublishes the proposal)
	 */
	

}
