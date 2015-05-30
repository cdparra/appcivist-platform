package models;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import enums.MembershipRoles;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class WorkingGroup extends Model{

    //Commons
    private User creator;
    private Date creation = new Date(); // it will automatically assign the current date by default
    private Date removal;
    private String lang;

    @Id
    private Long groupId;
    private String name;
    private String text;
    private Date expiration;
    private Boolean isPublic = true;
    private Boolean acceptRequests = true;
    private MembershipRoles membershipRole = MembershipRoles.MEMBER;

    @ManyToMany(mappedBy = "workingGroups")
    private List<Assembly> assemblies = new ArrayList<Assembly>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Resource> resources = new ArrayList<Resource>();

    @JsonIgnore
    @OneToMany(mappedBy="workingGroup", cascade = CascadeType.ALL)
    private List<GroupMembership> memberships = new ArrayList<GroupMembership>();

    public static Model.Finder<Long, WorkingGroup> find = new Model.Finder<Long, WorkingGroup>(
            Long.class, WorkingGroup.class);

    public static WorkingGroup read(Long workingGroupId) {
        return find.ref(workingGroupId);
    }

    public static List<WorkingGroup> findAll() {
        return find.all();
    }

    public static Integer readByTitle(String name){
        ExpressionList<WorkingGroup> wGroups = find.where().eq("name", name);
        return wGroups.findList().size();
    }

    public static WorkingGroup create(WorkingGroup workingGroup) {
        workingGroup.save();
        workingGroup.refresh();
        return workingGroup;
    }

    public static WorkingGroup createObject(WorkingGroup workingGroup) {
        workingGroup.save();
        return workingGroup;
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public static void update(Long id) {
        find.ref(id).update();
    }

    public User getCreator() {
        return creator;

    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getRemoval() {
        return removal;
    }

    public void setRemoval(Date removal) {
        this.removal = removal;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public List<Assembly> getAssemblies() {
        return assemblies;
    }

    public void setAssemblies(List<Assembly> assemblies) {
        this.assemblies = assemblies;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<GroupMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<GroupMembership> memberships) {
        this.memberships = memberships;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getAcceptRequests() {
        return acceptRequests;
    }

    public void setAcceptRequests(Boolean acceptRequests) {
        this.acceptRequests = acceptRequests;
    }

    public MembershipRoles getMembershipRole() {
        return membershipRole;
    }

    public void setMembershipRole(MembershipRoles membershipRole) {
        this.membershipRole = membershipRole;
    }
}
