package com.itba.imagenes.plaque;

import Jama.Matrix;

public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int N = 5;

		double[][] pattern1 = { { -1, 1, 1, 1, -1 } };
		double[][] pattern2 = { { 1, -1, 1, -1, 1 } };

		Matrix[] patterns = { new Matrix(pattern1), new Matrix(pattern2) };

		Hopfield hopfield = new Hopfield(N);
		hopfield.train(patterns);
		hopfield.getWeights().print(N, 2);

		double[][] testPattern = { { -1, 1, - 1, 1, 1 } };
		Matrix testPatternMatrix = new Matrix(testPattern);
		hopfield.evaluate(testPatternMatrix).print(N, 2);
		
		System.out.println(hopfield.patternExists(new Matrix(testPattern)));
	}
}
