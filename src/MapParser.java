import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class MapParser {

	private String fileName;

	private Collection<RoadIntersection> roadIntersections;
	private Collection<Road> roads;

	public MapParser(String file) {
		fileName = file;
		roadIntersections = new ArrayList<RoadIntersection>(19203);
		roads = new ArrayList<Road>(19824);
	}

	public Collection<RoadIntersection> getRoadIntersections() {
		return roadIntersections;
	}

	public Collection<Road> getRoads() {
		return roads;
	}

	
	public boolean parse() {
		Scanner sc = null;

		InputStream is;
		try {
			is = new FileInputStream(new File("src/"+fileName));
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

			}
			sc.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		
		}

	}

}
