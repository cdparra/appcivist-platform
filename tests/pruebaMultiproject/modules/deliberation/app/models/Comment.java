package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Comment extends Model {

    @Id
    @GeneratedValue
    private Long commentId;
    @NotNull
    private String comment;
    @NotNull
    private Date commentDate;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Tag> commentTags = new ArrayList<Tag>();
    @ManyToMany(cascade = CascadeType.ALL)
    private List<ExternalResource> commentExternalResources = new ArrayList<ExternalResource>();

    @ManyToMany(mappedBy = "userComments")
    private  List<User> user;

    public Comment(String comment, Date date, List<Tag> tags, List<ExternalResource> externalResources, List<User> user) {
        this.comment = comment;
        this.commentDate = date;
        this.commentTags = tags;
        this.commentExternalResources = externalResources;
        this.user = user;
    }

    public static Finder<Long, Comment> find = new Finder<Long, Comment>(Long.class, Comment.class);

    public static CommentCollection findAll() {
        List<Comment> comments = find.all();
        CommentCollection commentCollection = new CommentCollection();
        commentCollection.setComments(comments);
        return commentCollection;
    }

    public static Comment findById(Long id) {
        Comment comment = find.byId(id);
        return comment;
    }

    public static void delete(Long id) {
        find.ref(id).deleteManyToManyAssociations("user");
        find.ref(id).delete();
    }

    public static void saveComment(Long userId, String comment/*, Long commentId*/){
        Date commmentDate = new Date();
        User user = User.findById(userId);
        List<User> users = new ArrayList<User>();
        users.add(user);
        Comment newComment;
        /*if(commentId != null){
            newComment = find.byId(commentId);
            newComment.setComment(comment);
            newComment.setDate(commmentDate);
        }
        else {
            newComment = new Comment(comment,commmentDate,null,null,users);
        }*/
        newComment = new Comment(comment,commmentDate,null,null,users);
        newComment.save();
        newComment.refresh();
    }

    /*
     * Getters and setters
     */

    public Long get_id() {
        return commentId;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public void set_id(Long _id) {
        this.commentId = _id;
    }

    public String getComment() {
        return comment;

    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return commentDate;
    }

    public void setDate(Date date) {
        this.commentDate = date;
    }

    public List<Tag> getTags() {
        return commentTags;
    }

    public void setTags(List<Tag> tags) {
        this.commentTags = tags;
    }

    public List<ExternalResource> getExternalResources() {
        return commentExternalResources;
    }

    public void setExternalResources(List<ExternalResource> externalResources) {
        this.commentExternalResources = externalResources;
    }
}
