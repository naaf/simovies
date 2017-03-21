package mypackage;

import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robotsimulator.Brain;
import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.Parameters;

//@SuppressWarnings("unused")
public class Exo2 extends Brain {

	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private static final String[] NOM_ROBOTS = { "Ash", "MOR", "KALI" };
	static double GDIR = 0;

	private static int index = 0;

	int corX = 298, corY = 149;

	public static Map<String, Exo2> robots;

	static {
		robots = new HashMap<>();
	}

	private double direction;
	private Position position;
	private String name;
	private boolean isBack;
	private boolean isTurn;
	private static boolean isGroup;
	private boolean sameDirection;
	private boolean danger;
	private int id;
	public boolean fusion;
	public static boolean _fusion;
	private static boolean init_fusion;

	@Override
	public void activate() {
		synchronized (NOM_ROBOTS) {
			init(index);
			index++;
		}
	}

	@Override
	public void step() {
		// TODO detect and fire
		if (isGroup) {

			if (!robots.values().stream().map(e -> e.fusion).reduce(true, (e,v) -> e&&v)) {
				this.direction = Parameters.EAST;
				isGroup = false;
//				if (robots.values().stream().map(e -> e.sameDirection).reduce(true, (e,v) -> e&&v) ){
//					isGroup = false;
//				} else {
//					makeSameDirect();
//				}
			} else {
				fusion();
			}
		} else if (isHeading(direction)) {
			move();

		} else {
			stepTurn(Parameters.Direction.RIGHT);
		}
		logMe();
	}

	@Override
	public void move() {

		if (isBack) {
			isBack = false;
			moveBack();
			position = Position.deplacement(position, getHeading(), -1);
		} else if (isTurn) {
			isTurn = false;
			stepTurn(Parameters.Direction.RIGHT);

		} else if (!isMurCollision()) {

			if (!isRobotCollision()) {
				super.move();
				position = Position.deplacement(position, getHeading(), 1);
			} else {
				// isTurn = true;
				// move();
			}
		} else {
			// isBack = true;
			// move();
		}
	}

	private boolean isRobotCollision() {
		ArrayList<IRadarResult> radarResults = detectRadar();
		for (IRadarResult r : radarResults)
			if (r.getObjectType() == IRadarResult.Types.TeamMainBot
					|| r.getObjectType() == IRadarResult.Types.TeamSecondaryBot
					|| r.getObjectType() == IRadarResult.Types.OpponentMainBot
					|| r.getObjectType() == IRadarResult.Types.OpponentSecondaryBot
					|| r.getObjectType() == IRadarResult.Types.Wreck) {
				if (r.getObjectDistance() <= 100) {
					System.out.println(">>>>>>>>>>>>>>>> collision robots!!!!");
					return true;
				}
			}
		return false;
	}

	// private boolean isHeading(double dir) {
	// return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
	// }
	private boolean isHeading(double dir) {
		// System.out.println("sin " + Math.abs(Math.sin(getHeading() - dir))
		// + "cos " + Math.abs(Math.cos(getHeading() - dir)));
		return (Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION)
				&& (Math.abs(Math.cos(getHeading() - dir) - 1) < HEADINGPRECISION);
	}

	private boolean isSameDirection(double dir1, double dir2) {
		return Math.abs(dir1 - dir2) < ANGLEPRECISION;
	}

	private void init(int indexRobot) {
		this.name = NOM_ROBOTS[indexRobot];
		position = new Position();
		this.isBack = false;
		this.isTurn = false;
		isGroup = true;
		this.fusion = false;
		this.sameDirection = false;

		switch (indexRobot) {
		case 0:
			this.position.x = Parameters.teamAMainBot1InitX;
			this.position.y = Parameters.teamAMainBot1InitY;
			this.direction = Parameters.SOUTH;
			this.id = 0;
			break;
		case 1:
			this.position.x = Parameters.teamAMainBot2InitX;
			this.position.y = Parameters.teamAMainBot2InitY;
			this.direction = Parameters.EAST;
			this.id = 1;
			break;
		case 2:
			this.position.x = Parameters.teamAMainBot3InitX;
			this.position.y = Parameters.teamAMainBot3InitY;
			this.direction = Parameters.NORTH;
			this.id = 2;
			break;
		}
	}

	private void logMe() {
		sendLogMessage("#" + name + " is rolling at position (" + position.x
				+ ", " + position.y + ").");
	}

	private boolean isMurCollision() {
		int coef = isBack ? -1 : 1;
		Position b = Position.deplacement(position, direction, coef);

		if (49 <= b.x && b.x <= 2949 && 50 <= b.y && b.y <= 1949)
			return false;
		System.out.println(">>>>>>>>>>>>>>>> mur detect!!!!");
		return true;

	}

	private void fusion() {
		System.out.println("<<<<<<<<<<<<<<<<Fusion");
		switch (this.id) {
		case 0:
			if (!isRobotCollision())
				if (isHeading(direction))
					move();
				else
					stepTurn(Parameters.Direction.RIGHT);
			else
				this.fusion = true;
			break;
		case 1:
			this.fusion = true;
			break;
		case 2:
			if (!isRobotCollision())
				if (isHeading(direction))
					move();
				else
					stepTurn(Parameters.Direction.LEFT);
			else
				this.fusion = true;
			break;
		}

	}

	private void makeSameDirect() {
		switch (this.id) {
		case 0:
			if (!isHeading(direction))
				stepTurn(Parameters.Direction.RIGHT);
			else
				this.sameDirection = true;
			break;
		case 1:
			this.fusion = true;
			break;
		case 2:
			if (!isHeading(direction))
				stepTurn(Parameters.Direction.LEFT);
			else
				this.sameDirection = true;
			break;
		}
	}

}
