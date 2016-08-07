package org.scss.jgit.test;

public class CreateFileName {
	
	public static String getFileNameFromUrl(String str)
	{
		if(str == null)
			return "Invalid url :" + str;
		
		int index = str.lastIndexOf("/"); 
		System.out.println("index is :"+ index + "length is :" + str.length());
		
		String result = str.substring(index + 1, str.length() - 4);
		return 	result;
				
	}
	
	public static void main(String[] args)
	{
		String url = "https://github.com/facebook/WebDriverAgent.git";
		System.out.println(getFileNameFromUrl(url));
	}

}
