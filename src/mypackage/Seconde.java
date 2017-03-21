package mypackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robotsimulator.Brain;
import characteristics.IRadarResult;
import characteristics.Parameters;

//@SuppressWarnings("unused")
public class Seconde extends Brain {

	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private static final String[] NOM_ROBOTS = { "SEC1", "SEC2" };
	private static int index = 0;

	public static Map<String, Seconde> robots;

	static {
		robots = new HashMap<>();
	}

	private double endTaskDirection;
	private Position position;
	private String name;

	@Override
	public void activate() {
		synchronized (NOM_ROBOTS) {
			init(index);
			index++;
		}
	}

	@Override
	public void step() {
		if (isHeading(endTaskDirection)) {

			move();

		} else {
			// endTaskDirection = tasks.get(0).getDirection();
			stepTurn(Parameters.Direction.LEFT);
			// if(isHeading(endTaskDirection))
			// tasks.remove(0);

		}
		logMe();
	}

	@Override
	public void move() {
		double distX = Math.abs(Parametrage.WIDTH
				- Position.deplacement(position, getHeading(),1).x);
		double distY = Math.abs(Parametrage.HEIGHT
				- Position.deplacement(position, getHeading(),1).y);
		//
		if ((isHeading(Parameters.EAST) && distX > Parameters.teamAMainBotRadius)
				|| (isHeading(Parameters.SOUTH) && distY > Parameters.teamAMainBotRadius)
				|| (isHeading(Parameters.WEST) && distX > Parameters.teamAMainBotRadius)
				|| (isHeading(Parameters.NORTH) && distY > Parameters.teamAMainBotRadius)) {
			// if (detectFront().getObjectType() !=
			// IFrontSensorResult.Types.WALL) {
			if (!interCollision()) {
				super.move();
				position = Position.deplacement(position, getHeading(),1);
			} else {
			}
		}
	}

	private boolean interCollision() {
		ArrayList<IRadarResult> radarResults = detectRadar();
		for (IRadarResult r : radarResults)
			if (r.getObjectType() == IRadarResult.Types.TeamMainBot) {
				if (r.getObjectDistance() <= 100) {

					return true;
				}
			}
		return false;

	}

	private boolean isHeading(double dir) {
		// System.out.println("sin " + Math.abs(Math.sin(getHeading() - dir)) +
		// "cos " + Math.abs(Math.cos(getHeading() - dir)) );
		return (Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION)
				&& (Math.abs(Math.cos(getHeading() - dir) - 1) < HEADINGPRECISION);
	}

	private void init(int indexRobot) {
		this.name = NOM_ROBOTS[indexRobot];
		position = new Position();

		switch (indexRobot) {
		case 0:
			this.position.x = Parameters.teamASecondaryBot1InitX;
			this.position.y = Parameters.teamASecondaryBot1InitY;
			endTaskDirection = Parameters.NORTH;
			break;
		case 1:
			this.position.x = Parameters.teamASecondaryBot2InitX;
			this.position.y = Parameters.teamASecondaryBot2InitX;
			endTaskDirection = Parameters.SOUTH;
			break;
		}
	}

	private void logMe() {
		sendLogMessage("#" + name + " is rolling at position (" + position.x
				+ ", " + position.y + ").");
	}

}
