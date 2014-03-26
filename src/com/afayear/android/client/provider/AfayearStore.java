package com.afayear.android.client.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class AfayearStore {

	public static final String AUTHORITY = "afayear";
	private static final String TYPE_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";
	private static final String TYPE_INT = "INTEGER";
	private static final String TYPE_INT_UNIQUE = "INTEGER UNIQUE";
	private static final String TYPE_BOOLEAN = "INTEGER(1)";
	private static final String TYPE_TEXT = "TEXT";
	private static final String TYPE_TEXT_NOT_NULL = "TEXT NOT NULL";

	public static final Uri BASE_CONTENT_URI = new Uri.Builder()
			.scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY)
			.build();

	public static interface Accounts extends BaseColumns {

		public static final int AUTH_TYPE_OAUTH = 0;
		public static final int AUTH_TYPE_XAUTH = 1;
		public static final int AUTH_TYPE_BASIC = 2;
		public static final int AUTH_TYPE_TWIP_O_MODE = 3;

		public static final String TABLE_NAME = "accounts";
		public static final String CONTENT_PATH = TABLE_NAME;
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				BASE_CONTENT_URI, CONTENT_PATH);

		/**
		 * Login name of the account<br>
		 * Type: TEXT NOT NULL
		 */
		public static final String SCREEN_NAME = "screen_name";

		public static final String NAME = "name";

		/**
		 * Unique ID of the account<br>
		 * Type: INTEGER (long)
		 */
		public static final String ACCOUNT_ID = "account_id";

		/**
		 * Auth type of the account.</br> Type: INTEGER
		 */
		public static final String AUTH_TYPE = "auth_type";

		/**
		 * Password of the account. (It will not stored)<br>
		 * Type: TEXT
		 */
		public static final String PASSWORD = "password";

		/**
		 * Password of the account for basic auth.<br>
		 * Type: TEXT
		 */
		public static final String BASIC_AUTH_PASSWORD = "basic_auth_password";

		/**
		 * OAuth Token of the account.<br>
		 * Type: TEXT
		 */
		public static final String OAUTH_TOKEN = "oauth_token";

		/**
		 * Token Secret of the account.<br>
		 * Type: TEXT
		 */
		public static final String TOKEN_SECRET = "token_secret";

		public static final String REST_BASE_URL = "rest_base_url";

		public static final String SIGNING_REST_BASE_URL = "signing_rest_base_url";

		public static final String OAUTH_BASE_URL = "oauth_base_url";

		public static final String SIGNING_OAUTH_BASE_URL = "signing_oauth_base_url";

		public static final String USER_COLOR = "user_color";

		/**
		 * Set to a non-zero integer if the account is activated. <br>
		 * Type: INTEGER (boolean)
		 */
		public static final String IS_ACTIVATED = "is_activated";

		public static final String CONSUMER_KEY = "consumer_key";

		public static final String CONSUMER_SECRET = "consumer_secret";

		/**
		 * User's profile image URL of the status. <br>
		 * Type: TEXT
		 */
		public static final String PROFILE_IMAGE_URL = "profile_image_url";

		public static final String PROFILE_BANNER_URL = "profile_banner_url";

		public static final String[] COLUMNS = new String[] { _ID, NAME,
				SCREEN_NAME, ACCOUNT_ID, AUTH_TYPE, BASIC_AUTH_PASSWORD,
				OAUTH_TOKEN, TOKEN_SECRET, CONSUMER_KEY, CONSUMER_SECRET,
				REST_BASE_URL, SIGNING_REST_BASE_URL, OAUTH_BASE_URL,
				SIGNING_OAUTH_BASE_URL, PROFILE_IMAGE_URL, PROFILE_BANNER_URL,
				USER_COLOR, IS_ACTIVATED };

		public static final String[] TYPES = new String[] { TYPE_PRIMARY_KEY,
				TYPE_TEXT_NOT_NULL, TYPE_TEXT_NOT_NULL, TYPE_INT_UNIQUE,
				TYPE_INT, TYPE_TEXT, TYPE_TEXT, TYPE_TEXT, TYPE_TEXT,
				TYPE_TEXT, TYPE_TEXT, TYPE_TEXT, TYPE_TEXT, TYPE_TEXT,
				TYPE_TEXT, TYPE_TEXT, TYPE_INT, TYPE_BOOLEAN };

	}
}
