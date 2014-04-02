package processor;

import java.util.ArrayList;
import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.lang.Exception;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.Math;

import processor.pipe.CaseFolding;
import processor.pipe.Indexing;
import processor.pipe.Stemming;
import processor.pipe.StopWordRemoval;
import main.input.settings.ApplicationSetup;
import reader.Reader;
import stemming.SnowballStemmerWrapper;

public class VocabularyBuilder {

	public VocabularyBuilder(String text) {
		this.completeText = text;
	}

	private String completeText;
	private String stopWordsDocPath = "dictionary\\stop_words.txt";
	private String vocabularyPath = "dictionary\\vocabulary.txt";

	public void build() {

		long start = System.currentTimeMillis();

		// all lower case
		completeText = completeText.toLowerCase();

		// read stop words from file
		ArrayList<String> stopWords = getStopWords();

		// stemming
		SnowballStemmerWrapper stemmerWrapper = new SnowballStemmerWrapper(
				completeText);
		completeText = stemmerWrapper.runStringInput();

		// split by space
		String[] tokens = completeText.split(" ");
		System.out.println("Number of tokens: " + tokens.length);

		// remove stop words
		ArrayList<String> vocsAll = new ArrayList<>();
		int step = 10000;
		int count = 0;
		for (int i = count * step; i < Math.min((count + 1) * step,
				tokens.length); i++) {
			String token = tokens[i];
			boolean isStopWord = false;
			for (String stopWord : stopWords) {
				if (token.equals(stopWord)) {
					isStopWord = true;
					break;
				}
			}
			if (!isStopWord) {
				// remove special characters
				// token = token.replaceAll("[^a-z]", "");
				token = token.replaceAll("[\\W]", "");
				token = token.trim();
				vocsAll.add(token);
			}
			count++;
		}
		System.out.println("Number of total vocs: " + vocsAll.size());

		// get unique values
		ArrayList<String> vocs = new ArrayList<>();
		for (String voc : vocsAll) {
			if (!vocs.contains(voc))
				vocs.add(voc);
		}
		System.out.println("Number of vocs: " + vocs.size());

		long finished = System.currentTimeMillis();

		System.out.println("in " + (finished - start) / 1000 + "seconds");

		// write Vocabulary to file
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					vocabularyPath));
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

	public void buildWithPipe() {
		long start = System.currentTimeMillis();
		ArrayList<File> documents = new ArrayList<>();
		String path = ApplicationSetup.getInstance().getCorporaPath();
		System.out.println("Reading directory: " + path);
		Reader reader = new Reader(path);

		// stores all files in the arrayList
		reader.readFiles(documents);

		PipedWriter inputFileWriter = null;

		try {
			System.out.println("STARTING");

			inputFileWriter = new PipedWriter();

			CaseFolding folding = new CaseFolding(new PipedReader(
					inputFileWriter), new PipedWriter());
			StopWordRemoval stopwords = new StopWordRemoval(new PipedReader(
					folding.getOut()), new PipedWriter());
			Stemming stemming = new Stemming(
					new PipedReader(stopwords.getOut()), new PipedWriter());
			Indexing indexing = new Indexing(
					new PipedReader(stemming.getOut()), new PipedWriter());

			folding.start();
			stopwords.start();
			stemming.start();
			indexing.start();

			BufferedReader br;
			String line;
			String[] tokens;
			int id = 0; // for testing
			for (File f : documents) {
				// System.out.println(f.getAbsolutePath());
				indexing.setCurrentDocID("" + id++);
				br = new BufferedReader(new FileReader(f));
				while ((line = br.readLine()) != null) {
					tokens = line.split("\\s");
					for (int i = 0; i < tokens.length; i++) {
						inputFileWriter.write(tokens[i] + "\n");
						inputFileWriter.flush();
					}
				}

			}

			indexing.backup();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (inputFileWriter != null)
					inputFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long end = System.currentTimeMillis();
			System.out.println("DONE");
			System.out.println((end - start) / 1000 + " seconds");

		}

	}

	private ArrayList<String> getStopWords() {
		ArrayList<String> stopWords = new ArrayList<>();

		try {
			Path path = Paths.get(stopWordsDocPath);
			BufferedReader reader = Files.newBufferedReader(path,
					StandardCharsets.UTF_8);
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("?"))
					stopWords.add(line);
			}
		} catch (Exception ex) {

		}

		return stopWords;
	}
}
