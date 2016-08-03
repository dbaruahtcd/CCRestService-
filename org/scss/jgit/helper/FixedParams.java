package org.scss.jgit.helper;

public final class FixedParams {
	
	private static final String REMOTE_URL = "https://github.com/facebook/css-layout.git";
	private static final String LOCAL_GIT_DIR = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git";

	public static String getRemoteUrl() {
		return REMOTE_URL;
	}
	
	public static String getLocalGitDir()
	{
		return LOCAL_GIT_DIR;
	}

}
