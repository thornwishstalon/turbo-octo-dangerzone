package index;

import java.io.IOException;
import java.io.PipedWriter;

public class IndexFeeder extends Thread{
	//private AbstractBlockedIndexCreator index;
	private PipedWriter writer;

	public IndexFeeder(AbstractBlockedIndexCreator index)
	{
		//this.index= index;
		try {
			writer = new PipedWriter();
			writer.connect(index.getReader());
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void feedIndex(String token)
	{
		
		try {
			if(token!=null && token.length() > 0){
				writer.write(token, 0, token.length());
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close()
	{
		if(writer!= null){
			try {
				writer.close();
				writer= null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(writer!=null){
			
			//do stuff
		}
	}



}
