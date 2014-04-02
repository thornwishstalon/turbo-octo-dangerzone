package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import processor.BlockMerger;

public class TEST_BlockMerger {

	@Test
	public void test() {
		//fail("Not yet implemented");
		ArrayList<String> list = new ArrayList<String>();
		list.add("./dictionary/index_b0.txt");
		list.add("./dictionary/index_b1.txt");
		list.add("./dictionary/index_b2.txt");
		list.add("./dictionary/index_b3.txt");
		list.add("./dictionary/index_b4.txt");
		list.add("./dictionary/index_b5.txt");
		
		
		BlockMerger merger = new BlockMerger(list);
		merger.run();
	}

}
