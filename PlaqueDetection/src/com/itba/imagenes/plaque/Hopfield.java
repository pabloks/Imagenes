package com.itba.imagenes.plaque;

import Jama.Matrix;

public class Hopfield {

	private int N;
	private Matrix weights;

	public Hopfield(int N) {
		this.N = N;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public Matrix getWeights() {
		return weights;
	}

	public void setWeights(Matrix weights) {
		this.weights = weights;
	}

	public void train(Matrix[] pattern) {
		weights = new Matrix(N, N);
		for (int k = 0; k < pattern.length; k++)
			weights.plusEquals(pattern[k].transpose().times(pattern[k]));

		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (i == j)
					weights.set(i, j, 0);
				else
					weights.set(i, j, weights.get(i, j) / N);
	}

	public Matrix evaluate(Matrix pattern) {
		int rand;
		while (true) {
			Matrix patternCopy = pattern.copy();
			Matrix sigma = pattern.times(weights);

			// Son vectores, uso Matrix de [1][N]
			int i = 0;
			for (int j = 0; j < N; j++)
				if (sigma.get(i, j) == 0)
					sigma.set(i, j, pattern.get(i, j));
				else if (sigma.get(i, j) > 0)
					sigma.set(i, j, 1);
				else if (sigma.get(i, j) < 0)
					sigma.set(i, j, -1);

			// Asi es LITTLE, SINCRONICO, pisa todos
			// pattern = sigma.copy();

			// Asi es HOPFIELD, ASINCRONICO, pisa solo uno
			rand = (int) (Math.random() * N);
			pattern.set(0, rand, sigma.get(0, rand));

			if (equalsPatterns(patternCopy, pattern))
				break;
		}

		return pattern;
	}

	public boolean equalsPatterns(Matrix pattern1, Matrix pattern2) {
		boolean equals = true;

		int i = 0;
		for (int j = 0; j < N && equals; j++)
			if (pattern1.get(i, j) != pattern2.get(i, j))
				equals = false;

		return equals;
	}
}
