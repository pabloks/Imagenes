package com.itba.imagenes.hough;

import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;
import ij.gui.*;

/**
 * Hough_Circles.java This ImageJ plugin shows the Hough Transform Space for
 * circles of predefined radius. The source image must be an 8-Bit black &
 * white.
 * <p>
 * HoughCircles_.java is open-source. You are free to do anything you want with
 * this source as long as I get credit for my work.
 * 
 * @author Hemerson Pistori (pistori@ec.ucdb.br) e Eduardo Rocha Costa
 *         (eduardo.rocha@poli.usp.br) The Hough Transform implementation was
 *         based on an applet by Mark A. Schulze (http://www.markschulze.net/)
 */
public class Hough_Circles implements PlugInFilter {
	public float radius; // Radius of circles to be found
	public int maxCircles; // Numbers of circles to be found
	public int threshold = -1; // An alternative to maxCircles
	// must lies in the interval: [-125,+125].
	// Higher values - fewer circles.
	byte imageValues[]; // Raw image (returned by ip.getPixels())
	double houghValues[][]; // Hough Space Values
	public int width; // Hough Space width
	public int height; // Hough Space heigh
	public int offset; // Image Width
	public int offx; // ROI x offset
	public int offy; // ROI y offset
	Point centerPoint[]; // Center Points of the Circles Found.
	private int vectorMaxSize = 500;
	boolean useThreshold = false;

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	}

	public void run(ImageProcessor ip) {
		imageValues = (byte[]) ip.getPixels();
		Rectangle r = ip.getRoi();
		offx = r.x;
		offy = r.y;
		width = r.width;
		height = r.height;
		offset = ip.getWidth();
		if (readParameters()) { // Show a Dialog Window for user input of
			// radius and maxCircles.
			houghTransform();
			// Create image View for Hough Transform.
			ImageProcessor newip = new ByteProcessor(width, height);
			byte[] newpixels = (byte[]) newip.getPixels();
			createHoughPixels(newpixels);
			// Create image View for Marked Circles.
			ImageProcessor circlesip = new ByteProcessor(width, height);
			byte[] circlespixels = (byte[]) circlesip.getPixels();
			// Mark the center of the found circles in a new image
			if (useThreshold)
				getCenterPointsByThreshold(threshold);
			else
				getCenterPoints(maxCircles);
			drawCircles(circlespixels);
			new ImagePlus("Hough Space [r=" + radius + "]", newip).show();
			new ImagePlus(maxCircles + " Circles Found", circlesip).show();
		}
	}

	void showAbout() { 
         IJ.showMessage("About Circles_...", 
                        "This plugin finds n circles of fixed radius\n" + 
                        "using a basic HoughTransform operator\n." + 
                        "For better results apply an Edge Detector\n" + 
                        "filter and a binarizer before using this 
plugin\n"+ 
                        "\nAuthor: Hemerson Pistori (pistori@ec.ucdb.br)" 
                       ); 
     }	boolean readParameters() { 
         GenericDialog gd = new GenericDialog("Hough Parameters", 
IJ.getInstance()); 
         gd.addNumericField("Radius (in pixels):", 10, 0); 
         gd.addNumericField("Number of Circles (NC): (enter 0 if using 
threshold)", 10, 0); 
         gd.addNumericField("Threshold: (not used if NC > 0)", 60, 0); 
         gd.showDialog(); 
         if (gd.wasCanceled()) { 
             return(false); 
         } 
         radius = (float) gd.getNextNumber(); 
         maxCircles = (int) gd.getNextNumber(); 
         threshold = (int) gd.getNextNumber(); 
         if (maxCircles > 0) { 
             useThreshold = false; 
             threshold = -1; 
         } else { 
             useThreshold = true; 
             if(threshold < 0) { 
                 IJ.showMessage("Threshold must be greater than 0"); 
                 return(false); 
             } 
         } 
         return(true); 
     }	// The hard work is done here.

	private void houghTransform() {
		int i = 0;
		int j = 1;
		int k = width - j;
		int l = height - j;
		houghValues = new double[width][height];
		int i2 = Math.round(8F * radius);
		int ai[][] = new int[2][i2];
		for (int j2 = 0; j2 < i2; j2++) {
			double d1 = (6.2831853071795862D * (double) j2) / (double) i2;
			int k1 = (int) Math.round((double) radius * Math.cos(d1));
			int l1 = (int) Math.round((double) radius * Math.sin(d1));
			if ((i == 0) | (k1 != ai[0][i]) & (l1 != ai[1][i])) {
				ai[0][i] = k1;
				ai[1][i] = l1;
				i++;
			}
		}
		double d;
		for (int y = j; y < l; y++) {
			for (int x = j; x < k; x++) {
				if (imageValues[(x + offx) + (y + offy) * offset] == 0)
					d = 0;
				else
					d = 1;
				if (d != 0.0D) {
					for (int i3 = 0; i3 < i; i3++) {
						int i1 = y + ai[0][i3];
						int j1 = x + ai[1][i3];
						if ((i1 >= 0) & (i1 < height) & (j1 >= 0)
								& (j1 < width)) {
							houghValues[j1][i1] += d;
						}
					}
				}
			}
		}
	}

	// Convert Values in Hough Space to an 8-Bit Image Space.
	private void createHoughPixels(byte houghPixels[]) {
		double d = -1D;
		for (int j = 0; j < height; j++) {
			for (int k = 0; k < width; k++)
				if (houghValues[k][j] > d) {
					d = houghValues[k][j];
				}
		}
		for (int l = 0; l < height; l++) {
			for (int i1 = 0; i1 < width; i1++) {
				houghPixels[i1 + l * width] = (byte) Math
						.round((houghValues[i1][l] * 255D) / d);
			}
		}
	}

	// Draw the circles found in the original image.
	public void drawCircles(byte[] circlespixels) {
		// Copy original image to the circlespixels image.
		// Changing pixels values to 100, so that the marked
		// circles appears more clear.
		for (int i = 0; i < width * height; ++i) {
			if (imageValues[i] != 0)
				circlespixels[i] = 100;
			else
				circlespixels[i] = 0;
		}
		if (centerPoint == null) {
			if (useThreshold)
				getCenterPointsByThreshold(threshold);
			else
				getCenterPoints(maxCircles);
		}
		byte cor = -1;
		for (int l = 0; l < maxCircles; l++) {
			int i = centerPoint[l].x;
			int j = centerPoint[l].y;
			// Draw a gray cross marking the center of each circle.
			for (int k = -10; k <= 10; ++k) {
				if (!outOfBounds(j + k + offy, i + offx))
					circlespixels[(j + k + offy) * offset + (i + offx)] = cor;
				if (!outOfBounds(j + offy, i + k + offx))
					circlespixels[(j + offy) * offset + (i + k + offx)] = cor;
			}
			for (int k = -2; k <= 2; ++k) {
				if (!outOfBounds(j - 2 + offy, i + k + offx))
					circlespixels[(j - 2 + offy) * offset + (i + k + offx)] = cor;
				if (!outOfBounds(j + 2 + offy, i + k + offx))
					circlespixels[(j + 2 + offy) * offset + (i + k + offx)] = cor;
				if (!outOfBounds(j + k + offy, i - 2 + offx))
					circlespixels[(j + k + offy) * offset + (i - 2 + offx)] = cor;
				if (!outOfBounds(j + k + offy, i + 2 + offx))
					circlespixels[(j + k + offy) * offset + (i + 2 + offx)] = cor;
			}
		}
	}

	private boolean outOfBounds(int y, int x) {
		if (x >= width)
			return (true);
		if (x <= 0)
			return (true);
		if (y >= height)
			return (true);
		if (y <= 0)
			return (true);
		return (false);
	}

	public Point nthMaxCenter(int i) {
		return centerPoint[i];
	}

	private void getCenterPoints(int i) {
		centerPoint = new Point[maxCircles];
		double d2 = radius / 2.0F;
		double d3 = d2 * d2;
		int j1 = 0;
		int k1 = 0;
		for (int l1 = 0; l1 < i; l1++) {
			double d1 = -1;
			for (int i2 = 0; i2 < height; i2++) {
				for (int j2 = 0; j2 < width; j2++)
					if (houghValues[j2][i2] > d1) {
						d1 = houghValues[j2][i2];
						j1 = j2;
						k1 = i2;
					}
			}
			centerPoint[l1] = new Point(j1, k1);
			int j = (int) Math.floor((double) k1 - d2);
			if (j < 0)
				j = 0;
			int k = (int) Math.ceil((double) k1 + d2) + 1;
			if (k > height)
				k = height;
			int l = (int) Math.floor((double) j1 - d2);
			if (l < 0)
				l = 0;
			int i1 = (int) Math.ceil((double) j1 + d2) + 1;
			if (i1 > width)
				i1 = width;
			for (int k2 = j; k2 < k; k2++) {
				for (int l2 = l; l2 < i1; l2++)
					if (Math.pow(l2 - j1, 2D) + Math.pow(k2 - k1, 2D) < d3)
						houghValues[l2][k2] = 0.0D;
			}
		}
	}

	private void getCenterPointsByThreshold(int threshold) {
		centerPoint = new Point[vectorMaxSize];
		double d2 = radius / 2.0F;
		double d3 = d2 * d2;
		int j1 = 0;
		int k1 = 0;
		int countCircles;
		countCircles = 0;
		for (int i2 = 0; i2 < height; i2++) {
			for (int j2 = 0; j2 < width; j2++) {
				if (houghValues[j2][i2] > threshold) {
					j1 = j2;
					k1 = i2;
					if (countCircles < vectorMaxSize) {
						centerPoint[countCircles] = new Point(j1, k1);
						int j = (int) Math.floor((double) k1 - d2);
						if (j < 0)
							j = 0;
						int k = (int) Math.ceil((double) k1 + d2) + 1;
						if (k > height)
							k = height;
						int l = (int) Math.floor((double) j1 - d2);
						if (l < 0)
							l = 0;
						int i1 = (int) Math.ceil((double) j1 + d2) + 1;
						if (i1 > width)
							i1 = width;
						++countCircles;
					} else
						break;
				}
			}
		}
		maxCircles = countCircles;
	}
}