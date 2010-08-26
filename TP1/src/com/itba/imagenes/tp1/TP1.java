package com.itba.imagenes.tp1;

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

		try {
			if (params.oper.equalsIgnoreCase("suma")
					|| params.oper.equalsIgnoreCase("resta")
					|| params.oper.equals("mult")
					|| params.oper.equalsIgnoreCase("scalar")) {
				MathOperators.MathOperatorFunction(params);
			} else if (params.oper.equalsIgnoreCase("rdinamico")) {
				RangoDinamico.RangoDinamicoFunction(params);
			} else if (params.oper.equalsIgnoreCase("umbral")) {
				Umbral.UmbralFunction(params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
