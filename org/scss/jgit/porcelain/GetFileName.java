package org.scss.jgit.porcelain;

public class GetFileName {
	
	public static String getFileNameFromUrl(String url)
	{
		if(url == null)
		{
			return "The string is empty!";
		}
		int indexOfSlash = url.indexOf("/");
		return url.substring(indexOfSlash + 1 , url.length() - 4);
	}

}
