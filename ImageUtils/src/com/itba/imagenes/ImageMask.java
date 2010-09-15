package com.itba.imagenes;

public class ImageMask {
	private double[][] values;
	private int width; 
	private int height;
	
	public double[][] getValues() {
		return values;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public ImageMask(double[][] values, int width, int height) {
		// TODO Auto-generated constructor stub
		this.values = values.clone();
		this.width = width;
		this.height = height;
	}
	
	public double getValue(int x, int y){
		if (x > width || y > height){
			return 0;
		}
		return values[x][y];
	}
	
}
