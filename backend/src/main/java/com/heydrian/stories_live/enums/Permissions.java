package com.heydrian.stories_live.enums;

public enum Permissions {
    // User Permissions
    USER_VIEW_PROFILE,
    USER_EDIT_PROFILE,
    USER_CHANGE_PASSWORD,
    USER_CHANGE_USERNAME,
    USER_CHANGE_EMAIL,
    USER_UPLOAD_AVATAR,
    USER_DELETE_ACCOUNT,

    // User Interaction Permissions
    USER_VIEW_OTHER_PROFILES,
    USER_VIEW_PUBLIC_INFO,
    USER_VIEW_ACTIVITY,
    USER_FOLLOW,
    USER_UNFOLLOW,
    USER_BLOCK,
    USER_UNBLOCK,
    USER_MUTE,
    USER_UNMUTE,

    // Account Management Permissions
    USER_VIEW,
    USER_SEARCH,
    USER_DISABLE,
    USER_ENABLE,
    USER_SUSPEND,
    USER_UNSUSPEND,
    USER_BAN,
    USER_UNBAN,
    USER_DELETE,
    USER_EDIT_OWN, // for editing own profile
    USER_EDIT_ANY, // for editing any user's profile (admin)

    // Role Management Permissions
    ROLE_VIEW,
    ROLE_CREATE,
    ROLE_EDIT,
    ROLE_DELETE,
    ROLE_ASSIGN,
    ROLE_REVOKE
}
