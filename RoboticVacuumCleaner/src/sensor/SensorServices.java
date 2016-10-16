package sensor;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class SensorServices {
	private static SensorServices sensorServices = new SensorServices();
	private SensorLoader sensorLoader;
	private SensorServices(){
		sensorLoader = new SensorLoader();
	}
	public static SensorServices getInstance(){
		return sensorServices;
	}
	public Obstacle getStartPosition(){
		return sensorLoader.getStartPosition();
	}
	public void generateObstacleFloor() throws ParserConfigurationException, SAXException, IOException{
		sensorLoader.ObstaclesFloorPlan();
	}
	public void generateTileFloor() throws ParserConfigurationException, SAXException, IOException{
		sensorLoader.tileFloorPlan();
	}
	public void generateSurfaceFloor() throws ParserConfigurationException, SAXException, IOException{
		sensorLoader.surfaceFloorPlan();
	}
}