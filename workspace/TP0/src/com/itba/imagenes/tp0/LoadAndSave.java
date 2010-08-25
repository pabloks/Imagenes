package com.itba.imagenes.tp0;

import java.awt.image.BufferedImage;

import com.itba.imagenes.ImageHandler;
import com.itba.imagenes.ParamsReader;

public class LoadAndSave {

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
		img = ImageHandler.read(params.inputImageName1,
				params.imageFormatInput1, params);
		ImageHandler.write(img, params.imageFormatOutput,
				params.outputImageName);
	}
}
