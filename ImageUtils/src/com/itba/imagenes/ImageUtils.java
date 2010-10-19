package com.itba.imagenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Arrays;
import java.util.Random;

import javax.swing.ImageIcon;

import com.itba.imagenes.canny.CannyEdgeDetector;
import com.itba.imagenes.hough.GreyscaleFilter;
import com.itba.imagenes.hough.LineHoughTransformOp;
import com.itba.imagenes.hough.SobelEdgeDetectorFilter;
import com.itba.imagenes.hough.ThresholdFilter;
import com.itba.imagenes.hough.circleHough;
import com.itba.imagenes.hough.hystThresh;
import com.itba.imagenes.hough.nonMaxSuppression;
import com.itba.imagenes.hough.sobel;

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

	private static double varianza(BufferedImage image, int x, int y, int m) {
		double[] rgb = new double[3];
		double aux = 0;

		for (int i = x - m; i < x + m; i++) {
			for (int j = y - m; j < y + m; j++) {
				image.getRaster().getPixel(i, j, rgb);
				aux += Math.pow(
						(rgb[0] + rgb[1] + rgb[2]) / 3
								- varianzaW(image, i, j, m), 2);
			}
		}

		return aux / Math.pow(2 * m + 1, 2);
	}

	private static double varianzaW(BufferedImage image, int x, int y, int m) {
		double[] rgb = new double[3];
		double aux = 0;

		for (int i = x - m; i < x + m; i++) {
			for (int j = y - m; j < y + m; j++) {
				image.getRaster().getPixel(i, j, rgb);
				aux += (rgb[0] + rgb[1] + rgb[2]) / 3;
			}
		}

		return aux / Math.pow(2 * m + 1, 2);
	}

	public static BufferedImage laplacevarianza(BufferedImage image, int m) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		double[][] values1 = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
		ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

		BufferedImage maskedImage = ImageUtils.filterImage(image, mask1);

		double[] rgb = new double[3];
		double[] rgbant = new double[3];
		double[] white = { 255, 255, 255 };
		double[] black = { 0, 0, 0 };
		int umbral = 230;
		int umbral_laplace = 20;

		// rows
		for (int i = 2 * m; i < width - 2 * m; i++) {
			for (int j = 2 * m + 1; j < height - 2 * m; j++) {
				maskedImage.getRaster().getPixel(i, j - 1, rgbant);
				maskedImage.getRaster().getPixel(i, j, rgb);
				if (Math.abs(rgbant[0] - rgb[0]) > umbral) {
					if (varianza(maskedImage, i, j, m) < umbral_laplace)
						newImage.getRaster().setPixel(i, j, white);
					else
						newImage.getRaster().setPixel(i, j, black);
				} else
					newImage.getRaster().setPixel(i, j, black);
			}
		}

		// cols
		for (int j = 2 * m; j < width - 2 * m; j++) {
			for (int i = 2 * m + 1; i < height - 2 * m; i++) {
				maskedImage.getRaster().getPixel(i, j - 1, rgbant);
				maskedImage.getRaster().getPixel(i, j, rgb);
				if (Math.abs(rgbant[0] - rgb[0]) > umbral) {
					if (varianza(maskedImage, i, j, m) < umbral_laplace)
						newImage.getRaster().setPixel(i, j, white);
					else
						newImage.getRaster().setPixel(i, j, black);
				} else
					newImage.getRaster().setPixel(i, j, black);
			}
		}

		return newImage;
	}

	public static BufferedImage crossbycero(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		double[][] values1 = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
		ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

		BufferedImage maskedImage = ImageUtils.filterImage(image, mask1);

		double[] rgb = new double[3];
		double[] rgbant = new double[3];
		double[] white = { 255, 255, 255 };
		double[] black = { 0, 0, 0 };
		int umbral = 5;

		// rows
		for (int i = 0; i < width; i++) {
			for (int j = 1; j < height; j++) {
				maskedImage.getRaster().getPixel(i, j - 1, rgbant);
				maskedImage.getRaster().getPixel(i, j, rgb);
				if (Math.abs(rgbant[0] - rgb[0]) < umbral)
					newImage.getRaster().setPixel(i, j, white);
				else
					newImage.getRaster().setPixel(i, j, black);
			}
		}

		// cols
		for (int j = 0; j < width; j++) {
			for (int i = 1; i < height; i++) {
				maskedImage.getRaster().getPixel(i - 1, j, rgbant);
				maskedImage.getRaster().getPixel(i, j, rgb);
				if (Math.abs(rgbant[0] - rgb[0]) < umbral)
					newImage.getRaster().setPixel(i, j, white);
				else
					newImage.getRaster().setPixel(i, j, black);
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

	public static BufferedImage filterImageW(BufferedImage image, ImageMask mask) {
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
						applyMaskW(image, mask, i, j));
			}
		}

		return newImage;
	}

	private static double[] applyMask(BufferedImage image, ImageMask mask,
			int x, int y) {

		double[] newPixel = applyMaskW(image, mask, x, y);

		newPixel[0] = Math.max(newPixel[0], 0);
		newPixel[1] = Math.max(newPixel[1], 0);
		newPixel[2] = Math.max(newPixel[2], 0);

		return newPixel;
	}

	private static double[] applyMaskW(BufferedImage image, ImageMask mask,
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
		BufferedImage in = image;

		GreyscaleFilter s1 = new GreyscaleFilter();
		SobelEdgeDetectorFilter s2 = new SobelEdgeDetectorFilter();
		ThresholdFilter s3 = new ThresholdFilter();

		BufferedImage in1 = s1.filter(in); // to greyscale
		BufferedImage in2 = s2.filter(in1);
		BufferedImage in3 = s3.filter(in2);

		LineHoughTransformOp hough = new LineHoughTransformOp();

		double accRatio = deltaError; // 0.25d
		int lpn = 7; // 7 local Peaks

		hough.setLocalPeakNeighbourhood(lpn);
		hough.run(in3);
		hough.setLineColor(Color.RED);

		/*
		 * javax.imageio.ImageIO.write(in2, "png", new java.io.File(
		 * "houghSobel.png")); javax.imageio.ImageIO.write(in3, "png", new
		 * java.io.File( "houghInput.png"));
		 * javax.imageio.ImageIO.write(hough.getAccumulatorImage(), "png", new
		 * java.io.File("houghAccumulator.png"));
		 * javax.imageio.ImageIO.write(hough.getSuperimposed(in, accRatio),
		 * "png", new java.io.File("houghSuperimposed.png"));
		 */
		return hough.getSuperimposed(in, accRatio);
	}

	public static BufferedImage HoughCircle(BufferedImage img,
			int ciclesRadius, int numberOfCicles) {

		Image image = img.getScaledInstance(256, 256, Image.SCALE_SMOOTH);

		Image LinesImage = null, SobelImage = null, MaxSuppImage = null, HystImage = null, OverlayImage = null, HoughAccImage = null;

		int lines = numberOfCicles;
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int orig[] = null;
		orig = new int[width * height];
		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height,
				orig, 0, width);
		try {
			grabber.grabPixels();

			sobel sobelObject = new sobel();
			nonMaxSuppression nonMaxSuppressionObject = new nonMaxSuppression();
			hystThresh hystThreshObject = new hystThresh();
			circleHough circleHoughObject = new circleHough();

			sobelObject.init(orig, width, height);
			orig = sobelObject.process();
			double direction[] = new double[width * height];
			direction = sobelObject.getDirection();
			SobelImage = Toolkit.getDefaultToolkit().createImage(
					new MemoryImageSource(width, height, orig, 0, width));

			nonMaxSuppressionObject.init(orig, direction, width, height);
			orig = nonMaxSuppressionObject.process();
			MaxSuppImage = Toolkit.getDefaultToolkit().createImage(
					new MemoryImageSource(width, height, orig, 0, width));

			hystThreshObject.init(orig, width, height, 25, 50);
			orig = hystThreshObject.process();
			HystImage = Toolkit.getDefaultToolkit().createImage(
					new MemoryImageSource(width, height, orig, 0, width));

			circleHoughObject.init(orig, width, height, ciclesRadius);
			circleHoughObject.setLines(lines);
			orig = circleHoughObject.process();
			OverlayImage = Toolkit.getDefaultToolkit().createImage(
					new MemoryImageSource(width, height, overlayImage(orig,
							image), 0, width));

			@SuppressWarnings("unused")
			int rmax = (int) Math.sqrt(width * width + height * height);
			int acc[] = new int[width * height];
			acc = circleHoughObject.getAcc();
			HoughAccImage = Toolkit
					.getDefaultToolkit()
					.createImage(
							new MemoryImageSource(width, height, acc, 0, width))
					.getScaledInstance(256, 256, Image.SCALE_SMOOTH);

			LinesImage = Toolkit.getDefaultToolkit().createImage(
					new MemoryImageSource(width, height, orig, 0, width));
		} catch (Exception e) {
			System.out.println(e);
		}

		return toBufferedImage(OverlayImage);
	}

	public static int[] overlayImage(int[] input, Image image) {

		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int[] myImage = new int[width * height];

		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height,
				myImage, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if ((input[y * width + x] & 0xff) > 0)
					myImage[y * width + x] = 0xffff0000;
			}
		}

		return myImage;
	}

	// This method returns a buffered image with the contents of an image
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

	public static BufferedImage Canny(BufferedImage image, float low, float hi) {
		CannyEdgeDetector detector = new CannyEdgeDetector();

		detector.setLowThreshold(low);
		detector.setHighThreshold(hi);

		detector.setSourceImage(image);
		detector.process();

		return detector.getEdgesImage();
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
