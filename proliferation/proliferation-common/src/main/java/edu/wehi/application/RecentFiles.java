package edu.wehi.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.wehi.util.FixedSizeContainer;

public class RecentFiles implements Serializable
{

	private static final long serialVersionUID = 1L;

	/** Container of old files **/
	FixedSizeContainer<File> recentFiles;
	
	/** File where this is saved **/
	File file;

	
	public RecentFiles(int size, String path)
	{
		try
		{
			this.file = new File(new File(".").getCanonicalPath()+"/"+path);
			this.file.getParentFile().mkdirs();
		}
		catch (IOException e1)
		{
			this.file =  new File(path);
		}
		init(size,this.file);
	}
	
	public RecentFiles( int size,
						File file)
	{
		init(size,file);
	}
	
	public void init(int size,File file)
	{
		if (file.exists())
		{
			this.recentFiles = deserialize(file);
			if (this.recentFiles == null)
			{
				this.recentFiles = new FixedSizeContainer<>(size);
			}
		}
		else
		{
			this.recentFiles = new FixedSizeContainer<>(size);
		}
	}
	
	@SuppressWarnings("unchecked")
	private FixedSizeContainer<File> deserialize(File fileToOpen)
	{
		FixedSizeContainer<File> container;
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(file);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         container  = (FixedSizeContainer<File>) in.readObject();
	         in.close();
	         fileIn.close();
	         return container;
	      }
	      catch(Exception e)
	      {
	    	  e.printStackTrace();
	         return null;
	      }
	}
	
	private boolean serializeFiles()
	{
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		try
		{
			fileOut = new FileOutputStream(file);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(recentFiles);
			out.close();
			return true;
		}
		catch (IOException e)
		{
			try
			{
				out.close();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			return false;
		}
	}
	

	public boolean addFile(File file)
	{
		boolean result = recentFiles.addElement(file);
		serializeFiles();
		System.out.println("Adding file: "+file.getAbsolutePath());
		return result;
	}
	
	public List<File> getRecentFiles()
	{
		return recentFiles.getElements();
	}
	
	public File getMostRecentFile()
	{
		return recentFiles.getMostRecentElement();
	}
	
	
	public static void syncRecents(JMenu mnu, RecentFileListener al, RecentFiles recentFiles)
	{
		try {mnu.removeAll();} catch(Exception e){}
		recentFiles.getRecentFiles().forEach(f ->
		{
			JMenuItem itm = new JMenuItem(f.getName());
			itm.addActionListener(a -> al.fileSelected(f));
			mnu.add(itm);
		});
		mnu.revalidate();
		mnu.repaint();
	}
	
	
	
	
}
