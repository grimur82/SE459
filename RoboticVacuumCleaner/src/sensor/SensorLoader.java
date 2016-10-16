package sensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SensorLoader {
	private Obstacle[][] obstacleFloorPlan;
	private Tiling[][] tileFloorPlan;
	private Surface[][] surfaceFloorPlan;
	//private XMLReader readFilePlan;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Tiling> tiles;
	private ArrayList<Surface> surfaces;
	public SensorLoader(){
		obstacles = new ArrayList<Obstacle>();
		tiles = new ArrayList<Tiling>();
		surfaces = new ArrayList<Surface>();
		obstacleFloorPlan = new Obstacle[100][100];
		tileFloorPlan = new Tiling[100][100];
		surfaceFloorPlan = new Surface[100][100];
	}
	public void surfaceFloorPlan() throws ParserConfigurationException, SAXException, IOException{
		File surfaceFile = new File("SurfaceTypePlan.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if (!surfaceFile.exists()) {
            System.err.println("**** XML File '" + surfaceFile + "' cannot be found");
            System.exit(-1);
        }
        Document parsing = db.parse(surfaceFile);
        parsing.getDocumentElement().normalize();
        NodeList entry = parsing.getDocumentElement().getChildNodes();
        for(int i=0; i < entry.getLength();i++){
       	 if (entry.item(i).getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            String entryName = entry.item(i).getNodeName();
            Element elem = (Element) entry.item(i);
            NodeList getChildNode = elem.getElementsByTagName("x");
            for(int j =0; j < getChildNode.getLength();j++){
           	 	String x = elem.getElementsByTagName("x").item(j).getTextContent().trim();
                String y = elem.getElementsByTagName("y").item(j).getTextContent().trim();
                String type = elem.getElementsByTagName("type").item(j).getTextContent().trim();
                surfaces.add(SurfaceFactory.getSurface(type, Integer.parseInt(x), 
                Integer.parseInt(y)));
                surfaceFloorPlan[Integer.parseInt(x)][Integer.parseInt(y)] = 
                SurfaceFactory.getSurface(type, Integer.parseInt(x), Integer.parseInt(y));
                System.out.println(type);
            } 
        }
	}
	public void tileFloorPlan() throws ParserConfigurationException, SAXException, IOException{
		File tileFile = new File("TilesFloorPlan.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if (!tileFile.exists()) {
            System.err.println("**** XML File '" + tileFile + "' cannot be found");
            System.exit(-1);
        }
        Document parsing = db.parse(tileFile);
        parsing.getDocumentElement().normalize();
        NodeList entry = parsing.getDocumentElement().getChildNodes();
        for(int i=0; i < entry.getLength();i++){
       	 if (entry.item(i).getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            String entryName = entry.item(i).getNodeName();
            Element elem = (Element) entry.item(i);
            NodeList getChildNode = elem.getElementsByTagName("fromx");
            for(int j =0; j < getChildNode.getLength();j++){
           	 	String fromX = elem.getElementsByTagName("fromx").item(j).getTextContent().trim();
                String fromY = elem.getElementsByTagName("fromy").item(j).getTextContent().trim();
                String toX = elem.getElementsByTagName("tox").item(j).getTextContent().trim();
                String toY = elem.getElementsByTagName("toy").item(j).getTextContent().trim();
                tiles.add(TilingFactory.getTiling(entryName, Integer.parseInt(fromX), 
                Integer.parseInt(fromY),Integer.parseInt(toX),Integer.parseInt(toY) ));
                tileFloorPlan[Integer.parseInt(fromX)][Integer.parseInt(fromY)] = 
                TilingFactory.getTiling(entryName, Integer.parseInt(toX), Integer.parseInt(toY),
                		Integer.parseInt(toX),Integer.parseInt(toY));
            } 
        }
	}
	public void ObstaclesFloorPlan() throws ParserConfigurationException, SAXException, IOException{
		 File obstacleFile = new File("ObstaclesFloorPlan.xml");
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();
         if (!obstacleFile.exists()) {
             System.err.println("**** XML File '" + obstacleFile + "' cannot be found");
             System.exit(-1);
         }
         Document parsing = db.parse(obstacleFile);
         parsing.getDocumentElement().normalize();
         NodeList entry = parsing.getDocumentElement().getChildNodes();
         for(int i=0; i < entry.getLength();i++){
        	 if (entry.item(i).getNodeType() == Node.TEXT_NODE) {
                 continue;
             }
             String entryName = entry.item(i).getNodeName();
             Element elem = (Element) entry.item(i);
             NodeList getChildNode = elem.getElementsByTagName("x");
             for(int j =0; j < getChildNode.getLength();j++){
            	 String x = elem.getElementsByTagName("x").item(j).getTextContent().trim();
                 String y = elem.getElementsByTagName("y").item(j).getTextContent().trim();
                 obstacles.add(ObstacleFactory.getObstacle(entryName, Integer.parseInt(x), 
                 Integer.parseInt(y)));
                 obstacleFloorPlan[Integer.parseInt(x)][Integer.parseInt(y)] = 
                 ObstacleFactory.getObstacle(entryName, Integer.parseInt(x), Integer.parseInt(y));
                 System.out.println(entryName + " X: " + x + " Y: "+ y);
             } 
         }
	}
	public Obstacle checkObstacle(int x, int y){
		return obstacleFloorPlan[x][y];
	}
	public Tiling checkTile(int x, int y){
		return tileFloorPlan[x][y];
	}
	public Surface checkSurface(int x, int y){
		return surfaceFloorPlan[x][y];
	}
	public Obstacle getStartPosition(){
		for(Obstacle o : obstacles){
			if(o.getType().equals("chargingstation")){
				return o;
			}
		}
		return null;
	}
}