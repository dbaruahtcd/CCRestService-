package org.scss.jgit.helper;

public final class FixedParams {
	
	private static final String REMOTE_URL = "https://github.com/facebook/css-layout.git";
	private static final String LOCAL_GIT_DIR = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git";
	protected static final String FILE_PATTERN = "(.*\\.js.*)|(.*\\.cpp.*)|(.*\\.py.*)|(.*\\.c)|(.*\\.rb)|(.*\\.php)|(.*\\.java)|(.*\\.sh)|(.*\\.ml)|(.*\\.hs)"; // hs haskell ml - ocaml
	protected static final String[] keyWords = {"if", "for", "do", "while", "switch", "case", "default", "continue", "break", "&&", "||", "?", "else", "return","unless", "until", "or", "and", "elif"};
	private static String projectUrl = "";
	//private static final String FILE_PATTERN1 = ".*(\\.js.*)|(.*\\.cpp.*)|(.*\\.py.*)|(.*\\.c)|(.*\\.rb)|(.*\\.php)|(.*\\.java)";
	private static String localDirName = "";

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
	
	public static String getProjectUrl()
	{ 
		return projectUrl;
	}
	
	public static void setProjectUrl(String url)
	{
		projectUrl = url;
	}
	
	public static String getLocalDirName()
	{
		return localDirName;
	}
	
	public static void setLocalDirName(String name)
	{
		localDirName = name;
	}
	
	public static String[] getKeywords()
	{
		return keyWords;
	}
}
