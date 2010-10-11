/*
 * SobelEdgeDetector.java
 * Created on 12 July 2004, 17:53
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
 * This filter performs a simple edge detection algorithm (Sobel).
 * 
 * @author James Matthews
 */
public class SobelEdgeDetectorFilter extends Filter {
	/**
	 * Creates a new instance of SobelEdgeDetector
	 */
	public SobelEdgeDetectorFilter() {
	}

	/**
	 * Run the edge detection algorithm on the image passed. This filter will
	 * work on greyscale or RGB images.
	 * 
	 * @param image
	 *            the input image
	 * @param output
	 *            the output image (optional).
	 * 
	 * @return the image with the edges detected
	 */
	public BufferedImage filter(BufferedImage image, BufferedImage output) {
		output = verifyOutput(output, image);

		Raster in_pixels = image.getRaster();
		WritableRaster out_pixels = output.getRaster();

		short gc;
		int a;
		int b;
		int c;
		int d;
		int e;
		int f;
		int g;
		int h;
		int z;

		float sobscale = 1;
		float offsetval = 0;

		int i = 1;
		int j = 0;
		int final_i = image.getWidth() - 1;
		int final_j = image.getHeight() - 1;

		for (int bnd = 0; bnd < in_pixels.getNumBands(); bnd++) {
			for (j = 1; j < final_j; j++) {
				a = in_pixels.getSample(i - 1, j - 1, bnd);
				b = in_pixels.getSample(i, j - 1, bnd);
				d = in_pixels.getSample(i - 1, j, bnd);
				f = in_pixels.getSample(i - 1, j + 1, bnd);
				g = in_pixels.getSample(i, j + 1, bnd);
				z = in_pixels.getSample(i, j, bnd);

				for (i = 1; i < final_i; i++) {
					c = in_pixels.getSample(i + 1, j - 1, bnd);
					e = in_pixels.getSample(i + 1, j, bnd);
					h = in_pixels.getSample(i + 1, j + 1, bnd);

					int hor = (a + d + f) - (c + e + h); // The Sobel algorithm

					if (hor < 0) {
						hor = -hor;
					}

					int vert = (a + b + c) - (f + g + h);

					if (vert < 0) {
						vert = -vert;
					}

					gc = (short) (sobscale * (hor + vert));
					gc = (short) (gc + offsetval);

					gc = (short) ((gc > 255) ? 255 : gc);

					out_pixels.setSample(i, j, bnd, gc);

					a = b;
					b = c;
					d = z;
					f = g;
					g = h;
					z = e;
				}

				i = 1;
			}
		}

		return output;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String toString() {
		return "Sobel Edge Detector";
	}
}