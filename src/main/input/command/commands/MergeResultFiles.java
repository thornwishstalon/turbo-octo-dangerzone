package main.input.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class MergeResultFiles implements ICommand {

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		
		
		String pattern= ".*tfidf_result_.*";
		String path= ApplicationSetup.getInstance().getResultPath();
		
		File f= new File(path+"/res");
		if(!f.exists())
			f.mkdir();
		
		String[] files= getFiles(path, pattern);
		String text=get(files);
		write(text,"./queries/res/tfidf_results.txt");
		
		pattern=".*bm25l_result_.*";
		files= getFiles(path, pattern);
		text=get(files);
		write(text,"./queries/res/bm25l_results.txt");
		
		pattern=".*bm25_result_t.*";
		files= getFiles(path, pattern);
		text=get(files);
		write(text,"./queries/res/bm25_results.txt");
		
		pattern=".*defaultLucene_result_.*";
		files= getFiles(path, pattern);
		text=get(files);
		write(text,"./queries/res/defaultLucene_results.txt");
		
		
		return "!print merging results done";
	}
	
	private void write(String text,String filename){
		//System.out.println("write file");
		if(text== null){
			System.out.println("couldn't read text");
			return;
		}
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			String[] lines= text.split("\n");
			for(int i=0;i<lines.length;i++){
				writer.println(lines[i]);
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null){
				writer.close();
			}
		}
	
	}
	
	private String[] getFiles(String path,String pattern){
		File f= new File(path);
		String[] names=new String[20];
		int c=0;
		if(f.isDirectory()){
			String[] files=f.list();
			for(int i=0;i<files.length;i++){
				//System.out.println(files[i]);
				if(files[i].matches(pattern)){
					names[c++]= path+"/"+files[i];
				}
			}
			
			return names;
		}
		return null;
		
		
	}

	
	private String get(String[] files){
		BufferedReader br=null;
		String text="";
		for(int i=0; i<files.length;i++){
			
			try {
				br = new BufferedReader(new FileReader(files[i]));
				String line;
				while ((line = br.readLine()) != null) {
					text+= line+"\n";
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		return text;
	}
}
