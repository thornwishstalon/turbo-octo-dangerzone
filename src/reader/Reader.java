package reader;

import java.lang.Exception;
import java.util.ArrayList;
import java.io.File;


public class Reader {
	private int size=0;
	
	
	public Reader(String newsgroup_path) {
		this.newsgroup_path = newsgroup_path;
	}

	private String newsgroup_path;

	public void readFiles(ArrayList<File> allFiles) {
		try {
			File directory = new File(this.newsgroup_path);
			listFilesForFolder(directory, allFiles);
			System.out.println("Total number of files: " + allFiles.size());
			size= allFiles.size();
					
			
		} catch (Exception ex) {
			System.out.println("Folder doesn't exist!");
		}

	}
	
	public int getSize(){
		return size;
	}

	public void listFilesForFolder(File folder, ArrayList<File> allFiles) {

		for (File entry : folder.listFiles()) {
			if (entry.isDirectory()) {
				listFilesForFolder(entry, allFiles);
			} else if (entry.isFile()) {
				if(!entry.getName().startsWith(".")) //ignore hidden files
					allFiles.add(entry);
			}
		}
	}
}
