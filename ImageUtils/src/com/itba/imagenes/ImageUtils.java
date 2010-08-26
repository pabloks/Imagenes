package com.itba.imagenes;

import java.awt.image.BufferedImage;

public class ImageUtils {

	public static BufferedImage MathOperatorFunction(BufferedImage imgInput1,
			BufferedImage imgInput2, String oper, Double scalar)
			throws Exception {

		int maxHeight = imgInput1.getHeight();
		int maxWidth = imgInput1.getWidth();

		if (imgInput2 != null) {
			maxHeight = (imgInput1.getHeight() > imgInput2.getHeight() ? imgInput1
					.getHeight() : imgInput2.getHeight());

			maxWidth = (imgInput1.getWidth() > imgInput2.getWidth() ? imgInput1
					.getWidth() : imgInput2.getWidth());
		}

		BufferedImage imgOutput = new BufferedImage(maxWidth, maxHeight,
				BufferedImage.TYPE_INT_RGB);

		double[] rgb1 = new double[3];
		double[] rgb2 = new double[3];
		double[] out = new double[3];

		for (int i = 0; i < maxWidth; i++) {
			for (int j = 0; j < maxHeight; j++) {

				try {
					imgInput1.getRaster().getPixel(i, j, rgb1);
				} catch (ArrayIndexOutOfBoundsException e) {
					rgb1[0] = 0;
					rgb1[1] = 0;
					rgb1[2] = 0;
				}

				try {
					imgInput2.getRaster().getPixel(i, j, rgb2);
				} catch (ArrayIndexOutOfBoundsException e) {
					rgb2[0] = 0;
					rgb2[1] = 0;
					rgb2[2] = 0;
				}

				if (oper.equalsIgnoreCase("suma")) {
					out[0] = rgb1[0] + rgb2[0];
					out[1] = rgb1[1] + rgb2[1];
					out[2] = rgb1[2] + rgb2[2];
				} else if (oper.equalsIgnoreCase("resta")) {
					out[0] = rgb1[0] - rgb2[0];
					out[1] = rgb1[1] - rgb2[1];
					out[2] = rgb1[2] - rgb2[2];
				} else if (oper.equalsIgnoreCase("mult")) {
					out[0] = rgb1[0] * rgb2[0];
					out[1] = rgb1[1] * rgb2[1];
					out[2] = rgb1[2] * rgb2[2];
				} else if (oper.equalsIgnoreCase("scalar")) {
					out[0] = rgb1[0] * scalar;
					out[1] = rgb1[1] * scalar;
					out[2] = rgb1[2] * scalar;
				} else
					throw new Exception("Parametro invalido");

				// Si se va de 0-255 lo mando a 0
				if (out[0] < 0 || out[0] > 255)
					out[0] = 255;
				if (out[1] < 0 || out[1] > 255)
					out[1] = 255;
				if (out[2] < 0 || out[2] > 255)
					out[2] = 255;

				imgOutput.getRaster().setPixel(i, j, out);
			}
		}

		return imgOutput;
	}

	public static BufferedImage negative(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		double[] rgb = new double[3];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.getRaster().getPixel(i, j, rgb);
				rgb[0] = 255 - rgb[0];
				rgb[1] = 255 - rgb[1];
				rgb[2] = 255 - rgb[2];
				newImage.getRaster().setPixel(i, j, rgb);
			}
		}

		return newImage;
	}

	public static BufferedImage blackAndWhite(BufferedImage image, Color color) {
		int width = image.getWidth();
		int height = image.getHeight();
		int index;
		double[] rgb = new double[3];

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		if (color == Color.RED) {
			index = 0;
		} else if (color == Color.GREEN) {
			index = 1;
		} else {
			index = 2;
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.getRaster().getPixel(i, j, rgb);

				rgb[0] = rgb[index];
				rgb[1] = rgb[index];
				rgb[2] = rgb[index];
				newImage.getRaster().setPixel(i, j, rgb);
			}
		}

		return newImage;
	}

	public static BufferedImage getHistogram(BufferedImage img, Color color) {
		int width = img.getWidth();
		int height = img.getHeight();
		int index;
		double[] rgb = new double[3];
		int[] histogram = new int[256];

		if (color == Color.RED) {
			index = 0;
		} else if (color == Color.GREEN) {
			index = 1;
		} else {
			index = 2;
		}

		int max = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				img.getRaster().getPixel(i, j, rgb);
				if (++histogram[(int) rgb[index]] > max)
					max = histogram[(int) rgb[index]];

			}
		}

		width = 256;
		height = 256;

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		int[] white = { 255, 255, 255 };

		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				newImage.getRaster().setPixel(i, j, white);
			}
		}

		int[] black = { 0, 0, 0 };
		int y = 0;

		for (int i = 0; i < newImage.getWidth(); i++) {
			y = histogram[i] * 255 / max;
			System.out.println(i + " " + y);
			for (int j = 0; j < y; j++) {
				newImage.getRaster().setPixel(i, height - 1 - j, black);
			}
		}

		return newImage;
	}

	public static BufferedImage RangoDinamicoFunction(BufferedImage imgInput1,
			Double scalar) throws Exception {

		double[] rgb1 = new double[3];
		double greyValue;

		for (int i = 0; i < imgInput1.getWidth(); i++) {
			for (int j = 0; j < imgInput1.getHeight(); j++) {

				imgInput1.getRaster().getPixel(i, j, rgb1);

				// Lo paso a gris con el promedio:
				greyValue = (rgb1[0] + rgb1[1] + rgb1[2]) / 3;

				// Le aplico rango dinamico:
				greyValue = scalar * Math.log(greyValue + 1);

				rgb1[0] = greyValue;
				rgb1[1] = greyValue;
				rgb1[2] = greyValue;

				imgInput1.getRaster().setPixel(i, j, rgb1);
			}
		}

		return imgInput1;
	}

	public static BufferedImage UmbralFunction(BufferedImage imgInput1,
			Double scalar) throws Exception {

		double[] rgb1 = new double[3];
		double greyValue;

		for (int i = 0; i < imgInput1.getWidth(); i++) {
			for (int j = 0; j < imgInput1.getHeight(); j++) {

				imgInput1.getRaster().getPixel(i, j, rgb1);

				// Lo paso a gris con el promedio:
				greyValue = (rgb1[0] + rgb1[1] + rgb1[2]) / 3;

				// Le aplico umbral:
				if (greyValue <= scalar)
					greyValue = 0;
				else
					greyValue = 255;

				rgb1[0] = greyValue;
				rgb1[1] = greyValue;
				rgb1[2] = greyValue;

				imgInput1.getRaster().setPixel(i, j, rgb1);
			}
		}

		return imgInput1;
	}
}
