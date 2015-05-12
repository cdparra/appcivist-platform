package controllers.deliberation;

import models.Comment;
import models.CommentCollection;
import models.User;
import play.libs.Json;
import play.mvc.*;

public class Users extends Controller{

    /**
     * Return all the comments by user
     * @param userId
     */
    public static Result findCommentsByUser(Long userId){
        CommentCollection comments = User.findAllCommentsByUser(userId);
        return ok(Json.toJson(comments));
    }

    /**
     * Return user by Id
     * @param userId
     */
    public static Result findUserById(Long userId){
        User user = User.findById(userId);
        return ok(Json.toJson(user));
    }

}
