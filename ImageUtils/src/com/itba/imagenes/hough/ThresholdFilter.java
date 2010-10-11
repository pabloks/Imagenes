/*
 * Threshold.java
 * Created on 12 July 2004, 18:57
 *
 * Revision 18/11/04: All threshold methods have been altered to
 * use a greyscale type, instead of cloning the image type.
 *
 * Copyright 2004, Generation5. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package com.itba.imagenes.hough;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * This class encapsulates a variety of thresholding algorithms. The current
 * available thresholding algorithms are:
 * 
 * <ul>
 * <li>
 * Error diffusion</li>
 * <li>
 * Pattern diffusion</li>
 * <li>
 * One-point threshold</li>
 * </ul>
 * 
 * 
 * @author James Matthews
 */
public class ThresholdFilter extends Filter {
	/**
	 * One-point thresholding. Any value below the limit is thresholded to
	 * black, anything above is thresholded to white.
	 */
	public static final int ONE_POINT = 0;

	/**
	 * Pattern-diffusion thresholding. This uses a 4x4 dithering matrix to
	 * create a grouping of pixels that approximates the pixel group brightness.
	 */
	public static final int PATTERN_DIFFUSION = 1;

	/**
	 * Error-diffusion thresholding. This uses Floyd-Steinberg's algorithm to
	 * keep a track of the error in brightness caused by the thresholding, and
	 * diffusing it across an area. This is the best thresholding algorithms for
	 * photographs and other complicated images.
	 */
	public static final int ERROR_DIFFUSION = 2;

	/**
	 * Two-point thresholding. Any image between the two threshold points is
	 * painted white, otherwise the pixel is painted black.
	 */
	public static final int TWO_POINT = 3;

	/** The thresholding mode */
	protected int thresholdMode = ONE_POINT;

	/**
	 * The threshold limit using by one-point thresholding.
	 * 
	 * @see #ONE_POINT
	 */
	protected int thresholdLimit = 128;

	/** The upper limit used in two-point thresholding. */
	protected int upperLimit = 128;

	/**
	 * Creates a new instance of Threshold
	 */
	public ThresholdFilter() {
		this(ONE_POINT);
	}

	/**
	 * Creates a new instances of Threshold, and sets the threshold limit
	 * 
	 * @param threshold
	 *            the threshold limit
	 * 
	 * @see #setThresholdLimit(int)
	 */
	public ThresholdFilter(int threshold) {
		thresholdMode = threshold;
	}

	/**
	 * Sets the threshold limit used by one- or two-point thresholding. For one-
	 * point thresholding it is the primary threshold point. For two-point
	 * thresholding, it specifies the lower threshold limit.
	 * 
	 * @param limit
	 *            the threshold limit
	 */
	public void setThresholdLimit(int limit) {
		thresholdLimit = limit;
	}

	/**
	 * Retrieve the thresholding limit for one- or two-point thresholding. For
	 * one- point thresholding it is the primary threshold point. For two-point
	 * thresholding, it specifies the lower threshold limit.
	 * 
	 * @return the current thresholding limit
	 */
	public int getThresholdLimit() {
		return thresholdLimit;
	}

	/**
	 * Set the upper threshold limit for two-point thresholding.
	 * 
	 * @param upper
	 *            the upper threshold limit.
	 */
	public void setUpperLimit(int upper) {
		upperLimit = upper;
	}

	/**
	 * Retrieve the upper threshold limit for two-point thresholding.
	 * 
	 * @return the upper limit used in two-point thresholding.
	 */
	public int getUpperLimit() {
		return upperLimit;
	}

	/**
	 * Set the upper and lower threshold limits used in two-point thresholding.
	 * 
	 * @param threshold
	 *            the lower threshold limit.
	 * @param upper
	 *            the upper threshold limit.
	 */
	public void setThresholdLimits(int threshold, int upper) {
		thresholdLimit = threshold;
		upperLimit = upper;
	}

	/**
	 * Set the threshold mode.
	 * 
	 * @param mode
	 *            the threshold mode.
	 */
	public void setThresholdMode(int mode) {
		thresholdMode = mode;
	}

	/**
	 * Return the threshold mode.
	 * 
	 * @return the threshold mode.
	 */
	public int getThresholdMode() {
		return thresholdMode;
	}

	/**
	 * Threshold the image according to the mode selected.
	 * 
	 * @param image
	 *            the input image.
	 * @param output
	 *            the output image (optional).
	 * 
	 * @return the output image.
	 */
	public BufferedImage filter(BufferedImage image, BufferedImage output) {
		// Convert to greyscale
		BufferedImage greyImage = GreyscaleFilter.toGrey(image, null);

		// Verify the output
		output = verifyOutput(output, greyImage);

		switch (thresholdMode) {
		default:
		case ONE_POINT:
			return onePoint(greyImage, output, thresholdLimit);

		case TWO_POINT:
			return twoPoint(greyImage, output, thresholdLimit, upperLimit);

		case PATTERN_DIFFUSION:
			return patternDiffusion(greyImage, output);

		case ERROR_DIFFUSION:
			return errorDiffusion(greyImage, output);
		}
	}

	/**
	 * Performs pattern diffusion on the input image.
	 * 
	 * @param image
	 *            the input image.
	 * @param output
	 *            the output image.
	 * 
	 * @return the output image.
	 */
	protected BufferedImage patternDiffusion(BufferedImage image,
			BufferedImage output) {
		Raster in_pixels = image.getRaster();
		WritableRaster out_pixels = output.getRaster();

		int[] dither = { 0, 128, 32, 160, 192, 64, 224, 96, 48, 176, 16, 144,
				240, 112, 208, 80 };

		int value;
		int final_i = image.getWidth();
		int final_j = image.getHeight();

		for (int j = 0; j < final_j; j++) {
			for (int i = 0; i < final_i; i++) {
				value = in_pixels.getSample(i, j, 0);

				if (value <= dither[((j % 4) * 4) + (i % 4)]) {
					out_pixels.setSample(i, j, 0, 0);
				} else {
					out_pixels.setSample(i, j, 0, 255);
				}
			}
		}

		return output;
	}

	/**
	 * Performs one-point thresholding.
	 * 
	 * @param image
	 *            the input image.
	 * @param output
	 *            the output image.
	 * @param thresholdLimit
	 *            the threshold limit.
	 * 
	 * @return the output image.
	 */
	protected BufferedImage onePoint(BufferedImage image, BufferedImage output,
			int thresholdLimit) {
		Raster in_pixels = image.getRaster();
		WritableRaster out_pixels = output.getRaster();

		int pixel;
		int w = image.getWidth();
		int h = image.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pixel = in_pixels.getSample(i, j, 0);

				if (pixel < thresholdLimit) {
					out_pixels.setSample(i, j, 0, 0);
				} else {
					out_pixels.setSample(i, j, 0, 255);
				}
			}
		}

		return output;
	}

	/**
	 * Perform two-point thresholding on the input image. This method is called
	 * within <code>filter</code>, which passes the lower and upper limits
	 * specified in <code>setThresholdLimits</code>.
	 * 
	 * @param image
	 *            the input image.
	 * @param output
	 *            the output image.
	 * @param lowerLimit
	 *            the lower limit.
	 * @param upperLimit
	 *            the upper limit.
	 * 
	 * @return the thresholded image.
	 * 
	 * @see #setThresholdLimits(int, int)
	 */
	protected BufferedImage twoPoint(BufferedImage image, BufferedImage output,
			int lowerLimit, int upperLimit) {
		Raster in_pixels = image.getRaster();
		WritableRaster out_pixels = output.getRaster();

		int pixel;
		int w = image.getWidth();
		int h = image.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pixel = in_pixels.getSample(i, j, 0);

				if ((pixel > lowerLimit) && (pixel < upperLimit)) {
					out_pixels.setSample(i, j, 0, 255);
				} else {
					out_pixels.setSample(i, j, 0, 0);
				}
			}
		}

		return output;
	}

	/**
	 * Performs error diffusion.
	 * 
	 * @param image
	 *            the input image.
	 * @param output
	 *            the output image.
	 * 
	 * @return the output image.
	 */
	protected BufferedImage errorDiffusion(BufferedImage image,
			BufferedImage output) {
		Raster in_pixels = output.getRaster();
		WritableRaster out_pixels = output.getRaster();
		image.copyData(out_pixels);

		int e = 0;
		int i = 0;
		int j = 0;
		int final_i = image.getWidth() - 1;
		int final_j = image.getHeight() - 1;

		int ed1; // Error diffusion
		int ed2; // Error diffusion
		int ed3; // Error diffusion
		int ed4; // Error diffusion
		int value;

		for (j = 0; j < final_j; j++) {
			value = in_pixels.getSample(i, j, 0);

			for (i = 1; i < final_i; i++) {
				if (value < 127) {
					out_pixels.setSample(i, j, 0, 0);
					e = value;
				} else {
					out_pixels.setSample(i, j, 0, 255);
					e = value - 255;
				}

				ed1 = in_pixels.getSample(i + 1, j, 0);
				ed2 = in_pixels.getSample(i - 1, j + 1, 0);
				ed3 = in_pixels.getSample(i, j + 1, 0);
				ed4 = in_pixels.getSample(i + 1, j + 1, 0);

				ed1 += ((7.0 * e) / 16.0);
				ed2 += ((3.0 * e) / 16.0);
				ed3 += ((5.0 * e) / 16.0);
				ed4 += ((1.0 * e) / 16.0);

				ed1 = (ed1 < 0) ? 0 : ((ed1 > 255) ? 255 : ed1);
				ed2 = (ed2 < 0) ? 0 : ((ed2 > 255) ? 255 : ed2);
				ed3 = (ed3 < 0) ? 0 : ((ed3 > 255) ? 255 : ed3);
				ed4 = (ed4 < 0) ? 0 : ((ed4 > 255) ? 255 : ed4);

				out_pixels.setSample(i + 1, j, 0, ed1);
				out_pixels.setSample(i - 1, j + 1, 0, ed2);
				out_pixels.setSample(i, j + 1, 0, ed3);
				out_pixels.setSample(i + 1, j + 1, 0, ed4);

				value = ed1;
			}

			i = 0;
		}

		return output;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String toString() {
		switch (thresholdMode) {
		default:
		case ONE_POINT:
			return "One-point threshold";

		case TWO_POINT:
			return "Two-point threshold";

		case PATTERN_DIFFUSION:
			return "Pattern diffusion thresholding";

		case ERROR_DIFFUSION:
			return "Error diffusion thresholding";
		}
	}
}