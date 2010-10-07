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

//		output = isotropicDifusionFilter(inputImage1, (float) 0.1);

		params.saveImage(output);
	}

	public static BufferedImage isotropicDifusionFilter(BufferedImage img,
			float lambda, int iterations) {

		BufferedImage newImage = null;

		double red, green, blue;
		float dn, ds, dw, de;

		for (int k = 0; k < iterations; k++) {
			newImage = new BufferedImage(img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_INT_RGB);
			for (int j = 1; j < img.getHeight() - 1; ++j)
				for (int i = 1; i < img.getWidth() - 1; ++i) {
					Color pixel = new Color(img.getRGB(i, j));
					Color northPixel = new Color(img.getRGB(i, j + 1));
					Color southPixel = new Color(img.getRGB(i, j - 1));
					Color eastPixel = new Color(img.getRGB(i + 1, j));
					Color westPixel = new Color(img.getRGB(i - 1, j));

					// RED
					dn = - (pixel.getRed() - northPixel.getRed());
					ds = - (pixel.getRed() - southPixel.getRed());
					dw = - (pixel.getRed() - westPixel.getRed());
					de = - (pixel.getRed() - eastPixel.getRed());

					red = pixel.getRed()
							+ lambda
							* (dn + ds + de + dw);

					// GREEN
					dn = -(pixel.getGreen() - northPixel.getGreen());
					ds = -(pixel.getGreen() - southPixel.getGreen());
					dw = -(pixel.getGreen() - westPixel.getGreen());
					de = -(pixel.getGreen() - eastPixel.getGreen());

					green = pixel.getGreen()
							+ lambda
							* (dn + ds + de + dw);

					// BLUE
					dn = -(pixel.getBlue() - northPixel.getBlue());
					ds = -(pixel.getBlue() - southPixel.getBlue());
					dw = -(pixel.getBlue() - westPixel.getBlue());
					de = -(pixel.getBlue() - eastPixel.getBlue());

					blue = pixel.getBlue()
							+ lambda
							* (dn + ds + de + dw);

					red = Math.max(0, Math.min(255, red));
					green = Math.max(0, Math.min(255, green));
					blue = Math.max(0, Math.min(255, blue));
					
					newImage.setRGB(i, j, new Color((int) red, (int) green,
							(int) blue).getRGB());
				}
			
			img = newImage;
		}
		return newImage;
	}
	
	public static BufferedImage anisotropicDifusionFilter(BufferedImage img,
			float lambda, int iterations, float sigma) {

		BufferedImage newImage = null;

		double red, green, blue;
		float dn, ds, dw, de;
		double cn, cs, cw, ce;

		for (int k = 0; k < iterations; k++) {
			newImage = new BufferedImage(img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			for (int j = 1; j < img.getHeight() - 1; ++j)
				for (int i = 1; i < img.getWidth() - 1; ++i) {
					Color pixel = new Color(img.getRGB(i, j));
					Color northPixel = new Color(img.getRGB(i, j + 1));
					Color southPixel = new Color(img.getRGB(i, j - 1));
					Color eastPixel = new Color(img.getRGB(i + 1, j));
					Color westPixel = new Color(img.getRGB(i - 1, j));

					// RED
					dn = - (pixel.getRed() - northPixel.getRed());
					ds = - (pixel.getRed() - southPixel.getRed());
					dw = - (pixel.getRed() - westPixel.getRed());
					de = - (pixel.getRed() - eastPixel.getRed());
					
					cn = lorentz(dn, sigma);
					cs = lorentz(ds, sigma);
					cw = lorentz(dw, sigma);
					ce = lorentz(de, sigma);

					red = pixel.getRed()
							+ lambda
							* (cn*dn + cs*ds + ce*de + cw*dw);

					// GREEN
					dn = -(pixel.getGreen() - northPixel.getGreen());
					ds = -(pixel.getGreen() - southPixel.getGreen());
					dw = -(pixel.getGreen() - westPixel.getGreen());
					de = -(pixel.getGreen() - eastPixel.getGreen());
					
					cn = lorentz(dn, sigma);
					cs = lorentz(ds, sigma);
					cw = lorentz(dw, sigma);
					ce = lorentz(de, sigma);

					green = pixel.getGreen()
							+ lambda
							* (cn*dn + cs*ds + ce*de + cw*dw);

					// BLUE
					dn = -(pixel.getBlue() - northPixel.getBlue());
					ds = -(pixel.getBlue() - southPixel.getBlue());
					dw = -(pixel.getBlue() - westPixel.getBlue());
					de = -(pixel.getBlue() - eastPixel.getBlue());
					
					cn = lorentz(dn, sigma);
					cs = lorentz(ds, sigma);
					cw = lorentz(dw, sigma);
					ce = lorentz(de, sigma);

					blue = pixel.getBlue()
							+ lambda
							* (cn*dn + cs*ds + ce*de + cw*dw);

					red = Math.max(0, Math.min(255, red));
					green = Math.max(0, Math.min(255, green));
					blue = Math.max(0, Math.min(255, blue));
					
					newImage.setRGB(i, j, new Color((int) red, (int) green,
							(int) blue).getRGB());
				}
			img = newImage;
		}
		
		return newImage;
	}
	
	private static double lorentz(double x, double sigma){
		return 1/(1 + Math.pow(x, 2)/Math.pow(sigma, 2));
	}
}
