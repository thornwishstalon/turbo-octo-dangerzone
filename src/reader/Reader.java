package reader;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

	public Reader(String newsgroup_path) {
		this.newsgroup_path = newsgroup_path;
	}

	private String newsgroup_path;

	public void readFiles(ArrayList<File> allFiles) {
		try {
			File directory = new File(this.newsgroup_path);
			listFilesForFolder(directory, allFiles);
			System.out.println("Total number of files: " + allFiles.size());
		} catch (Exception ex) {
			System.out.println("Folder doesn't exist!");
		}

	}

	public void listFilesForFolder(File folder, ArrayList<File> allFiles) {

		for (File entry : folder.listFiles()) {
			if (entry.isDirectory()) {
				listFilesForFolder(entry, allFiles);
			} else if (entry.isFile()) {
				allFiles.add(entry);
			}
		}
	}
}
