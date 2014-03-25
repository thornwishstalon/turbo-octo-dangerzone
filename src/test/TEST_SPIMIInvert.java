package test;

import static org.junit.Assert.*;


import index.AbstractBlockedIndexCreator;
import index.IndexFeeder;
import index.SPMIInvert;

import org.junit.Test;

public class TEST_SPIMIInvert {

	@Test
	public void test() {

		//pretty useless automatic test. for manual testing only ;)
		AbstractBlockedIndexCreator index= new SPMIInvert();
		IndexFeeder feeder= new IndexFeeder(index);
		
		index.start();
		feeder.start();
		
		System.out.println("feeding index");
		
		feeder.feedIndex("friends_1\n");
		feeder.feedIndex("friends_1\n");
		feeder.feedIndex("romans_2\n");
		feeder.feedIndex("countrymen_3\n");
		feeder.feedIndex("romans_3\n");
		feeder.feedIndex("friends_2\n");

		//end "token-stream" -> writes index block to disk
		feeder.close();
		
		
		
		

	}

}
