package com.itba.imagenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

public class ImageUtils {

	static Random rand = new Random();

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

	public static BufferedImage blackAndWhite(BufferedImage image,
			String colorStr) {
		int width = image.getWidth();
		int height = image.getHeight();
		int index;
		double[] rgb = new double[3];

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		ColorEnum color = ColorEnum.valueOf(colorStr);
		if (color == ColorEnum.RED) {
			index = 0;
		} else if (color == ColorEnum.GREEN) {
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

	public static BufferedImage getHistogram(BufferedImage img, String colorStr) {
		int width = img.getWidth();
		int height = img.getHeight();
		int index;
		double[] rgb = new double[3];
		int[] histogram = new int[256];

		ColorEnum color = ColorEnum.valueOf(colorStr);

		if (color == ColorEnum.RED) {
			index = 0;
		} else if (color == ColorEnum.GREEN) {
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

	public static BufferedImage equalizate(BufferedImage image, String colorStr) {
		int width = image.getWidth();
		int height = image.getHeight();
		int index;
		double[] rgb = new double[3];
		int[] histogram = new int[256];
		double[] s = new double[256];

		ColorEnum color = ColorEnum.valueOf(colorStr);

		if (color == ColorEnum.RED) {
			index = 0;
		} else if (color == ColorEnum.GREEN) {
			index = 1;
		} else {
			index = 2;
		}

		// int prom = 0;

		// obtain levels of grey
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.getRaster().getPixel(i, j, rgb);
				histogram[(int) rgb[index]]++;

			}
		}

		long totalPixels = width * height;

		// make s
		for (int i = 0; i < 256; i++) {
			s[i] = getSk(histogram, i, totalPixels);
		}

		// obtain minimin
		double[] aux = s.clone();
		Arrays.sort(aux);
		// double smin = aux[0];

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		double[] rgbAux = new double[3];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// obtain pixel
				image.getRaster().getPixel(i, j, rgb);
				// put new pixel
				rgbAux[0] = (int) ((s[(int) rgb[index]]) * 255);
				rgbAux[1] = rgbAux[0];
				rgbAux[2] = rgbAux[0];
				newImage.getRaster().setPixel(i, j, rgbAux);
			}
		}

		return newImage;
	}

	private static double getSk(int[] greyValues, int k, long totalPixels) {
		double aux = 0;
		for (int i = 0; i <= k; i++) {
			aux += ((double) greyValues[i]) / totalPixels;
		}

		return aux;
	}

	public static BufferedImage filterImage(BufferedImage image, ImageMask mask) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		int borderDistanceW = (int) (mask.getWidth() / 2);
		int borderDistanceH = (int) (mask.getHeight() / 2);

		// obtain pixels
		for (int i = borderDistanceW; i < width - borderDistanceW; i++) {
			for (int j = borderDistanceH; j < height - borderDistanceH; j++) {
				// for each pixel
				newImage.getRaster().setPixel(i, j,
						applyMask(image, mask, i, j));
			}
		}

		return newImage;
	}

	private static double[] applyMask(BufferedImage image, ImageMask mask,
			int x, int y) {
		int borderDistanceW = (int) (mask.getWidth() / 2);
		int borderDistanceH = (int) (mask.getHeight() / 2);
		double[] rgb = new double[3];
		double[] newPixel = new double[3];
		double aux;
		int factor = mask.getFactor();

		for (int i = 0; i < mask.getWidth(); i++) {
			for (int j = 0; j < mask.getHeight(); j++) {
				// get pixel
				image.getRaster().getPixel(x + i - borderDistanceW,
						y + j - borderDistanceH, rgb);
				// apply filter
				aux = mask.getValue(i, j);
				newPixel[0] += rgb[0] * aux / factor;
				newPixel[1] += rgb[1] * aux / factor;
				newPixel[2] += rgb[2] * aux / factor;
			}
		}

		newPixel[0] = Math.max(newPixel[0], 0);
		newPixel[1] = Math.max(newPixel[1], 0);
		newPixel[2] = Math.max(newPixel[2], 0);

		return newPixel;
	}

	public static BufferedImage filterMeanImage(BufferedImage image,
			int perimeter) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		int borderDistanceW = perimeter;
		int borderDistanceH = perimeter;

		// obtain pixels
		for (int i = borderDistanceW; i < width - borderDistanceW; i++) {
			for (int j = borderDistanceH; j < height - borderDistanceH; j++) {
				// for each pixel
				newImage.getRaster().setPixel(i, j,
						getMeanValue(image, i, j, perimeter));
			}
		}

		return newImage;
	}

	private static double[] getMeanValue(BufferedImage image, int x, int y,
			int perimeter) {
		double[] rgb = new double[3];
		double[] newPixel = new double[3];
		double[] values = new double[(2 * perimeter + 1) * (2 * perimeter + 1)];
		Double aux = 0.0;
		int count = 0;

		for (int i = -perimeter; i <= perimeter; i++) {
			for (int j = -perimeter; j <= perimeter; j++) {
				// get pixel
				image.getRaster().getPixel(x + i, y + j, rgb);
				// prom
				aux = (rgb[0] + rgb[1] + rgb[2]) / 3;

				// add to list
				values[count++] = aux;
			}
		}

		// sort array
		Arrays.sort(values);

		// if is even
		if (values.length % 2 == 0) {
			aux = (values[values.length / 2 - 1] + values[values.length / 2]) / 2;
		} else {
			aux = values[values.length / 2];
		}

		newPixel[0] = Math.max(aux, 0);
		newPixel[1] = Math.max(aux, 0);
		newPixel[2] = Math.max(aux, 0);

		return newPixel;
	}

	public static BufferedImage Hough(BufferedImage image, double deltaError) {
		int width = image.getWidth();
		int height = image.getHeight();
		double[] rgb = new double[3];

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		double thetas[] = { -Math.PI / 2, 0, Math.PI / 2, Math.PI };
		double ro[] = { width / 8, width / 4, width / 2, width };

		int[][] matrix = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 } };

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.getRaster().getPixel(i, j, rgb);

				if ((rgb[0] == 255) || (rgb[1] == 255) || (rgb[2] == 255))
					for (int k = 0; k < thetas.length; k++)
						for (int l = 0; l < ro.length; l++)
							if (ro[l] - i * Math.cos(thetas[k]) - j
									* Math.sin(thetas[k]) < deltaError)
								matrix[k][l]++;
			}
		}

		Graphics2D g = newImage.createGraphics();

		for (int i = 0; i < thetas.length; i++) {
			for (int j = 0; j < ro.length; j++) {
				if (matrix[i][j] > 0) {
					g.drawLine((int) (ro[j] * Math.sin(thetas[i])), 0, 0,
							(int) (ro[j] * Math.cos(thetas[i])));
				}
			}
		}

		return g.getDeviceConfiguration().createCompatibleImage(width, height);
	}

	public static BufferedImage isotropicDifusionFilter(BufferedImage img,
			float lambda, int iterations) {

		BufferedImage newImage = null;

		double red, green, blue;
		float dn, ds, dw, de;

		for (int k = 0; k < iterations; k++) {
			newImage = new BufferedImage(img.getWidth(), img.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			for (int j = 1; j < img.getHeight() - 1; ++j)
				for (int i = 1; i < img.getWidth() - 1; ++i) {
					Color pixel = new Color(img.getRGB(i, j));
					Color northPixel = new Color(img.getRGB(i, j + 1));
					Color southPixel = new Color(img.getRGB(i, j - 1));
					Color eastPixel = new Color(img.getRGB(i + 1, j));
					Color westPixel = new Color(img.getRGB(i - 1, j));

					// RED
					dn = -(pixel.getRed() - northPixel.getRed());
					ds = -(pixel.getRed() - southPixel.getRed());
					dw = -(pixel.getRed() - westPixel.getRed());
					de = -(pixel.getRed() - eastPixel.getRed());

					red = pixel.getRed() + lambda * (dn + ds + de + dw);

					// GREEN
					dn = -(pixel.getGreen() - northPixel.getGreen());
					ds = -(pixel.getGreen() - southPixel.getGreen());
					dw = -(pixel.getGreen() - westPixel.getGreen());
					de = -(pixel.getGreen() - eastPixel.getGreen());

					green = pixel.getGreen() + lambda * (dn + ds + de + dw);

					// BLUE
					dn = -(pixel.getBlue() - northPixel.getBlue());
					ds = -(pixel.getBlue() - southPixel.getBlue());
					dw = -(pixel.getBlue() - westPixel.getBlue());
					de = -(pixel.getBlue() - eastPixel.getBlue());

					blue = pixel.getBlue() + lambda * (dn + ds + de + dw);

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
			newImage = new BufferedImage(img.getWidth(), img.getHeight(),
					BufferedImage.TYPE_INT_RGB);

			for (int j = 1; j < img.getHeight() - 1; ++j)
				for (int i = 1; i < img.getWidth() - 1; ++i) {
					Color pixel = new Color(img.getRGB(i, j));
					Color northPixel = new Color(img.getRGB(i, j + 1));
					Color southPixel = new Color(img.getRGB(i, j - 1));
					Color eastPixel = new Color(img.getRGB(i + 1, j));
					Color westPixel = new Color(img.getRGB(i - 1, j));

					// RED
					dn = -(pixel.getRed() - northPixel.getRed());
					ds = -(pixel.getRed() - southPixel.getRed());
					dw = -(pixel.getRed() - westPixel.getRed());
					de = -(pixel.getRed() - eastPixel.getRed());

					cn = lorentz(dn, sigma);
					cs = lorentz(ds, sigma);
					cw = lorentz(dw, sigma);
					ce = lorentz(de, sigma);

					red = pixel.getRed() + lambda
							* (cn * dn + cs * ds + ce * de + cw * dw);

					// GREEN
					dn = -(pixel.getGreen() - northPixel.getGreen());
					ds = -(pixel.getGreen() - southPixel.getGreen());
					dw = -(pixel.getGreen() - westPixel.getGreen());
					de = -(pixel.getGreen() - eastPixel.getGreen());

					cn = lorentz(dn, sigma);
					cs = lorentz(ds, sigma);
					cw = lorentz(dw, sigma);
					ce = lorentz(de, sigma);

					green = pixel.getGreen() + lambda
							* (cn * dn + cs * ds + ce * de + cw * dw);

					// BLUE
					dn = -(pixel.getBlue() - northPixel.getBlue());
					ds = -(pixel.getBlue() - southPixel.getBlue());
					dw = -(pixel.getBlue() - westPixel.getBlue());
					de = -(pixel.getBlue() - eastPixel.getBlue());

					cn = lorentz(dn, sigma);
					cs = lorentz(ds, sigma);
					cw = lorentz(dw, sigma);
					ce = lorentz(de, sigma);

					blue = pixel.getBlue() + lambda
							* (cn * dn + cs * ds + ce * de + cw * dw);

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

	private static double lorentz(double x, double sigma) {
		return 1 / (1 + Math.pow(x, 2) / Math.pow(sigma, 2));
	}

	public static BufferedImage noiseSaltPepper(BufferedImage img, double p0,
			double p1) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		double newRand;

		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);

				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;

				newRand = rand.nextDouble();
				if (newRand <= p0)
					newPixel = 0;
				else if (newRand >= p1)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static BufferedImage noiseGauss(BufferedImage img, double mean,
			double stddev) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);
				// Saco el gris
				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;
				newPixel += gaussianPDF(mean, stddev);

				if (newPixel < 0 || newPixel > 255)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static BufferedImage noiseRayleigh(BufferedImage img, double sigma) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);
				// Saco el gris
				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;
				newPixel *= nextRayleigh(sigma);

				if (newPixel < 0 || newPixel > 255)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static BufferedImage noiseExp(BufferedImage img, double lambda) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);
				// Saco el gris
				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;
				newPixel *= exp(lambda);

				if (newPixel < 0 || newPixel > 255)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static double exp(double lambda) {
		return -Math.log(1 - Math.random()) / lambda;
	}

	public static double nextRayleigh(double sigma) {
		Double ret = Math.sqrt(-2.0D * Math.log(1.0D - rand.nextDouble()))
				* sigma;

		ret = (ret / Math.pow(sigma, 2))
				* Math.exp((-Math.pow(ret, 2)) / (2 * Math.pow(sigma, 2)));

		return ret;
	}

	public static double gaussianPDF(double mean, double stddev) {
		double gauss = gaussian(mean, stddev);
		double ret = 0;

		ret = (1 / (stddev * Math.sqrt(2 * Math.PI)))
				* Math.exp((Math.pow(-gauss - mean, 2))
						/ (2 * Math.pow(stddev, 2)));

		return ret;
	}

	public static double gaussian(double mean, double stddev) {
		return mean + stddev * gaussian();
	}

	public static double gaussian() {
		// use the polar form of the Box-Muller transform
		double r, x, y;
		do {
			x = uniform(-1.0, 1.0);
			y = uniform(-1.0, 1.0);
			r = x * x + y * y;
		} while (r >= 1 || r == 0);
		return x * Math.sqrt(-2 * Math.log(r) / r);

		// Remark: y * Math.sqrt(-2 * Math.log(r) / r)
		// is an independent random gaussian
	}

	public static double uniform(double a, double b) {
		return a + Math.random() * (b - a);
	}

}
