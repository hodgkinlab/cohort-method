package edu.wehi.celcalc.cohort;

import java.awt.Image;
import java.awt.Toolkit;

public class ImageUtil {

	
	public static Image getApplicationIcon()
	{
		return Toolkit.getDefaultToolkit().getImage(ImageUtil.class.getResource("icon/application_icon.png"));
	}
	
}
