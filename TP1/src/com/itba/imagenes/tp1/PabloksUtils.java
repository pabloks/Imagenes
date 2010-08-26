package com.itba.imagenes.tp1;

import java.awt.image.BufferedImage;

import com.itba.imagenes.Color;
import com.itba.imagenes.ParamsReader;

public class PabloksUtils {

	public static void negative(ParamsReader params) {

		BufferedImage image = params.loadNextImage();

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

		params.saveImage(image);
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
}
