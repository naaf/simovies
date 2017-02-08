package mypackage;

/* ******************************************************
 * Simovies - Eurobot 2015 Robomovies Simulator.
 * Copyright (C) 2014 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: algorithms/Stage1.java 2014-10-18 buixuan.
 * ******************************************************/

import robotsimulator.Brain;
import characteristics.Parameters;
import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;

import java.util.ArrayList;

public class Stage1_mieux extends Brain {
  //---PARAMETERS---//
  private static final double HEADINGPRECISION = 0.001;
  private static final double ANGLEPRECISION = 0.1;
  private static final int ROCKY = 0x1EADDA;
  private static final int CARREFOUR = 0x5EC0;
  private static final int DARTY = 0x333;
  private static final int UNDEFINED = 0xBADC0DE;

  //---VARIABLES---//
  private boolean turnNorthTask,turnLeftTask;
  private double endTaskDirection;
  private double myX,myY;
  private boolean isMoving;
  private int whoAmI;

  //---CONSTRUCTORS---//
  public Stage1_mieux() { super(); }

  //---ABSTRACT-METHODS-IMPLEMENTATION---//
  public void activate() {
    //ODOMETRY CODE
    whoAmI = ROCKY;
    for (IRadarResult o: detectRadar())
      if (isSameDirection(o.getObjectDirection(),Parameters.NORTH)) whoAmI=UNDEFINED;
    if (whoAmI == ROCKY){
      myX=Parameters.teamBMainBot1InitX;
      myY=Parameters.teamBMainBot1InitY;
    } else {
      myX=0;
      myY=0;
    }

    //INIT
    turnNorthTask=true;
    turnLeftTask=false;
    isMoving=false;
  }
  public void step() {
    //ODOMETRY CODE
    if (isMoving      &&  whoAmI == ROCKY             ){
      myX+=Parameters.teamBMainBotSpeed*Math.cos(getHeading());
      myY+=Parameters.teamBMainBotSpeed*Math.sin(getHeading());
      isMoving=false;
    }
    //DEBUG MESSAGE
    if (whoAmI == ROCKY) {
      sendLogMessage("#ROCKY is rolling at position ("+(int)myX+", "+(int)myY+").");
    }

    //AUTOMATON
    if (turnNorthTask && isHeading(Parameters.NORTH)) {
      turnNorthTask=false;
      myMove();
      //sendLogMessage("Moving a head. Waza!");
      return;
    }
    if (turnNorthTask && !isHeading(Parameters.NORTH)) {
      stepTurn(Parameters.Direction.RIGHT);
      //sendLogMessage("Initial TeamB position. Heading North!");
      return;
    }
    if (turnLeftTask && isHeading(endTaskDirection)) {
      turnLeftTask=false;
      myMove();
      //sendLogMessage("Moving a head. Waza!");
      return;
    }
    if (turnLeftTask && !isHeading(endTaskDirection)) {
      stepTurn(Parameters.Direction.LEFT);
      //sendLogMessage("Iceberg at 12 o'clock. Heading 9!");
      return;
    }
    if (!turnNorthTask && !turnLeftTask && detectFront().getObjectType()==IFrontSensorResult.Types.WALL) {
      turnLeftTask=true;
      endTaskDirection=getHeading()+Parameters.LEFTTURNFULLANGLE;
      stepTurn(Parameters.Direction.LEFT);
      //sendLogMessage("Iceberg at 12 o'clock. Heading 9!");
      return;
    }
    if (!turnNorthTask && !turnLeftTask && detectFront().getObjectType()!=IFrontSensorResult.Types.WALL) {
      myMove(); //And what to do when blind blocked?
      //sendLogMessage("Moving a head. Waza!");
      return;
    }
  }
  private void myMove(){
    isMoving=true;
    move();
  }
  private boolean isHeading(double dir){
    return Math.abs(Math.sin(getHeading()-dir))<HEADINGPRECISION;
  }
  private boolean isSameDirection(double dir1, double dir2){
    return Math.abs(dir1-dir2)<ANGLEPRECISION;
  }
}