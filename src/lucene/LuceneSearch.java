package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.ApplicationStatus;
import main.input.settings.ApplicationSetup;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


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


	public void search(String topic) throws ParseException, IOException{
		String text= readTopic(topic);
		ApplicationStatus.getInstance().setTopic(topic);

		queryParser = new QueryParser(Version.LUCENE_47,"docText" , analyzer); //docTest is field name
		
		ArrayList<String> terms= new ArrayList<String>();
		String queryText="docTest:";
		String operator= " OR "; 
		
		TokenStream tokenStream = analyzer.tokenStream("", text);
		//OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
		   
		    String term = charTermAttribute.toString();
		    terms.add(term);
		    queryText += "\""+term+"\""+operator;
		    
		}
		queryText = queryText.substring(0, queryText.length() - operator.length());
		tokenStream.close(); 
		
		TopScoreDocCollector collector= TopScoreDocCollector.create(100, true);
		//System.out.println(queryText);
		Query q= queryParser.parse(queryText);

		// get 100 top documents
		 searcher.search(q, collector);
		
		printResult(collector);
		
		

	}

	private void printResult(TopScoreDocCollector collector) {
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		Document d;
		String docName;
		float score;
		ApplicationStatus status= ApplicationStatus.getInstance();
		
		status.clear();
				
		for(int i=0; i<100; i++){
			int docId = hits[i].doc;
		    try {
				d = searcher.doc(docId);
				docName= d.get("docID");
				score= hits[i].score;
				status.addScoreFromLucene(docName,score);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		status.printResults();
		
		
	}


	private String readTopic(String topic){
		String pathString=ApplicationSetup.getInstance().getTopicFilePath()+"/topic"+topic;
		String pattern= "^[\\w-]+:\\s.*"; //header

		File file = new File(pathString);

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
