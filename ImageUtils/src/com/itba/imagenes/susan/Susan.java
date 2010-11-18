package com.itba.imagenes.susan;

import java.awt.image.BufferedImage;

public class Susan {

	public static BufferedImage susan(BufferedImage inputImage, final float t,
			final boolean detectEdge) {

		final int dim = 7;
		final int diff = dim / 2;
		final int max = diff + 1;
		final float[][] mask = new float[][] { { 0, 0, 1, 1, 1, 0, 0 },
				{ 0, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 },
				{ 0, 1, 1, 1, 1, 1, 0 }, { 0, 0, 1, 1, 1, 0, 0 }, };

		final double[] gray = { 125, 125, 125 };
		// final double[] white = { 255, 255, 255 };
		final double[] red = { 255, 0, 0 };
		double[] rgb = new double[3];

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		final BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// final Image newImage = new Image(getWidth(), getHeight(),
		// Color.BLACK);
		height = inputImage.getHeight() - diff;
		for (int j = diff; j < height; ++j) {
			width = inputImage.getWidth() - diff;
			for (int i = diff; i < width; ++i) {
				inputImage.getRaster().getPixel(i, j, rgb);
				Color center = new com.itba.imagenes.susan.Color(
						(float) rgb[0], (float) rgb[1], (float) rgb[2]);

				Color result = new Color(0);
				for (int v = -diff; v < max; ++v) {
					for (int u = -diff; u < max; ++u) {
						float value = mask[diff + v][diff + u];
						if (value == 0.0)
							continue;

						inputImage.getRaster().getPixel(i + u, j + v, rgb);
						Color pixel = new com.itba.imagenes.susan.Color(
								(float) rgb[0], (float) rgb[1], (float) rgb[2]);
						// System.out.println("value:" + value + " pixel:" +
						// pixel.red + " center:" + center.red + " diff:" +
						// Math.abs((value * pixel.red) - center.red));
						result.red += (Math.abs((value * pixel.red)
								- center.red) < t) ? 1.0 : 0.0;
						result.green += (Math.abs((value * pixel.green)
								- center.green) < t) ? 1.0 : 0.0;
						result.blue += (Math.abs((value * pixel.blue)
								- center.blue) < t) ? 1.0 : 0.0;
					}
				}
				// System.out.println(result);
				final int x = i;
				final int y = j;
				result.transformColor(new Color.ChannelTransformation() {
					@Override
					public float transform(float value, Channel channel) {
						value = 1.0f - (value / 37.0f);
						// System.out.println(value);
						if (detectEdge && Math.abs(value - 0.5) < 0.05) {
							newImage.getRaster().setPixel(x, y, gray);
							// System.out.println("BORDE " + Math.abs(value -
							// 0.5));
						} else if (!detectEdge && Math.abs(value - 0.75) < 0.05) {
							// System.out.println("ESQUINA " + Math.abs(value -
							// 0.75));
							newImage.getRaster().setPixel(x, y, red);
						}
						return value;
					}
				}, Channel.ALL);
			}
		}
		return newImage;

	}
}
