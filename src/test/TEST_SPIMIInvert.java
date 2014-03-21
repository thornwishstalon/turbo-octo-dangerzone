package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedWriter;

import index.AbstractBlockedIndexCreator;
import index.SPMIInvert;

import org.junit.Test;

public class TEST_SPIMIInvert {

	@Test
	public void test() {
		
		//pretty useless automatic test. for manual testing only ;)
		AbstractBlockedIndexCreator index= new SPMIInvert();
		
		PipedWriter writer =new PipedWriter();
		
		try {
			writer.connect(index.getReader());
			index.start();
			
			writer.write("friends_1\n");
			writer.write("friends_1\n");
			writer.write("romans_2\n");
			writer.write("countrymen_3\n");
			writer.write("romans_3\n");
			writer.write("friends_2\n");
			
			//end "token-stream" -> writes index block to disk
			writer.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("exception occured");
		}
		
		
		
		
	}

}
