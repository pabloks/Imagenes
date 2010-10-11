/*
 * HoughTransform.java
 * Created on 28 November 2004, 20:01
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

/**
 * This abstract class is for subclasses to implement different derivates of
 * Hough Transforms. Note that as this is more of an operation, not a filter,
 * this class does <i>not</i> implement the <code>Filter</code> interface.
 * 
 * @author James Matthews
 */
public abstract class HoughTransformOp {
	/**
	 * Creates a new instance of HoughTransform
	 */
	public HoughTransformOp() {
	}

	/**
	 * Run the Hough transform on the input image.
	 * 
	 * @param img
	 *            the input image.
	 */
	public abstract void run(BufferedImage img);

	/**
	 * Return a BufferedImage of the accumulator. How this is implemented is up
	 * to the programmer, since accumulators can be multidimensional.
	 * 
	 * @return the accumulator image.
	 */
	public abstract BufferedImage getAccumulatorImage();

	/**
	 * Return an image of the Hough Transform results superimposed on the input
	 * image.
	 * 
	 * @param img
	 *            the input image.
	 * @param threshold
	 *            the accumulator threshold.
	 * 
	 * @return the superimposed image.
	 */
	public abstract BufferedImage getSuperimposed(BufferedImage img,
			double threshold);
}