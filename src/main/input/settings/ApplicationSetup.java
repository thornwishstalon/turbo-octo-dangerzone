package main.input.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/***
 * 
 * @author f
 *
 */

public class ApplicationSetup {
	
	private final String CONFIG_FILE="config.properties";
	private final String CONFIG_ID_CORPORA_PATH = "corpora_path";
	private final String CONFIG_ID_USE_STEMMER = "stemmer";
	//.... more to come
	
	private static ApplicationSetup instance=null;
	private static Logger logger = LogManager.getLogger("ApplicationSetup");
	
	private Properties props;
	
	
	
	
	private ApplicationSetup()
	{
		load();
	}
	

	public static ApplicationSetup getInstance()
	{
		if(instance== null)
			instance= new ApplicationSetup();
		return instance;
	}
	
	private void load() {
		
		props = new Properties();
		InputStream in= null;
		try {
			in= new FileInputStream(CONFIG_FILE);
			props.load(in);
			
		} catch ( FileNotFoundException e) {
			logger.error("properties file not found ");
		} catch(IOException e)
		{
			logger.error("IO Exception occured");
			
		}finally
		{
			if(in!=null)
			{
				try {
					in.close();
				} catch (IOException e) {
					//nothing to do here
				}
			}
			
		}
	}
	
	public void store()
	{
		if(props!=null)
		{
			OutputStream output = null;
			
			try {
				output = new FileOutputStream(CONFIG_FILE);
				props.store(output, null);
			} catch (IOException e) {
				logger.error("writing application property file failed");
			}
			
		}
	}
	
	public String getInfo()
	{
		String s= CONFIG_ID_CORPORA_PATH +": "+ getCorporaPath() +"\n";
		
		return s;
	}
	
	public String getCorporaPath()
	{
		if(props!=null)
			return (String) props.get(CONFIG_ID_CORPORA_PATH);
		else return "";
	}
	
	public void setCorporaPath(String value)
	{
		if(props!=null)
		{
			props.setProperty(CONFIG_ID_CORPORA_PATH, value);
			store();
		}
		
	}
	
}
