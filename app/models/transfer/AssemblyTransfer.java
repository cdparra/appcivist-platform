package models.transfer;

import java.util.List;
import java.util.UUID;

public class AssemblyTransfer {
	private Long assemblyId;
	private UUID uuid;
	private String name;
	private String shortname;
	private String description; 
	private Boolean listed;
	private AssemblyProfileTransfer profile;
	private LocationTransfer location;
	private List<ThemeTransfer> themes;
	private List<ThemeTransfer> existingThemes;
	private List<ConfigTransfer> configs;
	private List<InvitationTransfer> invitations;
	private List<LinkedAssemblyTransfer> linkedAssemblies;
	public AssemblyTransfer() {
		super();
	}
	public Long getAssemblyId() {
		return assemblyId;
	}
	public void setAssemblyId(Long assemblyId) {
		this.assemblyId = assemblyId;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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
	public Boolean getListed() {
		return listed;
	}
	public void setListed(Boolean listed) {
		this.listed = listed;
	}
	public AssemblyProfileTransfer getProfile() {
		return profile;
	}
	public void setProfile(AssemblyProfileTransfer profile) {
		this.profile = profile;
	}
	public LocationTransfer getLocation() {
		return location;
	}
	public void setLocation(LocationTransfer location) {
		this.location = location;
	}
	public List<ThemeTransfer> getThemes() {
		return themes;
	}
	public void setThemes(List<ThemeTransfer> themes) {
		this.themes = themes;
	}
	public List<ThemeTransfer> getExistingThemes() {
		return existingThemes;
	}
	public void setExistingThemes(List<ThemeTransfer> existingThemes) {
		this.existingThemes = existingThemes;
	}
	public List<ConfigTransfer> getConfigs() {
		return configs;
	}
	public void setConfigs(List<ConfigTransfer> configs) {
		this.configs = configs;
	}
	public List<InvitationTransfer> getInvitations() {
		return invitations;
	}
	public void setInvitations(List<InvitationTransfer> invitations) {
		this.invitations = invitations;
	}
	public List<LinkedAssemblyTransfer> getLinkedAssemblies() {
		return linkedAssemblies;
	}
	public void setLinkedAssemblies(List<LinkedAssemblyTransfer> linkedAssemblies) {
		this.linkedAssemblies = linkedAssemblies;
	}
}
