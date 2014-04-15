package lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import main.input.settings.ApplicationSetup;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import reader.Reader;

public class LuceneTester {

	public LuceneTester() {
		
	}
		
	private ArrayList<File> documents = new ArrayList<>();
	
	public void test() {
		
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);

		IndexWriter w;
		try {
			w = new IndexWriter(index, config);
			readFiles();
			for (File f : documents) {
				
			}
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
}
