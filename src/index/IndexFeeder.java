package index;

import java.io.IOException;
import java.io.PipedWriter;

public class IndexFeeder implements Runnable  {
	//private AbstractBlockedIndexCreator index;
	private PipedWriter writer;

	public IndexFeeder(AbstractBlockedIndexCreator index)
	{
		//this.index= index;
		try {
			writer = new PipedWriter(index.getReader());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void feedIndex(String token)
	{
		if(writer==null)
			return;
		
		try {
			if(token!=null && token.length() > 0){
				writer.write(token);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			//
		}
	}



}
