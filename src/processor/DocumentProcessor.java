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
			    while ((line = reader.readLine()) != null) {
			        if (!line.contains(":"))
			        	text += line;
			    }      
			    //System.out.println(text);
		    } catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		return text;
	}
}
