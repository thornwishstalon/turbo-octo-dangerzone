package processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentProcessor {
	
	public DocumentProcessor() {
		
	}
	
	public String readDocument(File file) {
		
		String text = "";
		
		if (file != null) {
			Path path = Paths.get(file.getAbsolutePath());
			try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				String line = null;
				boolean firstBlankLineFound = false;
			    while ((line = reader.readLine()) != null && !firstBlankLineFound) {
			        //read until the first empty line
	    			if (line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))
	    				firstBlankLineFound = true;
			    }
			    while ((line = reader.readLine()) != null) {
			    	// read important text - ignoring lines starting with '<' and empty lines
			    	// until eof or ---
			    	if (!line.startsWith(">") && !line.startsWith("-") && 
		    			!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")))
			    		text += line;
			    }
			    
			    System.out.println(text);
		    } catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		return text;
	}
}
