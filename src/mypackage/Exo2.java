package mypackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.Parameters;
import robotsimulator.Brain;

//@SuppressWarnings("unused")
public class Exo2 extends Brain {

    private static final double HEADINGPRECISION = 0.001;
    private static final double ANGLEPRECISION = 0.1;
    private static final String[] NOM_ROBOTS = { "Ash", "MOR", "KALI", "SEC1", "SEC2" };
    private static int index = 0;

    public static Map<String, Exo2> robots;

    static {
	robots = new HashMap<>();
    }

    List<Task> tasks;
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
//	if (isHeading(endTaskDirection)) {
//	    move();
//
//	} else {
//	    stepTurn(Parameters.Direction.RIGHT);
//	}
//	logMe();
    }

    @Override
    public void move() {
	double distX = Math.abs(Parametrage.WIDTH - Position.deplacement(position, getHeading() ).x);
	double distY = Math.abs(Parametrage.HEIGHT - Position.deplacement(position, getHeading() ).y);
//
	if ((isSameDirection(getHeading(),Parameters.EAST) && distX > Parameters.teamAMainBotRadius ) || (isSameDirection(getHeading(),Parameters.SOUTH) && distY > Parameters.teamAMainBotRadius )
		|| (isSameDirection(getHeading(),Parameters.WEST) && distX > Parameters.teamAMainBotRadius )) {
	//if (detectFront().getObjectType() != IFrontSensorResult.Types.WALL) {
		
	    if (!interCollision()) {
			super.move();
			position = Position.deplacement(position, getHeading() );
	    } else {
	    	System.out.println("==========>>>>>>><collison");
	    }
	}
	else {
//		System.out.println("direction " + getHeading());
		if(isSameDirection(getHeading(),Parameters.WEST)) System.out.println( "min x => " + (position.x - Parameters.teamAMainBotFrontalDetectionRange));
		if(isSameDirection(getHeading(),Parameters.EAST)) System.out.println( "max x => " + (position.x + Parameters.teamAMainBotFrontalDetectionRange));
		if(isSameDirection(getHeading(),Parameters.NORTH))System.out.println( "min y => " + (position.y - Parameters.teamAMainBotFrontalDetectionRange));
		if(isSameDirection(getHeading(),Parameters.SOUTH))System.out.println( "max y => " + (position.y + Parameters.teamAMainBotFrontalDetectionRange));
		
	    if (!tasks.isEmpty()) {
		endTaskDirection = tasks.get(0).getDirection();
		tasks.remove(0);
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
	return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
    }

    private boolean isSameDirection(double dir1, double dir2) {
	return Math.abs(dir1 - dir2) < ANGLEPRECISION;
    }

    private void init(int indexRobot) {
	this.name = NOM_ROBOTS[indexRobot];
	this.endTaskDirection = Parameters.teamAMainBot1InitHeading;
	position = new Position();

	tasks = new ArrayList<Task>();
	Task tk;
	tk = new Task();
	tk.setDirection(Parameters.SOUTH);
	tasks.add(tk);
	tk = new Task();
	tk.setDirection(Parameters.WEST);
	tasks.add(tk);
	tk = new Task();
	tk.setDirection(Parameters.NORTH);
	tasks.add(tk);
	tk.setDirection(Parameters.EAST);
	tasks.add(tk);


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
	sendLogMessage("#" + name + " is rolling at position (" + position.x + ", " + position.y + ").");
    }

}
