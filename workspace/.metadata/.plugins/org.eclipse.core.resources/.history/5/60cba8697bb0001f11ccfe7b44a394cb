package com.itba.imagenes.tp0;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itba.imagenes.ParamsReader;

public class Crop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParamsReader params = null;
		try {
			params = new ParamsReader(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(params.inputImageName1));
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedImage croppedImage = new BufferedImage(params.width,
				params.height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < params.width; i++) {
			for (int j = 0; j < params.height; j++) {
				croppedImage.setRGB(i, j, img.getRGB(i, j));
			}
		}

		try {
			ImageIO.write(croppedImage, params.imageFormatOutput, new File(
					params.outputImageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
