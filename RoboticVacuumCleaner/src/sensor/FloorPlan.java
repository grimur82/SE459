package sensor;

import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import util.Debugger;

import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Read floor plan object.
 */
public class FloorPlan {

	// Singleton instance
	private static FloorPlan instance = null;

	// Known floor plan
	private Cell[][] floorPlan;
	
	// Known doors
	private HashMap<Coordinate, DoorStatus> doorList;

	// Current position
	private Coordinate startPosition;
	public void askuserforfile() throws URISyntaxException {
		FloorPlanLoader loader = new FloorPlanLoader();
		loader.askUserForFile();
		loadFloorData(loader);
	}
	public void loadFloorData(FloorPlanLoader loader){
		Debugger.log("Starting sensor simulator");
		Debugger.log("Starting control system");
		floorPlan = loader.getFloorPlan();
		startPosition = loader.getStartPosition();
		doorList = loader.getDoorList();
	}
	public Coordinate getCellCordinates(double x, double y){
		return floorPlan[(int)x][(int)y].getCoordinate();
	}
	/**
	 * Singleton.
	 *
	 * @return Instance.
	 * @throws URISyntaxException 
	 */
	public static FloorPlan getInstance() throws URISyntaxException {
		if (instance == null)
			instance = new FloorPlan();
		return instance;
	}

	/**
	 * Get floor plan.
	 *
	 * @return Floor plan.
	 */
	public Cell[][] getFloorPlan() {
		return floorPlan;
	}

	/**
	 * Get door list.
	 *
	 * @return Door list.
	 */
	public HashMap<Coordinate, DoorStatus> getDoorList() {
		return doorList;
	}

	/**
	 * Get current position.
	 *
	 * @return Current position.
	 */
	public Coordinate getStartPosition() {
		return startPosition;
	}

	/**
	 * Get cell.
	 *
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Cell.
	 */
	public Cell getCell(int x, int y) {
		return (floorPlan.length <= x || floorPlan[0].length <= y)
				? null : floorPlan[x][y];
	}

	/**
	 * Change door status of a coordinate.
	 *
	 * @param coordinate Coordinate whose door status is to be changed.
	 */
	void changeDoorStatus(Coordinate coordinate) {

		DoorStatus doorStatus = doorList.get(coordinate);
		doorList.put(
				coordinate,
				doorStatus == DoorStatus.CLOSED ? DoorStatus.OPEN : DoorStatus.CLOSED
		);

	}
	DoorStatus getDoorStatus(Coordinate coordinate) {
		return doorList.get(coordinate);
	}

}
