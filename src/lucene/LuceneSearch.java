package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import main.input.settings.ApplicationSetup;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;


public class LuceneSearch {
	private Directory directory;
	private DirectoryReader reader;
	private IndexSearcher searcher;
	private Analyzer analyzer;
	private QueryParser queryParser;


	public LuceneSearch(){
		File dir= new File(ApplicationSetup.getInstance().getLuceneIndexPath());
		try {
			directory= FSDirectory.open(dir);
			reader = DirectoryReader.open(directory);
			searcher   = new IndexSearcher(reader);
			analyzer = new StandardAnalyzer(Version.LUCENE_47);

			
			if(ApplicationSetup.getInstance().getUseBM25()){
				//TODO
				//searcher.setSimilarity(new BM25Similarity....);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void search(String topic) throws ParseException{
		String text= readTopic(topic);
		
		queryParser = new QueryParser(Version.LUCENE_47,"docText" , analyzer); //docTest is field name
		
		queryParser.parse(text);
		
		//System.out.println(q.toString());

	}

	private String readTopic(String topic){
		String pathString=ApplicationSetup.getInstance().getTopicFilePath()+"/topic"+topic;
		String pattern= "^[\\w-]+:\\s.*"; //header
		
		File file = new File(pathString);
		//System.out.println(path);
		//=path.toFile(); 
		String line,searchString="";
		BufferedReader br=null;
		try{

			//reading file and passing tokens to next pipe stages
			br = new BufferedReader(new FileReader(file));

			
			while ((line = br.readLine()) != null) {
				if(line.length() > 0){
					if(!line.matches(pattern))
						searchString += line;
			}
			}
			System.out.println(searchString);



		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return searchString;
	}
}
