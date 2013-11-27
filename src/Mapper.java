import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.io.IOException;

public class Mapper implements Runnable {

	public static void main(String[] args) {

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
		frame.add(new MapPanel("monroe-county.tab"));
		frame.setResizable(false);
		frame.setSize(650, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
