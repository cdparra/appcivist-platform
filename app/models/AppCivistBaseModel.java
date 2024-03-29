package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Where;
import com.fasterxml.jackson.annotation.JsonFormat;

import utils.GlobalData;

@MappedSuperclass
@Where(clause="removed=false")
public class AppCivistBaseModel extends Model {

	/** 
	 * Properties that are common to all the entities in the model
	 * 
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm a z")
	private Date creation = new Date(); // by Default, the creation is NOW
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm a z")
	private Date lastUpdate = new Date(); // by Default, the creation is NOW
	private String lang = GlobalData.DEFAULT_LANGUAGE; // defaults language to English 
													 // TODO get the language automatically from 
													 // from the requests that creates it
	
	// Fields to implement 'soft' deletion 
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm a z")
	private Date removal = null;
	@Column(name="removed")
	private Boolean removed = false;
			
	@Transient
	private Long contextUserId;
	/**
	 * Empty constructor
	 */
	public AppCivistBaseModel() {
		super();
	}
	
	public AppCivistBaseModel(Date creation, String lang) {
		super();
		this.creation = creation;
		this.lang = lang;
	}
	
	

	public AppCivistBaseModel(Date creation, Date lastUpdate, String lang,
			Date removal, Boolean removed) {
		super();
		this.creation = creation;
		this.lastUpdate = lastUpdate;
		this.lang = lang;
		this.removal = removal;
		this.removed = removed;
	}

	/** 
	 * Constructor with a specific language
	 * @param lang
	 */
	public AppCivistBaseModel(String lang) {
		super();
		this.lang = lang;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getRemoval() {
		return removal;
	}

	public void setRemoval(Date removal) {
		this.removal = removal;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public Long getContextUserId() {
		return contextUserId;
	}

	public void setContextUserId(Long contextUserId) {
		this.contextUserId = contextUserId;
	}

	public void updateNow() {
		this.lastUpdate = new Date();
	}
	
	public void softRemove() {
		this.removal = new Date();
		this.removed = true;
		this.save();
	}
	
	public static void softRemove(AppCivistBaseModel baseAppcivistObject) {
		baseAppcivistObject.removal = new Date();
		baseAppcivistObject.removed = true;
		baseAppcivistObject.save();
	}

	@PreUpdate
	public void onUpdate() {
		this.lastUpdate = new Date();
	}
}
