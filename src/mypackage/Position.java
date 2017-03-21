package mypackage;

import characteristics.Parameters;

public class Position {
	private static final double WIDTH = Parameters.teamAMainBotRadius * 2;
	double x;
	double y;

	public Position() {
	}

	public Position(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public static boolean collision1(Position a, Position b) {
		if ((a.x < b.x + WIDTH && a.x + WIDTH > b.x)
				&& (a.y < b.y + WIDTH && a.y + WIDTH > b.y)) {
			return true;
		}
		return false;
	}

	public static boolean collision(Position a, Position b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		double distance = (dx * dx + dy * dy);

		if (distance < (Parameters.teamAMainBotRadius * 2)
				* (Parameters.teamAMainBotRadius * 2)) {
			return true;
		}
		return false;
	}

	public static Position deplacement(Position a, double heading, int coef) {
		Position b = new Position();
		b.x = a.x + (Parameters.teamBMainBotSpeed * Math.cos(heading) * coef) ;
		b.y = a.y + (Parameters.teamBMainBotSpeed * Math.sin(heading) * coef);
		return b;
	}

	public static void main(String[] args) {
		Position a = new Position(Parameters.teamAMainBot1InitX,
				Parameters.teamAMainBot1InitY);
		Position b = new Position(Parameters.teamAMainBot2InitX,
				Parameters.teamAMainBot2InitY);
		System.out.println(collision1(a, b));
	}

}
