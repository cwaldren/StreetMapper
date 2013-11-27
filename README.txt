Casey Waldren
cwaldren@u.rochester.edu
TAs Ciaran Downey & Yang Yu

***Street Mapper***

How To Use
    I've included the src folder which has all the class files, the monroe-county.tab file, and a png which is used for the background image. 

    If compiling from src doesn't work for some reason, I've included a jar file, StreetMapper.jar. I have ran it with java -jar StreetMapper.jar on Windows and Ubuntu using Java 7.  

    If you need to test with a different input file, just use java -jar StreetMapper.jar filename

    (or modify source code)

    The UI has buttons for showing the MST, drawing points, and drawing roads. The UI displays the current intersection that is closest to cursor. It also displays the distance from point a to b when they are selected. 

    There is a button to enter two IDs and get the path back. 

    To easily find a shortest path, just click somewhere and then click somewhere else. 


Report Documentation

    My code attempts to follow the MVC pattern. I chose this because originally I was not following any patterns, and the code for unrelated tasks was scattered everywhere. This was really annoying and probably bad practice.

    The code begins in the static void main in the Mapper class. It simply adds a new Mapper into the EventQueue. This triggers the run method which initializes the UI.

    The UI has a main frame called the MapPanel. MapPanel takes a filename in the constructor. This filename is default monroe-county.tab, but you can pass it an argument in the command line. 

    MapPanel creates the parser, the view, and the controller. The parser parses the file while the controller waits for the result. Once it has it, it manipulates it so that the view can display the roads. It passes data to the view and the view begins to render it.

    Operations like taking care of clicks and hovering are all handled in the view. 

    The algorithms used are Dijkstra's and Kruskal's. Both run in SwingWorkers so they don't freeze up the UI. 

    Finding a shortest path is initated when the user selects a point A and a point B on the map with the mouse. It is also initated when the 'enter intersections' button is clicked. This can be used to enter specific ids. There is a visual path as well as a text path generated (verts and roads to standard output).

    Special Features: You can show either the roads, or the intersections. You can enable and disable the MST. To see a cool MST just turn on road intersections and enable the MST. Also the UI doesn't hang!

Report Run Time Analysis
    
    The parser parses in O(n) and the controller puts everything into lists and hashmaps in O(n). 

    All rendering is in O(n).

    Kruskal's runs in O(|E| log |V|) which isn't bad. It should scale fine. 

    Dijkstra's is O(|E| log |V|). Same as Kruskal's. It cuts out early if it finds the source which might give it a few extra ms. 

    The getClosestPoint() method is needed to snap points to points so the user can select intersections. It runs a lot. It runs in O(n) because it's just running through every roadintersection and comparing the distance from it to the reference point. It could be improved by sorting the points by x or y coordinate or by distance from some arbitrary point..I think. As it is, it's still O(n).


Files

    --DisjointSet.java
        Class with method for unioning. Used for Kruskal's algorithm.

     --Node.java
        Used in the DisjointSet.

    --IntersectionPair.java
        Holds two RoadIntersections. This is useful because I need to be able to refer to a pair and get the two roads, and refer to two roads and get back the pair. Each Pair has a distance property for ordering.

    --Map.java
        Just used for organization. The Map has a collection of roads and intersections. 

    --MapController.java
        The controller executes the parser, and processes the data for the view.

    --MapGraph.java
        The graph is used for all the algorithms. It has a method for calculating the adjacencies of an intersection.

    --MapPanel.java
        The MapPanel is glue. It creates the parser, the view, and the controller, and passes the parser and view to the controller. 

    --Mapper.java
        This is the first stop in executing the program. It deals with the command line argument and sets up the JFrame. 

    --MapView.java
        MapView is the largest and most complicated file (probably not a good thing). It as methods for painting all the components, listening for mouse input, running Kruskal's, Dijkstras, printing paths to std output, and getting the closest point to cursor. 

    --ParserWorker.java
        ParserWorker reads the input data. It used to use string.split() but scanner deals with the '\t's more gracefully. It's about .3 ms slower.

    --Road.java
        The Road is like an IntersectionPair, except it holds string ids, not actual objects. 

    --RoadIntersection.java
        RoadIntersections hold x and y coordinates and an adjacency list of neighbors. 

    --monroe-county.tab
        Default input file.

    --roch.png
        Background image to make map pretty. 