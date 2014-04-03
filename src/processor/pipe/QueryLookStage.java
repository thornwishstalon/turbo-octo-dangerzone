package processor.pipe;

import index.entities.PostingList;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;

import main.ApplicationStatus;

public class QueryLookStage extends AbstractPipeStage {
	private ArrayList<String> queryTerms;
	private ArrayList<PostingList> lists;
	
	public QueryLookStage(PipedReader in, PipedWriter out) {
		super(in, out);
		queryTerms = new ArrayList<String>();
		lists = new ArrayList<PostingList>();
	}

	@Override
	public String process(String input) {
		//input is term we search for
		System.out.println("query_token: "+input);
		queryTerms.add(input);
		
		return "";
	}

	public void doSearch() {
		//TODO
		System.out.println("searching...");
		
		//retrieve posting lists
		PostingList list=null;
		for(String term: queryTerms){
			list= ApplicationStatus.getInstance().getPostingsFor(term);
			if(list!=null){
				lists.add(list);
			}
		}
		
		for(PostingList p:lists){
			System.out.println(p.toString()+"\n");
		}
		
	}

}
