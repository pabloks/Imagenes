/*
 * Filter.java
 * Created on 12 July 2004, 14:13
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
 * This class provides abstract functionality for image processing filter
 * operations.
 * <p/>
 * Please note that this, as with the rest of the this JDK, is not intended as
 * an all-inclusive, efficient or complete package. Instead, it is meant to
 * facilitate easy understanding of core concepts and rapid web-demo
 * development.
 * 
 * @author James Matthews
 */
public abstract class Filter {
	/**
	 * Perform a filter operation. Note that your filter method should provide
	 * the necessary functionality to allow users to either pass an output
	 * image, or have own dynamically created if null is passed. This can be
	 * easily achieved using the <code>verifyOutput</code> methods provided.
	 * 
	 * @param output
	 *            the pre-allocated output image (optional).
	 * @param image
	 *            the input image
	 * @return the output from the filter
	 * @see #verifyOutput(BufferedImage, BufferedImage)
	 */
	public abstract BufferedImage filter(BufferedImage image,
			BufferedImage output);

	/**
	 * Allows <code>filter</code> to be called with null as the output image by
	 * default. This was originally the way all calls to filter were achieved,
	 * but has been superceded with the new call to allow for more efficient
	 * handling of images.
	 * 
	 * @param image
	 *            the input image.
	 * @return the result of <code>filter(image, null)</code>.
	 */
	public BufferedImage filter(BufferedImage image) {
		return filter(image, null);
	}

	/**
	 * This method simply returns: <code>
	 * verifyOutput(output, input, input.getWidth(), input.getHeight(), input.getType());
	 * </code>
	 * <p/>
	 * See {@link #verifyOutput(BufferedImage, BufferedImage, int, int, int)}
	 * for full details.
	 * 
	 * @param output
	 *            the current output image.
	 * @param input
	 *            the input image.
	 * @return a valid output image.
	 */
	public BufferedImage verifyOutput(BufferedImage output, BufferedImage input) {
		return verifyOutput(output, input.getWidth(), input.getHeight(),
				input.getType());
	}

	/**
	 * This method simply returns: <code>
	 * verifyOutput(output, input, input.getWidth(), input.getHeight(), type);
	 * </code>
	 * <p/>
	 * See {@link #verifyOutput(BufferedImage, BufferedImage, int, int, int)}
	 * for full details.
	 * 
	 * @param output
	 *            the current output image.
	 * @param input
	 *            the input image.
	 * @param type
	 *            the desired image type.
	 * @return a valid output image.
	 */
	public BufferedImage verifyOutput(BufferedImage output,
			BufferedImage input, int type) {
		return verifyOutput(output, input.getWidth(), input.getHeight(), type);
	}

	/**
	 * This method is designed to allow filters to verify the output image
	 * passed. The current design of <code>Filter.filter</code> allows for an
	 * optional output image to be passed. If the output image is not null, and
	 * meets the specified requirements, it is returned. Otherwise, this method
	 * creates a valid image and returns that.
	 * <p/>
	 * For an output image to be valid, it must a) not be null and b) must equal
	 * the width, height and type passed. If these requirements are not met, a
	 * new, valid BufferedImage is returned.
	 * <p/>
	 * Additional methods are provided to check against an existing image (most
	 * likely the input to the filter), or an existing image and a new data
	 * type.
	 * 
	 * @param output
	 *            the current output
	 * @param width
	 *            the desired width.
	 * @param height
	 *            the desired height.
	 * @param type
	 *            the desired type.
	 * @return a valid output image.
	 */
	public BufferedImage verifyOutput(BufferedImage output, int width,
			int height, int type) {
		// If output is not null, and the types match, then ok.
		if ((output != null) && (output.getWidth() == width)
				&& (output.getHeight() == height) && (output.getType() == type)) {
			return output;
		}

		return new BufferedImage(width, height, type);
	}
}