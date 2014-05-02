package main.input.command.commands;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import main.ApplicationStatus;
import main.input.command.ICommand;
import main.input.parser.CommandParser;
import main.input.settings.ApplicationSetup;

public class TestScript implements ICommand, Observer {
	private ArrayList<String> stack;
	private int stackCounter=0;
	private CommandParser parser;
	
	public TestScript(CommandParser parser){
		this.parser= parser;
		
		
		
	}
	
	
	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		
		ApplicationStatus.getInstance().setiScriptRunning(true);
		
		initCommands();
		
		String command;
		
		//bad solution.. but might work for now
		while(stackCounter <= stack.size()){
			if(stack.size()==0)
				break;
			if(stackCounter >= stack.size())
				break;
			
			command= stack.get(stackCounter);
			
			if(!command.isEmpty()){
				stack.set(stackCounter, "");
				System.out.println("executing: "+command);
				
				parser.parse(command);
			}
		}
		
		ApplicationStatus.getInstance().setiScriptRunning(false);
		ApplicationStatus.getInstance().notifyObservers();
		return "!print script done";
	}

	//callback
	public void increaseCounter(){
		System.out.println("inc counter");
		stackCounter++;
	}


	@Override
	public void update(Observable o, Object arg) {
		//if(((Event) arg).id == 123) //TODO add identifier??
			increaseCounter();
		
	}
	
	private void initCommands(){
		stack = new ArrayList<String>();
		ApplicationSetup setup= ApplicationSetup.getInstance();
		ApplicationStatus.getInstance().addObserver(this);
		
		if(setup.getUseLucene()){
			stack.add("!enableLucene");
			// disables lucene
		}
		if(setup.getUseBigrams()){
			stack.add("!bigram");
		}
		if(!setup.getUseStemmer()){
			stack.add("!stemmer");
		}
		if(!setup.getUseStopwords()){
			stack.add("!stopwords");
		}
		
		stack.add("!buildVoc");
		
		for(int i=1; i<=20; i++){
			stack.add("search "+i);
		}
		
		stack.add("!enableLucene");
		if(setup.getUseBM25()){
			stack.add("!enableBM25"); //-> disable bm25 similarity
		}
		stack.add("luceneBuildVoc");
		for(int i=1; i<=20; i++){
			stack.add("search "+i);
		}
		
		stack.add("!enableBM25l"); //enable bm25l similarity
		
		//TODO find out if index is still valid
		// stack.add("!luceneBuildVoc");
		for(int i=1; i<=20; i++){
			stack.add("search "+i);
		}
		
	}
}
