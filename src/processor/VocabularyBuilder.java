package processor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VocabularyBuilder {

	public VocabularyBuilder(String text) {
		this.completeText = text;
	}
	
	private String completeText;
	private String stopWordsDocPath = "dictionary\\stop_words.txt";
	private String vocabularyPath = "dictionary\\vocabulary.txt";
	
	public void build() {
		
		//all lower case
		completeText = completeText.toLowerCase();
		
		//read stop words from file
		ArrayList<String> stopWords = getStopWords();
		
		//remove special characters
		/*
		Pattern pt = Pattern.compile("[^a-z]");
		Matcher match = pt.matcher(completeText);
		while (match.find()) {
			String s = match.group();
			s = s.replaceAll("\\"+s, "");
		}*/
		completeText = completeText.replaceAll("[^a-z]", " ");
		
		//split by space
		String[] tokens = completeText.split(" ");
		System.out.println("Number of tokens: " + tokens.length);
		
		//get unique values
		ArrayList<String> vocsFull = new ArrayList<>();
		for (String token : tokens) {
			if (!vocsFull.contains(token))
				vocsFull.add(token);
		}
		System.out.println("Number of vocs: " + vocsFull.size());
		
		//remove stop words
		ArrayList<String> vocs = new ArrayList<>();
		for (String singleVoc : vocsFull) {
			if (!stopWords.contains(singleVoc))
				vocs.add(singleVoc);
		}
		System.out.println("Number of vocs without stop words: " + vocs.size());
		
		//wite Vocabulary to file
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(vocabularyPath));
			for (String voc : vocs) {
				writer.write(voc);
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private ArrayList<String> getStopWords() {
		ArrayList<String> stopWords = new ArrayList<>();
		
		try {
			Path path = Paths.get(stopWordsDocPath);
			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("?"))
					stopWords.add(line);
			}
		}
		catch (Exception ex) {
			
		}
		
		return stopWords;
	}
}
