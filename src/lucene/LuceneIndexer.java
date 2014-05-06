package lucene;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Pattern;

import main.input.settings.ApplicationSetup;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.*;

import bm25l.BM25LSimilarity;
import processor.DocParentFolderEnum;
import processor.DocumentProcessor;
import reader.Reader;

public class LuceneIndexer {

	public LuceneIndexer() {

	}

	private ArrayList<File> documents = new ArrayList<>();

	public void test() {

		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
		

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		config.setOpenMode(OpenMode.CREATE);
		
		if(ApplicationSetup.getInstance().getUseBM25L()){
			config.setSimilarity(new BM25LSimilarity());
		}else if(ApplicationSetup.getInstance().getUseBM25()){
			config.setSimilarity(new BM25Similarity());
		}
		
		IndexWriter w;
		String parentName,fileID,text;
		int id;
		Document doc;
		DocumentProcessor proc= new DocumentProcessor();
		float percent;
		int c=0;
		
		try {
			w = new IndexWriter(FSDirectory.open(new File(ApplicationSetup.getInstance().getLuceneIndexPath())), config);

			readFiles();
			for (File f : documents) {
				//defining file ID
				percent = round((c++ * 100.0f) / documents.size(),2);
				System.out.println(percent+"%\t\t|| processing file :" + f.getAbsolutePath());
				
				parentName = getParentFolderName(f.getAbsolutePath());
				id = getParentFolderValue(parentName);
				fileID = (id +" "+ f.getName()).trim();
				
				//mark indexable/storable etc stuff in document
				doc= new Document();
				//add !our! documentID
				doc.add(new StringField("docID", fileID,Field.Store.YES));
				//add file's text
				text= proc.readDocument(f);
				doc.add(new TextField("docText", text, Field.Store.NO));
				
				
				//add doc to index
				w.addDocument(doc);				
			}
			
			w.commit();			
			w.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readFiles() {
		Reader reader = new Reader(ApplicationSetup.getInstance().getCorporaPath());
		reader.readFiles(documents);
	}
	
	private float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private int getParentFolderValue(String parentName) {
		int value = 0;

		if (parentName != null) {
			// to match enum values
			parentName = parentName.replace("-", "_");
			parentName = parentName.replace(".", "_");
			parentName = parentName.toUpperCase();

			// take value form enum
			DocParentFolderEnum type = DocParentFolderEnum.ALT_ATHEISM;
			try {
				value = type.getValue(parentName);
			} catch (java.lang.Exception ex) {
				value = -1;
			}
		}

		return value;
	}

	private String getParentFolderName(String absolutePath) {
		String parentName = "";

		if (absolutePath != null) {
			// take care of OS specific path separator...
			String[] tokens = absolutePath.split(Pattern.quote(File.separator));
			parentName = tokens[tokens.length - 2];
		}

		return parentName;
	}

}
