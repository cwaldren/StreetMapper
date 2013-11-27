/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/
import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("serial")
public class MapPanel extends JPanel {
	
	List<RoadIntersection> intersections;
	
	/* Map Panel is what creates my model, view, and controller */
	/* Well, it kind of resembles MVC */
	public MapPanel(String fileName) throws IOException, InterruptedException, ExecutionException {
		super(new BorderLayout());

		//Model
		ParserWorker parser = new ParserWorker(fileName);
		
		//View
		MapView view = new MapView();
		
		//Controllers
		MapController controller = new MapController(parser, view);
		controller.control();
		this.add(view, BorderLayout.CENTER);
	}

	
	
	public void setIntersections(Collection<RoadIntersection> rIntersections) {
		this.intersections = new ArrayList<RoadIntersection>(rIntersections);
		
	}



}
