package edu.wehi.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class FileUtil
{
	
	public static String convertResourceToString(URL url)
	{
		try
		{
			return Resources.toString(url, Charsets.UTF_8);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static String convertResourceToString(String path)
	{
		return convertResourceToString(Resources.getResource(path));
	}
	
	public static String convertResourceToString(URI url)
	{
		try
		{
			return convertResourceToString(url.toURL());
		}
		catch (MalformedURLException e)
		{
			return null;
		}
	}
	
}
