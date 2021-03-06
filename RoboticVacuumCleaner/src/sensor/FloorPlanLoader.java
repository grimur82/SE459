package sensor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import floor.DoorStatus;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import controllers.UtilityController;
import floor.Coordinate;
import floor.Cell;
import floor.SurfaceType;
import util.Debugger;

/**
 * Preliminary sensor loading prep: retrieve and load the floor plan.
 */
public class FloorPlanLoader {

	private HashMap<Coordinate, DoorStatus> doors = new HashMap<>();
	private Cell[][] floorPlan = null;

	private Coordinate startPos = new Coordinate();

	public void askUserForFile(){
        try {
            File newFile = UtilityController.getServices().askUserFloorPlan();
            loadFloorPlan(newFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Debugger.log("Error loading floor plan");
            e.printStackTrace();
            System.exit(-1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
	
	Cell[][] getFloorPlan() {
        return floorPlan;
    }
    
    private void loadFloorPlan(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document parsing = db.parse(file);
        parsing.getDocumentElement().normalize();

        setDim(parsing);
        setStart(parsing);
        setDoors(parsing);
        setCells(parsing);
    }

    private void setDim(Document doc) {
        NodeList start = doc.getElementsByTagName("dimensions");
        Element s = (Element) start.item(0);

        int x = Integer.parseInt(s.getAttribute("x"));
        int y = Integer.parseInt(s.getAttribute("y"));

        floorPlan = new Cell[x + 1][y + 1];
    }

    private void setStart(Document doc) {
        NodeList start = doc.getElementsByTagName("start");
        Element s = (Element) start.item(0);

        startPos.setX(Integer.parseInt(s.getAttribute("x")));
        startPos.setY(Integer.parseInt(s.getAttribute("y")));
    }

    private void setDoors(Document doc) {
        NodeList d = doc.getElementsByTagName("doors");
        Element doorList = (Element) d.item(0);
        NodeList eachDoor = doorList.getElementsByTagName("door");

        for (int i = 0; i < eachDoor.getLength(); ++i) {
            Element door = (Element) eachDoor.item(i);

            Double x = Double.valueOf(door.getAttribute("x"));
            Double y = Double.valueOf(door.getAttribute("y"));
			DoorStatus status = DoorStatus.valueOf(door.getAttribute("status"));

            Coordinate coordinate = new Coordinate(x.doubleValue(), y.doubleValue());
            doors.put(coordinate, status);
        }
    }

	HashMap<Coordinate, DoorStatus> getDoorList() {
    	return doors;
    }

    private void setCells(Document doc) {
        NodeList cells = doc.getElementsByTagName("cells");
        Element cellList = (Element) cells.item(0);
        NodeList eachCell = cellList.getElementsByTagName("cell");

        for (int i = 0; i < eachCell.getLength(); ++i) {
            Element cell = (Element) eachCell.item(i);

            // Extract data
            int x = Integer.parseInt(cell.getElementsByTagName("x").item(0).getTextContent());
            int y = Integer.parseInt(cell.getElementsByTagName("y").item(0).getTextContent());
            String surface = cell.getElementsByTagName("surface").item(0).getTextContent();
            String name = cell.getAttribute("name");

            // Create cell
            Cell c = new Cell(SurfaceType.valueOf(surface));

            // Set name
            c.setName(name);
            c.setCoordinate(x, y);
            // Set obstacles
            NodeList obs = cell.getElementsByTagName("obstacles");
            if (obs.getLength() > 0) {
                Node obList = obs.item(0);
                Element obsList = (Element) obList;
                NodeList eachOb = obsList.getElementsByTagName("obstacle");
                for (int j = 0; j < eachOb.getLength(); ++j) {
                    String obstacle = (eachOb.item(j)).getTextContent();
                    c.setObstacle(obstacle);
                }
            }

			// Set flags
			NodeList flags = cell.getElementsByTagName("flags");
			if (flags.getLength() > 0) {

				Node flagList = flags.item(0);
				Element flatsList = (Element) flagList;
				NodeList eachFlag = flatsList.getElementsByTagName("flag");

				for (int j = 0; j < eachFlag.getLength(); ++j) {
					String content = (eachFlag.item(j)).getTextContent();

					if (content.equals("STAIRS"))
						c.setStairs(true);
					if (content.equals("CHARGINGSTATION")){
						c.setChargingBase();
					}
				}
			}

			// Record cell
            floorPlan[x][y] = c;
        }
    }

	Coordinate getStartPosition() {
        return startPos;
    }
}