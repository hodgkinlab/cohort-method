package edu.wehi.swing;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import edu.wehi.GUI;

public class ImageIconGetter
{
	public static final int s1 = 16;
	public static final int s2 = 22;
	public static final int s3 = 32;
	
	public enum IconSize {
		SMALL(16),
		MED(22),
		LARGE(32);
		
		public final int size;
		
		IconSize(int size)
		{
			this.size = size;
		}
	}
	
	
	public static final int s = s2;
	
	public static final String jar = "org/tango-project/tango-icon-theme";
	
	public static final String terminal = 		"apps/utilities-terminal.png";
	public static final String dataBrowsing = 	"mimetypes/x-office-spreadsheet.png";
	public static final String modelling = 	"apps/accessories-calculator.png";
	public static final String drawings =		"mimetypes/x-office-drawing.png";
	
	protected static ImageIcon createImageIcon(String uri)
	{
		uri = "/" + uri;
		try
		{
			ImageIcon.class.getClassLoader();
			URL imgURL;
			
			imgURL = ClassLoader.getSystemResource(uri);
			if (imgURL == null)
			{
				imgURL = ImageIcon.class.getResource(uri);
			}
			
			BufferedImage buf;
			if (imgURL == null)
			{
				buf = ImageIO.read(ImageIcon.class.getResourceAsStream(uri));
			}
			else
			{
				buf = ImageIO.read(imgURL);
			}
			
			
			return new ImageIcon(buf);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static ImageIcon getScriptIcon(String icon)
	{
		return getScriptIcon(icon, s);
	}
	
	public static ImageIcon getScriptIcon(String icon, IconSize size)
	{
		return getScriptIcon(icon, size.size);
	}
	
	private static ImageIcon getScriptIcon(String icon, int s)
	{
		return createImageIcon(jar+"/"+s+"x"+s+"/"+icon);
	}
	
	public static void main(String[] args)
	{
		GUI.gui(new JButton("test", ImageIconGetter.getScriptIcon(modelling)));
	}
	
}
