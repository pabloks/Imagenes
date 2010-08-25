package com.itba.imagenes.tp1;

import java.lang.reflect.Field;

public class ParamsReader {

	// Default param values if not passed by command line:
	public String inputImageName1 = "/Users/iandrono/Documents/ITBA/Imagenes/tps/tp0/input.jpg";
	public String inputImageName2 = "/Users/iandrono/Documents/ITBA/Imagenes/tps/tp0/input.jpg";
	public String outputImageName = "/Users/iandrono/Documents/ITBA/Imagenes/tps/tp0/output.jpg";
	public String outputImageFormat = "jpg";
	public int width = 100;
	public int height = 100;
	public int defaultRGB = 0x00FF00;

	ParamsReader(String[] args) throws NumberFormatException,
			IllegalArgumentException, IllegalAccessException {
		String split[] = null;
		for (String arg : args) {
			split = arg.split("=");
			if (split.length >= 2) {
				try {
					Field aField = ParamsReader.class.getField(split[0]);
					if (aField.getType() == int.class) {
						aField.setInt(this, new Integer(split[1]));
					} else if (aField.getType() == String.class) {
						aField.set(this, split[1]);
					}
				} catch (NoSuchFieldException e) {
					System.out.println("No such argument : " + split[0]
							+ ", Using default");
				}
			}
		}
		return;
	}
}