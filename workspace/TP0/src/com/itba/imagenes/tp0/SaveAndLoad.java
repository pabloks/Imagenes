package com.itba.imagenes.tp0;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itba.imagenes.ParamsReader;

public class SaveAndLoad {

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
		try {
			img = ImageIO.read(new File(params.inputImageName1 + "."
					+ params.imageFormatInput1));
			ImageIO.write(img, params.imageFormatOutput, new File(
					params.outputImageName + "." + params.imageFormatOutput));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
