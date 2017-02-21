package mypackage;

import characteristics.Parameters;

public class Position {
	double x;
	double y;

	public static boolean collision(Position a, Position b) {
		return false;
	}

	public static Position deplacement(Position a, double heading) {
		Position b = new Position();
		b.x = a.x + Parameters.teamBMainBotSpeed * Math.cos(heading);
		b.y = a.y += Parameters.teamBMainBotSpeed * Math.sin(heading);
		return b;
	}

}
