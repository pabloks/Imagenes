package com.itba.imagenes.susan;

import java.awt.image.BufferedImage;

public class Susan {

	public static BufferedImage susan(BufferedImage img, float threshold,
			boolean isEdgeDetection) {
		BufferedImage newImage = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		int[][] mask = { { 0, 0, 1, 1, 1, 0, 0 }, { 0, 1, 1, 1, 1, 1, 0 },
				{ 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1, 0 },
				{ 0, 0, 1, 1, 1, 0, 0 } };

		// define the USAN area
		float nmax = 3 * 37 / 4;
		// float nmax = 37;

		int a = img.getWidth();
		int b = img.getHeight();
		double[] rgb = new double[3];
		double r0, pivot;
		int acum = 0;
		double s;

		// Arranco en (3,3) y llego hasta (width-4, height-4) para aplicarle la
		// mascara
		for (int i = 3; i < a - 4; i++) {
			for (int j = 3; j < b - 4; j++) {

				// El centro de la mascara siempre es i,j
				img.getRaster().getPixel(i, j, rgb);
				r0 = (rgb[0] + rgb[1] + rgb[2]) / 3;
				acum = 0;

				// Recorro toda la mascara:
				for (int k = 0; k < mask.length; k++) {
					for (int l = 0; l < mask[k].length; l++) {
						if (mask[k][l] == 1) {
							img.getRaster().getPixel(i + k - 3, j + l - 3, rgb);
							pivot = (rgb[0] + rgb[1] + rgb[2]) / 3;
							if (Math.abs(pivot - r0) < threshold)
								acum++;
						}
					}
				}

				System.out.println(acum);

				s = 1 - acum / nmax;
				// s -> 0 r0 no es borde
				// s -> 0.5 r0 es borde
				// s -> 0.75 r0 es esquina

				rgb[0] = 255;
				rgb[1] = 255;
				rgb[2] = 255;

				if ((isEdgeDetection && Math.abs(s - 0.5) < 0.1)
						|| (!isEdgeDetection && Math.abs(s - 0.75) < 0.1))
					newImage.getRaster().setPixel(i, j, rgb);
			}
		}
		return newImage;
	}
}
