package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="appCivistUser")
public class User extends Model{

    @Id
    private Long userId;
    private String userName;
    private String userPicture;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    public List<Comment> userComments = new ArrayList<Comment>();

    public User(String name, String picture){
        this.userName = name;
        this.userPicture = picture;
    }

    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public static CommentCollection findAllCommentsByUser(Long id){
        User user = find.byId(id);
        List<Comment> comments = user.userComments;
        CommentCollection commentCollection = new CommentCollection();
        commentCollection.setComments(comments);
        return commentCollection;
    }

    public static User findById(Long id){
        User user = find.byId(id);
        return user;
    }

    /*
     * Getters and setters
     */

    public Long get_id() {
        return userId;
    }

    public void set_id(Long _id) {
        this.userId = _id;
    }

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getPicture() {
        return userPicture;
    }

    public void setPicture(String picture) {
        this.userPicture = picture;
    }

    public List<Comment> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<Comment> userComments) {
        this.userComments = userComments;
    }
}
