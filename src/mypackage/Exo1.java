package mypackage;

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
			if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
				move();
				myX += Parameters.teamBMainBotSpeed * Math.cos(getHeading());
				myY += Parameters.teamBMainBotSpeed * Math.sin(getHeading());

			} else {
				fire(endTaskDirection);
			}
		} else {
			stepTurn(Parameters.Direction.LEFT);
		}
		logMe();
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
		this.endTaskDirection = Parameters.NORTH;
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

	private void logMe() {
		sendLogMessage("#" + name + " is rolling at position (" + (int) myX + ", " + (int) myY + ").");
	}

}
