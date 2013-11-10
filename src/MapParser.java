import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class MapParser {

	String fileName;
	int totalLines;
	Collection<RoadIntersection> roadIntersections;
	Collection<Road> roads;
	
	public MapParser(String file) {
		fileName = file;
		roadIntersections = new ArrayList<RoadIntersection>(19203);
		roads = new ArrayList<Road>(19824);
		totalLines = 0;
	}

	
	public boolean parse() {
		Scanner sc = null;

			InputStream is = getClass().getResourceAsStream(fileName);
			if (is != null) {
				sc = new Scanner(is);
				String[] tokens;
			
				while (sc.hasNextLine()) {
					totalLines++;
					String line = sc.nextLine();
					tokens = line.split(" ");
					if (tokens[0].equals("i")) {
						roadIntersections.add(new RoadIntersection(tokens[1], tokens[2], tokens[3]));
					} 
					else if (tokens[0].equals("r")) {
						roads.add(new Road(tokens[1], tokens[2], tokens[3]));
					}
				}
	
				sc.close();
				return true;
			} else {
				return false;
			}
		
	}


	public int getTotalLines() {
		return totalLines;
	}
}
