package com.itba.imagenes.tp2;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.itba.imagenes.ParamsReader;

public class Disfunction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParamsReader params = null;
		try {
			ParamsReader.workPath += "tp2/";
			params = new ParamsReader(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedImage inputImage1 = params.loadNextImage();
		BufferedImage output = null;

		output = isotropicDifusionFilter(inputImage1, (float) 0.1);

		params.saveImage(output);
	}

	public static BufferedImage isotropic(BufferedImage img, float t) {

		GaussianFilter gf = new GaussianFilter(t);
		return gf.filter(img, null);
	}

	public static BufferedImage anisotropicDifusionFilter(BufferedImage img,
			float lambda, float cn, float cs, float ce, float cw) {

		BufferedImage newImage = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double red, green, blue;

		for (int j = 1; j < img.getHeight() - 1; ++j)
			for (int i = 1; i < img.getWidth() - 1; ++i) {
				Color pixel = new Color(img.getRGB(i, j));
				Color northPixel = new Color(img.getRGB(i, j + 1));
				Color southPixel = new Color(img.getRGB(i, j - 1));
				Color eastPixel = new Color(img.getRGB(i + 1, j));
				Color westPixel = new Color(img.getRGB(i - 1, j));

				red = pixel.getRed()
						+ lambda
						* (northPixel.getRed() * cn + southPixel.getRed() * cs
								+ eastPixel.getRed() * ce + westPixel.getRed()
								* cw);
				green = pixel.getGreen()
						+ lambda
						* (northPixel.getGreen() * cn + southPixel.getGreen()
								* cs + eastPixel.getGreen() * ce + westPixel
								.getGreen() * cw);

				blue = pixel.getBlue()
						+ lambda
						* (northPixel.getBlue() * cn + southPixel.getBlue()
								* cs + eastPixel.getBlue() * ce + westPixel
								.getBlue() * cw);

				if (red < 0 || red > 255)
					red = 255;
				if (green < 0 || green > 255)
					green = 255;
				if (blue < 0 || blue > 255)
					blue = 255;

				newImage.setRGB(i, j, new Color((int) red, (int) green,
						(int) blue).getRGB());
			}

		return newImage;
	}

	public static BufferedImage isotropicDifusionFilter(BufferedImage img,
			float lambda) {
		return anisotropicDifusionFilter(img, lambda, 1, 1, 1, 1);
	}
}
