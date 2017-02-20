package mypackage;

import java.util.ArrayList;
import java.util.List;

import robotsimulator.Brain;
import characteristics.IFrontSensorResult;
import characteristics.Parameters;

//@SuppressWarnings("unused")
public class Exo1 extends Brain {

	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private static final String[] NOM_ROBOTS = { "Ash", "MOR", "KALI", "SEC1",
			"SEC2" };
	private static int index = 0;

	private boolean turnNorthTask, turnLeftTask;

	List<Task> tasks;
	private double endTaskDirection;
	private double myX, myY;
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
		double distX = Math.abs(Parameters.teamAMainBotFrontalDetectionRange - myX);
		double distY = Math.abs(Parameters.teamAMainBotFrontalDetectionRange - myY);
		if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
			super.move();
			updateCoordonnees();

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
			this.myX = Parameters.teamAMainBot1InitX;
			this.myY = Parameters.teamAMainBot1InitY;
			break;
		case 1:
			this.myX = Parameters.teamAMainBot2InitX;
			this.myY = Parameters.teamAMainBot2InitY;
			break;
		case 2:
			this.myX = Parameters.teamAMainBot3InitX;
			this.myY = Parameters.teamAMainBot3InitY;
			break;
		}
	}

	private void updateCoordonnees() {
		myX += Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY += Parameters.teamBMainBotSpeed * Math.sin(getHeading());
	}

	private void logMe() {
		sendLogMessage("#" + name + " is rolling at position (" + (int) myX
				+ ", " + (int) myY + ").");
	}

}
