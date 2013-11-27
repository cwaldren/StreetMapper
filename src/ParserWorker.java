/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingWorker;

public class ParserWorker extends SwingWorker<Map, Integer> {

	private String fileName;
	private int progressNumber;

	private List<RoadIntersection> roadIntersections;
	public Collection<Road> roads;

	public ParserWorker(String file) {
		fileName = file;
		roadIntersections = new ArrayList<RoadIntersection>();
		roads = new ArrayList<Road>();
		progressNumber = 0;
	}

	public Collection<Road> getRoads() {
		return roads;
	}

	//Very fast method to find the file line size so the progress bar has a reference
	public int count() throws IOException {
		InputStream is = new BufferedInputStream(ParserWorker.this.getClass().getResourceAsStream(fileName));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	@Override
	protected Map doInBackground() throws Exception {
		Scanner sc = null;
		int count = count();
		InputStream is;
		setProgress(0);

		is = ParserWorker.this.getClass().getResourceAsStream(fileName);
		//is = new FileInputStream(new File("src/" + fileName));
		sc = new Scanner(is);
		Scanner ls = new Scanner("");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			ls = new Scanner(line);
			String prefix = ls.next();
			if (prefix.equals("i")) {
				roadIntersections.add(new RoadIntersection(ls.next(), ls.next(), ls.next()));
			} else if (prefix.equals("r")) {
				roads.add(new Road(ls.next(), ls.next(), ls.next()));
			}

			progressNumber++;
			setProgress((int) (((double) progressNumber / count) * 100));
			

		}
		ls.close();
		sc.close();

		return new Map(roads, roadIntersections);
	}
}
