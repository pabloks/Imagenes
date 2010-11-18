package com.itba.imagenes.plaque;

import javax.swing.text.MaskFormatter;

import Jama.Matrix;

public class Hopfield {

	private int N;
	private Matrix weights;
	Matrix[] patterns;

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
		this.patterns = pattern;
		
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
		int countEqual = 0;
		int countNotEqual = 0;
		int maxNotEqual = 10;
		int maxEqual = 10;
		boolean lastEqual = true;
		boolean found = false;
		
		while (!found) {
			Matrix patternCopy = pattern.copy();
			Matrix sigma = pattern.times(weights);

			// Son vectores, uso Matrix de [1][N]
			int i = 0;
			for (int j = 0; j < N; j++)
				if (sigma.get(i, j) == 0)
					sigma.set(i, j, pattern.get(i, j));
				else if (sigma.get(i, j) > 0)
					sigma.set(i, j, 1);
				else if (sigma.get(i, j) < -0)
					sigma.set(i, j, -1);

			// Asi es LITTLE, SINCRONICO, pisa todos
			// pattern = sigma.copy();

			// Asi es HOPFIELD, ASINCRONICO, pisa solo uno
			rand = (int) (Math.random() * N);
			pattern.set(0, rand, Math.signum(sigma.get(0, rand)));
			
			if (equalsPatterns(patternCopy, pattern)){
				//patternExists(pattern)
				if (lastEqual){
					countEqual++;
					System.out.println(countEqual);
					if (countEqual > maxEqual){
						found = patternExists(pattern);
						if (!found){
							countEqual = 0;
							System.out.println("Mutate");
							rand = (int) (Math.random() * N);
							pattern.set(0, rand, Math.signum(sigma.get(0, rand)));
						}
					}
				}else{
					countNotEqual++;
					countEqual = 0;
					lastEqual = false;
					
					//mutate si se estanca
					if (countNotEqual > maxNotEqual){
						rand = (int) (Math.random() * N);
						pattern.set(0, rand, Math.signum(sigma.get(0, rand)));
						countNotEqual=0;
					}
				}
			}						
		}

		return pattern;
	}
	
	public boolean equalsInvertedPatterns(Matrix pattern1, Matrix pattern2) {
		boolean distinct = true;

		int i = 0;
		for (int j = 0; j < N && distinct; j++)
			distinct = Math.signum(pattern1.get(i, j)) != Math.signum(pattern2.get(i, j));
		
		return distinct;
	}
	
	public boolean equalsPatterns(Matrix pattern1, Matrix pattern2) {
		boolean equals = true;

		int i = 0;
		for (int j = 0; j < N && equals; j++)
			if (Math.signum(pattern1.get(i, j)) != Math.signum(pattern2.get(i, j)))
				equals = false;
		
		return equals;
	}
	
	public boolean patternExists(Matrix pattern){
		boolean found = false;
		for(int i = 0; i< patterns.length && !found; i++)
			if (equalsPatterns(pattern, patterns[i]) || equalsInvertedPatterns(pattern, patterns[i]))
				found = true;
		
		return found;
			
	}
}
