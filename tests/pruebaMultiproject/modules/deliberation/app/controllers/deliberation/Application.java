package controllers.deliberation;

import play.*;
import play.mvc.*;

import views.html.*;
import views.html.deliberation.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

}
