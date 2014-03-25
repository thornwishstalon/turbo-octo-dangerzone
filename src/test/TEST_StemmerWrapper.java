package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import stemming.SnowballStemmerWrapper;

public class TEST_StemmerWrapper {

	@Test
	public void test() {
		//fail("Not yet implemented");


		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader("testData/stemmerTest.txt"));
			OutputStream out = new FileOutputStream("testData/stemmerTestResult.txt");

			SnowballStemmerWrapper stemmer= new SnowballStemmerWrapper(reader, out);
			
			while(stemmer.isAlive()){
				// wait
			}

			assertTrue(checkFiles());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	private boolean checkFiles()
	{
		boolean check=true;
		BufferedReader reader=null;
		BufferedReader refReader=null;
		try {
			reader= new BufferedReader(new FileReader("testData/stemmerTestResult.txt"));
			refReader= new BufferedReader(new FileReader("testData/stemmerTestReference.txt"));
			String input= null;
			String reference= null;
			while((input= reader.readLine())!=null)
			{
				input= input.trim();
				reference= refReader.readLine().trim();
				if(!input.equals(reference))
				{
					check= false;
					break;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			check= false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			check= false;
		}
		finally
		{
			try {
				reader.close();
				refReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return check;
	}

}
