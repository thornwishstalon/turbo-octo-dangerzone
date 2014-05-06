package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

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
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import bm25l.BM25LSimilarity;


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


			if(ApplicationSetup.getInstance().getUseBM25L()){
				searcher.setSimilarity(new BM25LSimilarity());
			}else if(ApplicationSetup.getInstance().getUseBM25()){
				searcher.setSimilarity(new BM25Similarity());
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
		
		HashMap<String,Integer> terms= new HashMap<String, Integer>();
		
		String queryText="docTest:";
		String operator= " OR "; 
		
		
		TokenStream tokenStream = analyzer.tokenStream("", text);
		//OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
		   
		    String term = charTermAttribute.toString();
		    if(!terms.containsKey(term)){
		    	terms.put(term,1);
		    }else{
		    	Integer i= terms.get(term) ;
		    	i= i+1;
		    	terms.remove(term);
		    	terms.put(term, i);
		    }
		}
		
		Integer termFreq;
		for(String key: terms.keySet()){
			termFreq= terms.get(key);
			if(termFreq <= 1 )
				queryText += "\""+key+"\""+operator;
			else
				queryText += "\""+key+"\"^"+termFreq+" "+operator;
		}
		
		queryText = queryText.substring(0, queryText.length() - operator.length());
		tokenStream.close(); 
		
		TopScoreDocCollector collector= TopScoreDocCollector.create(100, true);
		//System.out.println(queryText);
		Query q= queryParser.parse(queryText);

		// get 100 top documents
		 searcher.search(q, collector);
		
		
		printResult(collector,q);
		
	}

	private void printResult(TopScoreDocCollector collector,Query q) {
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		Document d;
		String docName;
		float score;
		ApplicationStatus status= ApplicationStatus.getInstance();
		
		status.clear();
				
		for(int i=0; i<Math.min(100,hits.length); i++){
			int docId = hits[i].doc;
		    try {
				d = searcher.doc(docId);
				docName= d.get("docID");
				score= hits[i].score;
				status.addScoreFromLucene(docName,score);
				if(i==0 || i==1){
					Explanation e= searcher.explain(q, docId);
					System.out.println("\n"+ e.toString());
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		status.printResults();
	}


	private String readTopic(String topic){
		String pathString=ApplicationSetup.getInstance().getTopicFilePath()+"/topic"+topic;
		pathString = pathString.trim();
		
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
