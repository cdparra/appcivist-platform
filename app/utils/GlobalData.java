package utils;

import enums.CampaignTypesEnum;

public class GlobalData {
	public static final String APPCIVIST_BASE_URL = "/api";
	public static final String APPCIVIST_ASSEMBLY_BASE_URL = APPCIVIST_BASE_URL+"/assembly";
	public static final String APPCIVIST_ASSEMBLY_DEFAULT_ICON = "/assets/images/justicia-140.png"; 
	public static final String APPCIVIST_ASSEMBLY_DEFAULT_COVER = "/assets/images/covers/2015_GoldenGateFromPointBonita-1920.JPG";
	/**
	 * Internationalization Messages Bindings
	 */
	public static final String DEFAULT_LANGUAGE = "en";
	
	// Assemblies
	public static final String ASSEMBLY_CREATE_MSG_SUCCESS = "assemblies.creation.success";
	public static final String ASSEMBLY_CREATE_MSG_ERROR = "assemblies.creation.error";
	public static String ASSEMBLY_BASE_PATH = "/assembly";
	
	// Working Groups
	public static String GROUP_CREATE_MSG_SUCCESS = "groups.creation.success";
	public static String GROUP_CREATE_MSG_ERROR = "groups.creation.error";
	public static String GROUP_BASE_PATH = "/group";

	// Working Groups Membership
	public static String MEMBERSHIP_INVITATION_CREATE_MSG_SUCCESS = "membership.invitation.creation.success";
	public static String MEMBERSHIP_INVITATION_CREATE_MSG_ERROR = "membership.invitation.creation.error";
	public static String MEMBERSHIP_INVITATION_CREATE_MSG_UNAUTHORIZED = "membership.invitation.creation.unauthorized";
	public static String MEMBERSHIP_INVITATION_BASE_PATH = "/membership";
	
	// Role
	public static String ROLE_CREATE_MSG_SUCCESS = "roles.creation.success";
	public static String ROLE_CREATE_MSG_ERROR = "roles.creation.error";
	public static String ROLE_BASE_PATH = "/role";

	// Config
	public static String CONFIG_CREATE_MSG_SUCCESS = "configs.creation.success";
	public static String CONFIG_CREATE_MSG_ERROR = "configs.creation.error";
	public static String CONFIG_BASE_PATH = "/config";

	// Campaign
	public static String CAMPAIGN_CREATE_MSG_SUCCESS = "campaign.creation.success";
	public static String CAMPAIGN_CREATE_MSG_ERROR = "campaign.creation.error";
	public static String CAMPAIGN_BASE_PATH = "/campaign";
	public static CampaignTypesEnum DEFAULT_CAMPAIGN_TYPE = CampaignTypesEnum.PARTICIPATORY_BUDGETING;

	// Campaign Phase
	public static String CAMPAIGN_PHASE_CREATE_MSG_SUCCESS = "campaign.phase.creation.success";
	public static String CAMPAIGN_PHASE_CREATE_MSG_ERROR = "campaign.phase.creation.error";
	public static String CAMPAIGN_PHASE_BASE_PATH = "/phase";

	// Contribution
	public static String CONTRIBUTION_CREATE_MSG_SUCCESS = "contribution.creation.success";
	public static String CONTRIBUTION_CREATE_MSG_ERROR = "contribution.creation.error";
	public static String CONTRIBUTION_UPDATE_MSG_ERROR = "contribution.update.error";
	public static String CONTRIBUTION_BASE_PATH = "/contribution";
	

	public final static String USER_ROLE = "USER";
	public final static String ADMIN_ROLE = "ADMIN";
	public final static String COORDINATOR_ROLE = "COORDINATOR";
	public final static String MEMBER_ROLE = "MEMBER";
	public final static String FOLLOWER_ROLE = "FOLLOWER";
	
	// External Services Constants
	public final static String GRAVATAR_BASE_URL = "http://www.gravatar.com/avatar/";
	public final static String ETHERPAD_BASE_URL = "http://localhost:9001/";
	
}
