package main.input.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import main.ApplicationStatus;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/***
 * 
 * @author f
 * 
 */

public class ApplicationSetup {

	private final String CONFIG_FILE = "config.properties";

	private final String CONFIG_ID_CORPORA_PATH = "corpora_path";
	private final String CONFIG_ID_TOPIC_PATH = "topic_path";
	private final String CONFIG_ID_STOPWORDS_PATH = "stopwords_path";
	private final String CONFIG_ID_USE_STEMMER = "use_stemmer";
	private final String CONFIG_ID_USE_STOPWORDS = "use_stopwords";
	private final String CONFIG_ID_USE_BIGRAMS = "use_bigrams";
	private final String CONFIG_ID_DOCUMENTLOOKUPTABLE_PATH = "document_lookup_table_path";
	private final String CONFIG_ID_LUCENE_INDEX_PATH = "lucene_index_path";
	private final String CONFIG_ID_USE_LUCENE = "use_lucene";
	private final String CONFIG_ID_USE_BM25 = "use_bm25";

	// .... more to come

	private static ApplicationSetup instance = null;
	private static Logger logger = LogManager.getLogger("ApplicationSetup");

	private Properties props;

	private ApplicationSetup() {
		load();
	}

	public static ApplicationSetup getInstance() {
		if (instance == null)
			instance = new ApplicationSetup();
		return instance;
	}

	private void load() {

		props = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(CONFIG_FILE);
			props.load(in);

		} catch (FileNotFoundException e) {
			logger.error("properties file not found ");
		} catch (IOException e) {
			logger.error("IO Exception occured");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// nothing to do here
				}
			}

		}
	}

	private String getValue(String key) {
		if (props != null)
			return (String) props.get(key);
		else
			return "";
	}

	private void setValue(String key, String value) {
		if (props != null) {
			props.setProperty(key, value);
			store();
		}

	}

	private void store() {
		if (props != null) {
			OutputStream output = null;

			try {
				output = new FileOutputStream(CONFIG_FILE);
				props.store(output, null);
			} catch (IOException e) {
				logger.error("writing application property file failed");
			}

		}
	}

	public String getInfo() {
		String s = CONFIG_ID_CORPORA_PATH + ": " + getCorporaPath() + "\n"
				+ CONFIG_ID_STOPWORDS_PATH + ": " + getStopwordsPath() + "\n"
				+ CONFIG_ID_TOPIC_PATH + ": " + getTopicFilePath() + "\n";

		s += "\n" + CONFIG_ID_USE_STOPWORDS + " " + getUseStopwords()
				+ "\n" + CONFIG_ID_USE_STEMMER + " " + getUseStemmer() + "\n"
				+ CONFIG_ID_USE_BIGRAMS + " " + getUseBigrams() + "\n";
		
		s += CONFIG_ID_LUCENE_INDEX_PATH +": "+ getLuceneIndexPath()+"\n";
		s += CONFIG_ID_USE_LUCENE +": "+ getUseLucene()+"\n";
		s += CONFIG_ID_USE_BM25 +": "+ getUseBM25()+"\n";

		if(!ApplicationStatus.getInstance().indexIsSet()){
			s += "\nWARNING: index-file needs to be created first!";
			
		}else{
			s+= "\nindex-file is present!";
		}
		return s;
	}
	
	public String getLuceneIndexPath() {
		return getValue(CONFIG_ID_LUCENE_INDEX_PATH);
	}

	public void setLuceneIndexPath(String value) {
		setValue(CONFIG_ID_LUCENE_INDEX_PATH, value);
	}
	

	public String getTopicFilePath() {
		return getValue(CONFIG_ID_TOPIC_PATH);
	}

	public void setTopicFilePath(String value) {
		setValue(CONFIG_ID_TOPIC_PATH, value);
	}

	public String getCorporaPath() {
		return getValue(CONFIG_ID_CORPORA_PATH);
	}

	public void setCorporaPath(String value) {
		setValue(CONFIG_ID_CORPORA_PATH, value);
	}

	public String getDocumentLookupTablePath() {
		return getValue(CONFIG_ID_DOCUMENTLOOKUPTABLE_PATH);
	}

	public void setDocumentLookupTablePath(String value) {
		setValue(CONFIG_ID_DOCUMENTLOOKUPTABLE_PATH, value);
	}

	public String getStopwordsPath() {
		return getValue(CONFIG_ID_STOPWORDS_PATH);
	}

	public void setStopwordsPath(String value) {
		setValue(CONFIG_ID_STOPWORDS_PATH, value);
	}
	
	public boolean getUseLucene() {
		return Boolean.parseBoolean(getValue(CONFIG_ID_USE_LUCENE));
	}

	public void setUseLucene(boolean value) {
		setValue(CONFIG_ID_USE_LUCENE, Boolean.toString(value));
	}
	
	public boolean getUseBM25() {
		return Boolean.parseBoolean(getValue(CONFIG_ID_USE_BM25));
	}

	public void setUseBM25(boolean value) {
		setValue(CONFIG_ID_USE_BM25, Boolean.toString(value));
	}

	public boolean getUseBigrams() {
		return Boolean.parseBoolean(getValue(CONFIG_ID_USE_BIGRAMS));
	}

	public void setUseBigrams(boolean value) {
		setValue(CONFIG_ID_USE_BIGRAMS, Boolean.toString(value));
	}

	public boolean getUseStopwords() {
		return Boolean.parseBoolean(getValue(CONFIG_ID_USE_STOPWORDS));
	}

	public void setUseStopwords(boolean value) {
		setValue(CONFIG_ID_USE_STOPWORDS, Boolean.toString(value));
	}

	public boolean getUseStemmer() {
		return Boolean.parseBoolean(getValue(CONFIG_ID_USE_STEMMER));
	}

	public void setUseStemmer(boolean value) {
		setValue(CONFIG_ID_USE_STEMMER, Boolean.toString(value));
	}

}
