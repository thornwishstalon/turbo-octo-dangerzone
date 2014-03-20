package index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import main.input.settings.ApplicationSetup;


public class DocumentLookUpTable {
	private ArrayList<String> lookup;
	
	public DocumentLookUpTable(){
		lookup = new ArrayList<>();
	}
	
	public void addDocument(String path)
	{
		lookup.add(path);
	}
	
	public void storeToDisk(){
		
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(ApplicationSetup.getInstance().getDocumentLookupTablePath());
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		for(int i= 0; i< lookup.size(); i++){
		
			writer.println(lookup.get(i));

		}
		if(writer != null)
			writer.close(); 
	}
	
	public void loadFromDisk(){
		BufferedReader reader=null;
		
		try {
			
			reader= new BufferedReader(new FileReader(ApplicationSetup.getInstance().getDocumentLookupTablePath()));
			String s;
			while( (s = reader.readLine()) !=null)
			{
				lookup.add(s);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
