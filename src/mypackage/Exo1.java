package mypackage;

import characteristics.Parameters;
import robotsimulator.Brain;

public class Exo1 extends Brain {

	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private boolean turnNorthTask, turnLeftTask;
	private double endTaskDirection;
	private double myX, myY;
	private boolean isMoving;
	private int whoAmI;
	private static final String[] NOM_ROBOTS = { "Ash", "MOR", "KALI" };
	private static int index = 0;
	private double mycurrentDirection;

	@Override
	public void activate() {
		endTaskDirection = Parameters.NORTH;
		mycurrentDirection = Parameters.EAST;
	}

	@Override
	public void step() {
		if (!isHeading(endTaskDirection)) {
			stepTurn(Parameters.Direction.LEFT);
			System.out.println("ok ==> " + getHeading());
		} 
		move();
	}

	private boolean isHeading(double dir) {
		return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
	}

	private boolean isSameDirection(double dir1, double dir2) {
		return Math.abs(dir1 - dir2) < ANGLEPRECISION;
	}
}
