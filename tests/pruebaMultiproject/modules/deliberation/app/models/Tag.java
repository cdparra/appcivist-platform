package models;

import enums.TagTypes;
import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Tag extends Model{

    @Id
    private String tagName;
    @NotNull
    private TagTypes tagType;

    public Tag(String name, TagTypes type) {
        this.tagName = name;
        this.tagType = type;
    }
/*
     * Getters and setters
     */

    public String getName() {
        return tagName;
    }

    public void setName(String name) {
        this.tagName = name;
    }

    public TagTypes getType() {
        return tagType;
    }

    public void setType(TagTypes type) {
        this.tagType = type;
    }
}