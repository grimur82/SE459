package navigation;

import sensor.Obstacle;

public class ControlSystemLoader {
	private Navigation navigation;
	private CleanSweeper cleanSweeper;
	private static ControlSystemLoader controlSystemLoader = new ControlSystemLoader();
	private ControlSystemLoader(){
		navigation = Navigation.getInstance();
		cleanSweeper = CleanSweeper.getInstance();
	}
	public static ControlSystemLoader getInstance(){
		return controlSystemLoader;
	}
	public void setNavigation(Obstacle s){
		navigation.setX(s.getX());
		navigation.setY(s.getY());
	}
	public void setSweeperDirection(String direction){
		cleanSweeper.setDirection(direction);
	}
	public void shutdownSweeper(){
		cleanSweeper.shutDown();
	}
}