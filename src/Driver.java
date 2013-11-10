import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class Driver {
	static MapView map;
	
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				map = new MapView();
			
			}
		});
		
		SwingWorker<Integer, Void> parseWorker = new SwingWorker<Integer, Void>() {

			@Override
			protected Integer doInBackground() throws Exception {
			
				MapParser mp = new MapParser("monroe-county.tab");
				
				if(mp.parse())
					return mp.getTotalLines();
				else {
					return -1;
				}
			}
			protected void done() {
				    
				    int totalLines;
				    try {
				     totalLines = get();
				     if (totalLines > 0)
				    	 map.setStatus("Total lines parsed: "+totalLines);
				     else
				    	 map.setStatus("Couldn't parse file");
				   //  map.hideLoadingLabel();
				     
				    } catch (InterruptedException e) {
				     // This is thrown if the thread's interrupted.
				    } catch (ExecutionException e) {
				     // This is thrown if we throw an exception
				     // from doInBackground.
				    }
				   }
		};
		
		parseWorker.execute();
			
	}
}
