/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.io.IOException;

public class Mapper implements Runnable {

	private static String cmndArg;
	
	public static void main(String[] args) {
		cmndArg = "monroe-county.tab";
		
		if (args.length == 1) 
			cmndArg = args[0];
		
		EventQueue.invokeLater(new Mapper());

	}

	@Override
	public void run() {
		try {
			initUI();
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

	private void initUI() throws IOException, InterruptedException, ExecutionException {
		JFrame frame = new JFrame("Rochester Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println(cmndArg);
		frame.add(new MapPanel(cmndArg));
		frame.setResizable(false);
		frame.setSize(650, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
