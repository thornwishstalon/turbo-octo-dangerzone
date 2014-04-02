package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import processor.pipe.CaseFolding;
import processor.pipe.Indexing;
import processor.pipe.Stemming;
import processor.pipe.StopWordRemoval;

public class TEST_PipedProcessing {

	@Test
	public void test() {
		// fail("Not yet implemented");

		try {

			PipedWriter writer = new PipedWriter();

			try {
				CaseFolding folding = new CaseFolding(new PipedReader(writer),
						new PipedWriter());
				StopWordRemoval stopwords = new StopWordRemoval(
						new PipedReader(folding.getOut()), new PipedWriter());
				Stemming stemming = new Stemming(new PipedReader(
						stopwords.getOut()), new PipedWriter());
				Indexing indexing = new Indexing(new PipedReader(
						stemming.getOut()), new PipedWriter());

				folding.start();
				stopwords.start();
				stemming.start();

				indexing.setCurrentDocID("1");
				indexing.start();

				System.out.println("STARTING");

				writer.write("TEST\n");
				writer.write("PONY\n");
				writer.write("PONIES\n");
				writer.write("as\n");
				writer.write("I\n");
				writer.write("am\n");

				writer.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Thread.sleep(2000);

			try {
				writer.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
