package com.itba.imagenes;

public class ImageMask {
	private double[][] values;
	private int width; 
	private int height;
	private int factor;
	
	public double[][] getValues() {
		return values;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public ImageMask(double[][] values, int width, int height, int factor) {
		// TODO Auto-generated constructor stub
		this.values = values.clone();
		this.width = width;
		this.height = height;
		this.factor = factor;
	}
	
	public double getValue(int x, int y){
		if (x > width || y > height){
			return 0;
		}
		return values[x][y];
	}

	public int getFactor() {
		return factor;
	}
	
}
