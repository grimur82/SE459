import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import navigation.ControlSystemService;
import navigation.Navigation;

import org.xml.sax.SAXException;

import sensor.SensorLoader;
import sensor.SensorServices;


public class Driver {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SensorServices sL = SensorServices.getInstance();
		ControlSystemService cS = ControlSystemService.getInstance();
		Navigation n = Navigation.getInstance();
		sL.generateObstacleFloor();
		sL.generateSurfaceFloor();
		sL.generateTileFloor();
		cS.setNavigation(sL.getStartPosition());
		
	}

}