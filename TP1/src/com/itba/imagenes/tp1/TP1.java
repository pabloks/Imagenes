package com.itba.imagenes.tp1;

import java.awt.image.BufferedImage;

import com.itba.imagenes.ImageUtils;
import com.itba.imagenes.ParamsReader;

public class TP1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParamsReader params = null;
		try {
			ParamsReader.workPath += "tp1/";
			params = new ParamsReader(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedImage inputImage1 = params.loadNextImage();
		BufferedImage output = null;

		try {
			if (params.oper.equalsIgnoreCase("suma")
					|| params.oper.equalsIgnoreCase("resta")
					|| params.oper.equals("mult")) {
				BufferedImage inputImage2 = params.loadNextImage();
				output = ImageUtils.MathOperatorFunction(inputImage1,
						inputImage2, params.oper, params.scalar);
			} else if (params.oper.equalsIgnoreCase("scalar")) {
				output = ImageUtils.MathOperatorFunction(inputImage1, null,
						params.oper, params.scalar);
			} else if (params.oper.equalsIgnoreCase("rdinamico")) {
				output = ImageUtils.RangoDinamicoFunction(inputImage1,
						params.scalar);
			} else if (params.oper.equalsIgnoreCase("umbral")) {
				output = ImageUtils.UmbralFunction(inputImage1, params.scalar);
			} else if (params.oper.equalsIgnoreCase("negative")) {
				output = ImageUtils.negative(inputImage1);
			} else if (params.oper.equalsIgnoreCase("histogram")) {
				output = ImageUtils.getHistogram(inputImage1, params.color);
			} else if (params.oper.equalsIgnoreCase("equalization")) {
				output = ImageUtils.equalizate(inputImage1, params.color);
			} else if (params.oper.equalsIgnoreCase("grey")) {
				output = ImageUtils.blackAndWhite(inputImage1, params.color);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		params.saveImage(output);
	}
}
