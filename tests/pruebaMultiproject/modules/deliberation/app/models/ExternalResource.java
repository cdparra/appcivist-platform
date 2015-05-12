package models;

import enums.ExternalResourceTypes;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ExternalResource extends Model{

    @Id
    @GeneratedValue
    private String extResId;
    @NotNull
    private String extResLink;
    private String extResAlias;
    @NotNull
    private ExternalResourceTypes extResType;

    public ExternalResource(String link, String alias, ExternalResourceTypes type) {
        this.extResLink = link;
        this.extResAlias = alias;
        this.extResType = type;
    }

    /*
     * Getters and setters
     */


    public String get_id() {
        return extResId;
    }

    public void set_id(String _id) {
        this.extResId = _id;

    }

    public String getLink() {
        return extResLink;
    }

    public void setLink(String link) {
        this.extResLink = link;
    }

    public String getAlias() {
        return extResAlias;
    }

    public void setAlias(String alias) {
        this.extResAlias = alias;
    }

    public ExternalResourceTypes getType() {
        return extResType;
    }

    public void setType(ExternalResourceTypes type) {
        this.extResType = type;
    }
}
