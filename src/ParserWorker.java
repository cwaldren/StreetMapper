import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		roadIntersections = new ArrayList<RoadIntersection>(19203);
		roads = new ArrayList<Road>(19824);
		progressNumber = 1;
	}

	public Collection<Road> getRoads() {
		return roads;
	}

	public int count() throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream("src/"
				+ fileName));
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

		try {
			is = new FileInputStream(new File("src/" + fileName));
			sc = new Scanner(is);
			String[] tokens;

			while (sc.hasNextLine()) {

				String line = sc.nextLine();
				tokens = line.split("\t");
				if (tokens[0].equals("i")) {
					roadIntersections.add(new RoadIntersection(tokens[1],
							tokens[2], tokens[3]));
				} else if (tokens[0].equals("r")) {
					roads.add(new Road(tokens[1], tokens[2], tokens[3]));
				}

				progressNumber++;
				setProgress((int) (((double) progressNumber / count) * 100));
				

			}

			sc.close();

			return new Map(roads, roadIntersections);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;

		}

	}

	

}
