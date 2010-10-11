/*
 * LineHoughTransformOp.java
 * Created on 03 December 2004, 01:45
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * This class implements a Line Hough transform. It is heavily based on code
 * written by Timothy Sharman for the excellent HIPR2 resource site
 * (http://homepages.inf.ed.ac.uk/rbf/HIPR2/hipr_top.htm).
 * 
 * @author James Matthews
 * @author Timothy Sharman
 */
public class LineHoughTransformOp extends HoughTransformOp {
	/** The accumulator. */
	protected long[][] houghAccumulator;

	/** The maximum accumulator value. */
	protected long maxAccValue = -1;

	/** The local peak neighbourhood. */
	protected int localPeakNeighbourhood = 5;

	/** The theta step. */
	protected double thetaStep = 0.0;

	/** The line colour used for the superimposed image (default WHITE). */
	protected Color lineColor = Color.WHITE;

	/**
	 * Creates a new instance of LineHoughTransformOp
	 */
	public LineHoughTransformOp() {
	}

	/**
	 * Set the line colour used for the superimposed image.
	 * 
	 * @param lc
	 *            the new line colour.
	 */
	public void setLineColor(Color lc) {
		lineColor = lc;
	}

	/**
	 * Get the line colour used.
	 * 
	 * @return the current line colour used.
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * Set the local peak neighbourhood.
	 * 
	 * @param lpn
	 *            the new local peak neighbourhood.
	 */
	public void setLocalPeakNeighbourhood(int lpn) {
		localPeakNeighbourhood = lpn;
	}

	/**
	 * Get the local peak neighbourhood.
	 * 
	 * @return the current local peak neighbourhood.
	 */
	public int getLocalPeakNeighbourhood() {
		return localPeakNeighbourhood;
	}

	/**
	 * Get the maximum value in the accumulator.
	 * 
	 * @return the maximum value.
	 */
	protected long getMaximum() {
		if (houghAccumulator == null) {
			return -1;
		}

		long accMax = Long.MIN_VALUE;

		for (int i = 0; i < houghAccumulator.length; i++) {
			for (int j = 0; j < houghAccumulator[0].length; j++) {
				if (houghAccumulator[i][j] > accMax) {
					accMax = houghAccumulator[i][j];
				}
			}
		}

		return accMax;
	}

	/**
	 * Returns the accumulator as an image. Values are scaled linearly according
	 * to the maximum value.
	 * 
	 * @return the accumulator image.
	 */
	public java.awt.image.BufferedImage getAccumulatorImage() {
		// throw exception?
		if (houghAccumulator == null) {
			return null;
		}

		double scaleFactor = 255 / maxAccValue;

		BufferedImage acc = new BufferedImage(houghAccumulator.length,
				houghAccumulator[0].length, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster output = acc.getRaster();

		for (int i = 0; i < houghAccumulator.length; i++)
			for (int j = 0; j < houghAccumulator[0].length; j++)
				output.setSample(i, j, 0,
						(int) (houghAccumulator[i][j] * scaleFactor));

		return acc;
	}

	/**
	 * Runs the line Hough Transform. Note that the input image must be an
	 * edge-only, greyscale image (ie, must have been passed through an edge
	 * detector and thresholding filter).
	 * 
	 * @param img
	 *            input image.
	 * 
	 * @throws IllegalArgumentException
	 *             DOCUMENT ME!
	 */
	public void run(BufferedImage img) {
		if (img.getType() != BufferedImage.TYPE_BYTE_GRAY) {
			throw new IllegalArgumentException("input image must be greyscale.");
		}

		int width = img.getWidth();
		int height = img.getHeight();

		Raster source = img.getRaster();

		int tmp = Math.max(width, height);
		int h_h = (int) (Math.sqrt(2) * tmp);
		int h_w = 180;

		houghAccumulator = new long[h_w][2 * h_h];
		thetaStep = Math.PI / h_w;

		int src_rgb;
		int centre_x = width / 2;
		int centre_y = height / 2;

		for (int i = 0; i < h_w; i++) {
			for (int j = 0; j < (2 * h_h); j++) {
				houghAccumulator[i][j] = 0;
			}
		}

		// Now find edge points and update hough array
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				src_rgb = source.getSample(i, j, 0);

				if (src_rgb != 0) {
					// Edge pixel found
					for (int k = 0; k < h_w; k++) {
						// Work out the r values for each theta step
						tmp = (int) (((i - centre_x) * Math.cos(k * thetaStep)) + ((j - centre_y) * Math
								.sin(k * thetaStep)));

						// Move all values into positive range for display
						// purposes
						// if (tmp== -1 && k ==150 )
						// System.out.println("LOW  ij: "+i+" "+j);
						// if (tmp== 0 && k ==150 )
						// System.out.println("ON   ij: "+i+" "+j);
						// if (tmp== 1 && k ==150 )
						// System.out.println("HIGH ij: "+i+" "+j);
						tmp = tmp + h_h;

						if ((tmp < 0) || (tmp >= (2 * h_h))) {
							continue;
						}

						// Increment hough array
						houghAccumulator[k][tmp]++;
					}
				}
			}
		}
	}

	/**
	 * Returns the superimposed image. This can be a full-colour version of the
	 * image the Hough transform has been run on.
	 * 
	 * @param img
	 *            the input image.
	 * @param threshold
	 *            the accumulator threshold.
	 * 
	 * @return the superimposed image.
	 */
	@SuppressWarnings("unused")
	public BufferedImage getSuperimposed(BufferedImage img, double threshold) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),
				img.getType());
		out.setData(img.copyData(null));

		WritableRaster output = out.getRaster();
		Graphics2D g2d = (Graphics2D) out.getGraphics();

		g2d.setColor(lineColor);

		// if (aa == true) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// }
		maxAccValue = getMaximum();

		long thresh = (int) (threshold * maxAccValue);

		// Search for local peaks above threshold to draw
		boolean draw = false;
		int k;
		int l;
		int dt; // test theta
		int dr; // test offset
		int width = img.getWidth();
		int height = img.getHeight();
		int centre_x = width / 2;
		int centre_y = height / 2;

		int tmp = Math.max(width, height);
		int hh = (int) (Math.sqrt(2) * tmp);
		int hw = 180;

		for (int i = 0; i < hw; i++) {
			for (int j = 0; j < (2 * hh); j++) {
				if (houghAccumulator[i][j] >= thresh) {
					// This is HORRIBLY inefficient, if another local peak is
					// found,then that is going to be the line that needs to
					// be drawn...why check it hundreds more times?
					if (localPeak(i, j, hw, hh, localPeakNeighbourhood) == false) {
						continue;
					}

					int x1;
					int x2;
					int y1;
					int y2;
					double tsin = Math.sin(i * thetaStep);
					double tcos = Math.cos(i * thetaStep);

					if ((i <= (hw / 4)) || (i >= ((3 * hw) / 4))) {
						y1 = 0;
						x1 = (int) (((j - hh) - ((y1 - centre_y) * tsin)) / tcos)
								+ centre_x;
						y2 = height - 1;
						x2 = (int) (((j - hh) - ((y2 - centre_y) * tsin)) / tcos)
								+ centre_x;
					} else {
						x1 = 0;
						y1 = (int) (((j - hh) - ((x1 - centre_x) * tcos)) / tsin)
								+ centre_y;
						x2 = width - 1;
						y2 = (int) (((j - hh) - ((x2 - centre_x) * tcos)) / tsin)
								+ centre_y;
					}

					g2d.drawLine(x1, y1, x2, y2);
				}
			}
		}

		return out;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param i
	 *            DOCUMENT ME!
	 * @param j
	 *            DOCUMENT ME!
	 * @param hw
	 *            DOCUMENT ME!
	 * @param hh
	 *            DOCUMENT ME!
	 * @param neighbourhood
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	private boolean localPeak(int i, int j, int hw, int hh, int neighbourhood) {
		int dt;
		int dr;
		long peak = houghAccumulator[i][j];

		for (int k = -neighbourhood; k <= neighbourhood; k++) {
			for (int l = -neighbourhood; l <= neighbourhood; l++) {
				if ((k == 0) && (l == 0)) {
					continue;
				}

				dt = i + k;
				dr = j + l;

				if ((dr < 0) || (dr >= (2 * hh))) {
					continue;
				}

				if (dt < 0) {
					dt = dt + hw;
				}

				if (dt >= hw) {
					dt = dt - hw;
				}

				if (houghAccumulator[dt][dr] > peak) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Simple test function.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err
					.println("usage: java LineHoughTransform <input> {acc-threshold} {local-peak}");
			return;
		}

		try {
			BufferedImage in = javax.imageio.ImageIO.read(new java.io.File(
					args[0]));

			GreyscaleFilter s1 = new GreyscaleFilter();
			SobelEdgeDetectorFilter s2 = new SobelEdgeDetectorFilter();
			ThresholdFilter s3 = new ThresholdFilter();

			BufferedImage in1 = s1.filter(in); // to greyscale
			BufferedImage in2 = s2.filter(in1);
			BufferedImage in3 = s3.filter(in2);

			LineHoughTransformOp hough = new LineHoughTransformOp();

			double accRatio = (args.length > 1) ? Double.parseDouble(args[1])
					: 0.25d;
			int lpn = (args.length > 2) ? Integer.parseInt(args[2]) : 7;

			hough.setLocalPeakNeighbourhood(lpn);
			hough.run(in3);
			hough.setLineColor(Color.RED);

			javax.imageio.ImageIO.write(in2, "png", new java.io.File(
					"houghSobel.png"));
			javax.imageio.ImageIO.write(in3, "png", new java.io.File(
					"houghInput.png"));
			javax.imageio.ImageIO.write(hough.getAccumulatorImage(), "png",
					new java.io.File("houghAccumulator.png"));
			javax.imageio.ImageIO.write(hough.getSuperimposed(in, accRatio),
					"png", new java.io.File("houghSuperimposed.png"));
		} catch (java.io.IOException e) {
			System.err.println(e);
		}
	}
}