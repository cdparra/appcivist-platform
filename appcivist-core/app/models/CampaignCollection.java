package models;

import java.util.*;

import play.db.ebean.*;

public class CampaignCollection extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7213623744477425962L;
	private List<Campaign> campaigns;

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}	
	
	
}
