package com.ibm.sbt.test.lib;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;

public class TestEnvironment {

	private final static String CONNECTIONS = "connections";
	private final static String SMARTCLOUD = "smartcloud";
	private final static String CURRENT_USER = "user";
	private final static String OTHER_USER = "otheruser";
	private final static String CURRENT_USER_MAIL = "mail";
	private final static String OTHER_USER_MAIL = "othermail";
	private final static String CURRENT_USER_PASSWORD = "password";
	private final static String OTHER_USER_PASSWORD = "otherpassword";
	private final static String CURRENT_USER_LOGIN = "username";
	private final static String OTHER_USER_LOGIN = "othername";

	private final static String CURRENT_USER_DISPLAYNAME = "userDisplayName";
	private final static String OTHER_USER_DISPLAYNAME = "otherDisplayName";

	private static boolean requiresAuthentication;
	private static boolean enableSmartcloud = System.getProperty(
			"testEnvironment", System.getProperty("environment","connections")).equals("smartcloud");
	private static String mockMode;
	private static final TestEnvironment instance = new TestEnvironment();

	protected TestEnvironment() {
		selectEnvironment();
	}

	private static void selectEnvironment() {
		mockMode = System.getProperty("testMode", "pass");
		String environment = enableSmartcloud ? "smartcloud" : "connections";
		String parsed = null;
		if (mockMode.equalsIgnoreCase("pass")) {
			parsed = "Passthrough";
		}
		if (mockMode.equalsIgnoreCase("mock")) {
			parsed = "Mocking";
		}
		if (mockMode.equalsIgnoreCase("record")) {
			parsed = "Recording";
		}
		if (parsed == null)
			throw new MockingException(null, "Unrecognized mode " + mockMode
					+ " use one of [pass, mocked, record]");

		System.setProperty("environment", environment + parsed + "Environment");
	}

	public TestEnvironment get() {
		return instance;
	}

	public static void enableSmartCloud(boolean b) {
		enableSmartcloud = b;
		selectEnvironment();
	}

	public static void setRequiresAuthentication(boolean b) {
		requiresAuthentication = b;
	}

	public static boolean getRequiresAuthentication() {
		return requiresAuthentication;
	}

	private static String getPropertyBasePath() {
		if (isSmartCloudEnvironment()) {
			return SMARTCLOUD + ".";
		}
		return CONNECTIONS + ".";
	}

	public static boolean isSmartCloudEnvironment() {
		return enableSmartcloud;
	}

	public static boolean isMockMode() {
		return mockMode.equalsIgnoreCase("mock");
	}

	public static String getSecondaryUserEmail() {
		return Context.get().getProperty(
				getPropertyBasePath() + OTHER_USER_MAIL);
	}

	public static String getCurrentUserEmail() {
		return Context.get().getProperty(
				getPropertyBasePath() + CURRENT_USER_MAIL);
	}

	public static String getSecondaryUserUuid() {
		return Context.get().getProperty(getPropertyBasePath() + OTHER_USER);
	}

	public static String getCurrentUserUuid() {
		return Context.get().getProperty(getPropertyBasePath() + CURRENT_USER);
	}

	public static String getSecondaryUserPassword() {
		return Context.get().getProperty(
				getPropertyBasePath() + OTHER_USER_PASSWORD);
	}

	public static String getCurrentUserPassword() {
		return Context.get().getProperty(
				getPropertyBasePath() + CURRENT_USER_PASSWORD);
	}

	public static String getSecondaryUsername() {
		return Context.get().getProperty(
				getPropertyBasePath() + OTHER_USER_LOGIN);
	}

	public static String getCurrentUsername() {
		return Context.get().getProperty(
				getPropertyBasePath() + CURRENT_USER_LOGIN);
	}

	public static String getCurrentUserDisplayName() {
		return Context.get().getProperty(
				getPropertyBasePath() + CURRENT_USER_DISPLAYNAME);
	}

	public static String getSecondaryUserDisplayName() {
		return Context.get().getProperty(
				getPropertyBasePath() + OTHER_USER_DISPLAYNAME);
	}

	public static String getEnvironmentProperty(String name) {
		return Context.get().getProperty(
				getPropertyBasePath() + name);
	}

    protected static String getProperty(String p) {
        Context ctx = RuntimeFactory.get().getContextUnchecked();
        return System.getProperty(p, ctx != null? Context.get().getProperty(p) : null);
    }

}
