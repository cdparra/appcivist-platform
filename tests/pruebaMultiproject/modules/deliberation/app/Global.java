import com.avaje.ebean.Ebean;
import models.Comment;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import java.util.List;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application has started");
        /*if (Comment.find.findRowCount() == 0) {
        	Ebean.save((List) Yaml.load("initial-data.yml"));
        }*/
        
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}