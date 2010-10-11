package com.itba.imagenes.hough;

public class Circle {
	private final double vx, vy, vradius;

	public Circle(double radio, double x, double y) {
		vx = x;
		vy = y;
		vradius = radio;
	}

	public double getX() {
		return vx;
	}

	public double getY() {
		return vy;
	}

	public double getRadius() {
		return vradius;
	}

}