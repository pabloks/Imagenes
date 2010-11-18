package com.itba.imagenes.plaque;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Jama.Matrix;

public class PlaqueDetector {
	static int UMBRAL = 100;
	static int BLACK = -1;
	static int WHITE = 1;
	static int PATTERN_HEIGHT = 52;
	static int PATTERN_WIDTH = 26;

	public static void main(String[] args) throws IOException {

		// 1-TRAINING
		List<Matrix> trainPatterns = new ArrayList<Matrix>();
		trainPatterns.add(imageToPattern("A"));
		trainPatterns.add(imageToPattern("B"));
		trainPatterns.add(imageToPattern("C"));
		trainPatterns.add(imageToPattern("D"));
		trainPatterns.add(imageToPattern("E"));
		trainPatterns.add(imageToPattern("F"));
		trainPatterns.add(imageToPattern("G"));
		trainPatterns.add(imageToPattern("H"));
		trainPatterns.add(imageToPattern("I"));
		trainPatterns.add(imageToPattern("J"));
		trainPatterns.add(imageToPattern("K"));
		trainPatterns.add(imageToPattern("L"));
		trainPatterns.add(imageToPattern("M"));
		trainPatterns.add(imageToPattern("N"));
		trainPatterns.add(imageToPattern("O"));
		trainPatterns.add(imageToPattern("P"));
		trainPatterns.add(imageToPattern("Q"));
		trainPatterns.add(imageToPattern("R"));
		trainPatterns.add(imageToPattern("S"));

		Matrix[] trainPattersArray = new Matrix[trainPatterns.size()];
		for (int i = 0; i < trainPatterns.size(); i++)
			trainPattersArray[i] = trainPatterns.get(i);

		Hopfield hopfield = new Hopfield(PATTERN_HEIGHT * PATTERN_HEIGHT);
		hopfield.train(trainPattersArray);

		// 2-TESTING
		Matrix foundPattern = hopfield.evaluate(imageToPattern("F_MOD"));

		File outputImageFile = new File(
				"/Users/iandrono/Documents/ITBA/Imagenes/workspace/patentes/hopfield.jpg");

		ImageIO.write(patternToImage(foundPattern), "jpg", outputImageFile);
	}

	public static Matrix imageToPattern(String character) throws IOException {

		BufferedImage img = ImageIO.read(new File(
				"/Users/iandrono/Documents/ITBA/Imagenes/workspace/patentes/"
						+ character + ".png"));

		int[] rgb = new int[3];
		int prom;
		Matrix pattern = new Matrix(1, PATTERN_HEIGHT * PATTERN_HEIGHT);

		for (int i = 0; i < PATTERN_WIDTH; i++) {
			for (int j = 0; j < PATTERN_HEIGHT; j++) {
				img.getRaster().getPixel(i, j, rgb);
				prom = ((rgb[0] + rgb[1] + rgb[2]) / 3) > UMBRAL ? WHITE
						: BLACK;
				pattern.set(0, i + j * PATTERN_WIDTH, prom);
			}
		}

		for (int i = PATTERN_HEIGHT * PATTERN_WIDTH; i < PATTERN_HEIGHT
				* PATTERN_HEIGHT; i++) {
			pattern.set(0, i, BLACK);
		}

		return pattern;
	}

	public static BufferedImage patternToImage(Matrix pattern) {
		int[] rgb = new int[3];
		BufferedImage img = new BufferedImage(PATTERN_WIDTH, PATTERN_HEIGHT,
				BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < PATTERN_WIDTH; i++) {
			for (int j = 0; j < PATTERN_HEIGHT; j++) {
				rgb[0] = rgb[1] = rgb[2] = pattern
						.get(0, i + j * PATTERN_WIDTH) == BLACK ? 0 : 255;
				img.getRaster().setPixel(i, j, rgb);
			}
		}

		return img;
	}
}
