package processor;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import processor.TextDocument;

public class DocumentProcessor {

	public DocumentProcessor() {

	}
	
	public String process(ArrayList<File> documents,
			List<TextDocument> documentList) {
		String completeText = "";

		if (documents != null) {
			for (File file : documents) {

				// read File, get Content and File Data
				String fileText = readDocument(file);

				if (!fileText.equals("") && !fileText.isEmpty()) {
					String absolutePath = file.getAbsolutePath();
					String parentName = getParentFolderName(absolutePath);
					int value = getParentFolderValue(parentName);

					// id will be parentFolderNameValue + fileName
					String id = value + file.getName();

					// create Document
					TextDocument doc = new TextDocument(id, fileText,parentName);
					documentList.add(doc);

					// added by f:

					// store text
					completeText += fileText;
				}
			}
		}

		return completeText;
	}

	public String readDocument(File file) {

		String text = "";

		if (file != null) {
			Path path = Paths.get(file.getAbsolutePath());
			try (BufferedReader reader = Files.newBufferedReader(path,
					Charset.defaultCharset())) {
				String line = null;
				boolean firstBlankLineFound = false;
				while ((line = reader.readLine()) != null
						&& !firstBlankLineFound) {
					// read until the first empty line
					if (line.isEmpty() || line.trim().equals("")
							|| line.trim().equals("\n"))
						firstBlankLineFound = true;
				}
				while ((line = reader.readLine()) != null) {
					// read important text - ignoring lines starting with '<'
					// and empty lines
					// until eof or ---
					if (!line.startsWith(">")
							&& !line.startsWith("-")
							&& !(line.isEmpty() || line.trim().equals("") || line
									.trim().equals("\n")))
						text = text + line + " ";
				}

				// System.out.println(text);
			} catch (java.lang.Exception ex) {
				// TODO do something about Encoding
				// System.out.println("File name: " + path);
			}
		}

		return text;
	}

	private String getParentFolderName(String absolutePath) {
		String parentName = "";

		if (absolutePath != null) {
			// take care of OS specific path separator...
			String[] tokens = absolutePath.split(Pattern.quote(File.separator));
			parentName = tokens[tokens.length - 2];
		}

		return parentName;
	}

	private int getParentFolderValue(String parentName) {
		int value = 0;

		if (parentName != null) {
			// to match enum values
			parentName = parentName.replace("-", "_");
			parentName = parentName.replace(".", "_");
			parentName = parentName.toUpperCase();

			// take value form enum
			DocParentFolderEnum type = DocParentFolderEnum.ALT_ATHEISM;
			try {
				value = type.getValue(parentName);
			} catch (java.lang.Exception ex) {
				value = -1;
			}
		}

		return value;
	}
}
