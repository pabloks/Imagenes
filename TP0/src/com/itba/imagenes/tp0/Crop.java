package com.itba.imagenes.tp0;

import java.awt.image.BufferedImage;

import com.itba.imagenes.ParamsReader;

public class Crop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParamsReader params = null;
		try {
			ParamsReader.workPath += "tp0/";
			params = new ParamsReader(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedImage img = null;
		img = params.loadNextImage();

		BufferedImage croppedImage = new BufferedImage(params.width1,
				params.height1, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < params.width1; i++) {
			for (int j = 0; j < params.height1; j++) {
				croppedImage.setRGB(i, j, img.getRGB(i, j));
			}
		}

		params.saveImage(croppedImage);
	}
}
