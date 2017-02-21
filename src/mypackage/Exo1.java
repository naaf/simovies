package mypackage;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import characteristics.IFrontSensorResult;
import characteristics.Parameters;
import robotsimulator.Brain;

//@SuppressWarnings("unused")
public class Exo1 extends Brain {

	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private static final String[] NOM_ROBOTS = { "Ash", "MOR", "KALI", "SEC1", "SEC2" };
	private static int index = 0;

	private boolean turnNorthTask, turnLeftTask;
	public static Map<String, Exo1> robots;
	static {
		robots = new HashMap<>();
	}

	List<Task> tasks;
	private double endTaskDirection;
	private Position position;
	private boolean isMoving;
	private String name;
	private double mycurrentDirection;

	@Override
	public void activate() {
		init(index);
		index++;
	}

	@Override
	public void step() {
		if (isHeading(endTaskDirection)) {
			move();

		} else {
			stepTurn(Parameters.Direction.RIGHT);
		}
		logMe();
	}

	@Override
	public void move() {
		double distX = Math.abs(Parameters.teamAMainBotFrontalDetectionRange - position.x);
		double distY = Math.abs(Parameters.teamAMainBotFrontalDetectionRange - position.y);

		if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
			super.move();
			position = Position.deplacement(position, getHeading());

		} else {
			if (!tasks.isEmpty()) {
				endTaskDirection = tasks.get(0).getDirection();
				tasks.remove(0);
			}
		}
	}

	private boolean isHeading(double dir) {
		return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
	}

	private boolean isSameDirection(double dir1, double dir2) {
		return Math.abs(dir1 - dir2) < ANGLEPRECISION;
	}

	private void init(int indexRobot) {
		this.isMoving = false;
		this.name = NOM_ROBOTS[indexRobot];
		this.endTaskDirection = Parameters.teamAMainBot1InitHeading;

		tasks = new ArrayList<Task>();
		Task tk = new Task();
		tk.setDirection(Parameters.SOUTH);
		tasks.add(tk);
		tk = new Task();
		tk.setDirection(Parameters.WEST);
		tasks.add(tk);
		tk = new Task();
		tk.setDirection(Parameters.NORTH);
		tasks.add(tk);

		this.mycurrentDirection = Parameters.EAST;

		switch (indexRobot) {
		case 0:
			this.position.x = Parameters.teamAMainBot1InitX;
			this.position.y = Parameters.teamAMainBot1InitY;
			break;
		case 1:
			this.position.x = Parameters.teamAMainBot2InitX;
			this.position.y = Parameters.teamAMainBot2InitY;
			break;
		case 2:
			this.position.x = Parameters.teamAMainBot3InitX;
			this.position.y = Parameters.teamAMainBot3InitY;
			break;
		}
	}

	private void logMe() {
		sendLogMessage("#" + name + " is rolling at position (" + position.x + ", " + position.x + ").");
	}

}
