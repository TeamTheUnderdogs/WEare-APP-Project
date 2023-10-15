package com.weare.testframework.api.utils;

import com.weare.testframework.api.models.*;

import static com.weare.testframework.Utils.getConfigPropertyByKey;

public class Constants {
    public static int POST_ID = -1;

    public static int COMMENT_ID = -1;

    // User with name Buddy and id 86 was created
    public static final int USERNAME_INDEX = 3;
    public static final int USER_ID_INDEX = 6;

    public static final String ROLE_USER = "ROLE_USER";

    // TODO: Use registered user id?
    public static int USER_ID = Integer.parseInt(getConfigPropertyByKey("social.api.userId")); //74; //-1;

    public static String USERNAME = getConfigPropertyByKey("social.api.username");
    public static String PASSWORD = getConfigPropertyByKey("social.api.password");

    public static String EMAIL;

    public static final CategoryModel CATEGORY_ALL = new CategoryModel(100, "All");

    public static int SKILL_ID = -1;

    public static String SKILL;

    public static final PageModel PAGE = new PageModel(0, true, "", "", 10);

    public static final double AVAILABILITY = 0.5;

    public static final LocationModel LOCATION = new LocationModel(0, 1, "Sofia");

    public static final String POST_DEFAULT_PICTURE = "";

    public static final boolean POST_PUBLIC = true;
}
