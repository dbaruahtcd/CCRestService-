package org.scss.jgit.helper;

public final class FixedParams {
	
	private static final String REMOTE_URL = "https://github.com/facebook/css-layout.git";
	private static final String LOCAL_GIT_DIR = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git";
	protected static final String FILE_PATTERN = ".*(\\.js.*)|(.*\\.cpp.*)|(.*\\.py.*)|(.*\\.c)|(.*\\.rb)|(.*\\.php)|(.*\\.java)|(.*\\.sh)|(.*\\.ml)|(.*\\.hs)"; // hs haskell ml - ocaml
	private static String projectUrl = "";
	//private static final String FILE_PATTERN1 = ".*(\\.js.*)|(.*\\.cpp.*)|(.*\\.py.*)|(.*\\.c)|(.*\\.rb)|(.*\\.php)|(.*\\.java)";


	public static String getRemoteUrl() {
		return REMOTE_URL;
	}
	
	public static String getLocalGitDir()
	{
		return LOCAL_GIT_DIR;
	}
	
	public static String getFilePattern()
	{
		return FILE_PATTERN;
		
	}
	
	public String getProjectUrl()
	{ 
		return projectUrl;
	}
	
	public static void setProjectUrl(String url)
	{
		projectUrl = url;
	}

}
