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
	private final String CONFIG_ID_TOPIC_PATH = "topic_path";
	private final String CONFIG_ID_STOPWORDS_PATH = "stopwords_path";
	private final String CONFIG_ID_USE_STEMMER = "use_stemmer";
	private final String CONFIG_ID_USE_STOPWORDS = "use_stopwords";
	private final String CONFIG_ID_DOCUMENTLOOKUPTABLE_PATH = "document_lookup_table_path";
	
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
	
	private String getValue(String key)
	{
		if(props!=null)
			return (String) props.get(key);
		else return "";
	}
	
	private void setValue(String key, String value)
	{
		if(props!=null)
		{
			props.setProperty(CONFIG_ID_CORPORA_PATH, value);
			store();
		}
		
	}
	
	private void store() {
		if(props!=null){
			OutputStream output = null;
			
			try {
				output = new FileOutputStream(CONFIG_FILE);
				props.store(output, null);
			} catch (IOException e) {
				logger.error("writing application property file failed");
			}
			
		}
	}
	
	public String getInfo()	{
		String s= CONFIG_ID_CORPORA_PATH +": "+ getCorporaPath() +"\n";
		
		return s;
	}
	
	public String getTopicFilePath() {
		return getValue(CONFIG_ID_TOPIC_PATH);
	}
	
	public void setTopicFilePath(String value) {
		setValue(CONFIG_ID_TOPIC_PATH, value);
	}
	
	public String getCorporaPath()	{
		return getValue(CONFIG_ID_CORPORA_PATH);
	}
	
	public void setCorporaPath(String value)	{
		setValue(CONFIG_ID_CORPORA_PATH, value);
	}
	
	public String getDocumentLookupTablePath()
	{
		return getValue(CONFIG_ID_DOCUMENTLOOKUPTABLE_PATH);
	}
	
	public void setDocumentLookupTablePath(String value)
	{
		setValue(CONFIG_ID_DOCUMENTLOOKUPTABLE_PATH,value);
	}
	
	public String getStopwordsPath()
	{
		return getValue(CONFIG_ID_STOPWORDS_PATH);
	}
	
	public void setStopwordsPath(String value)
	{
		setValue(CONFIG_ID_STOPWORDS_PATH, value);
	}
	
	
	
}
