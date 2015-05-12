package models;

import play.db.ebean.*;

import java.util.List;
import java.util.ArrayList;

public class CommentCollection extends Model {

    private List<Comment> comments = new ArrayList<Comment>();

    /*
     * Getters and setters
     */

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}