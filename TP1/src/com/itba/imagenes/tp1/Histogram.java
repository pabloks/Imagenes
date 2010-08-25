package com.itba.imagenes.tp1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itba.imagenes.Color;
import com.itba.imagenes.Image;

enum TYPE {
	TIF, RAW, BMP, JPG
}



public class Histogram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	

	public static BufferedImage getImage(String Path, TYPE type) {
		BufferedImage img = null;

		try {
			if (type == TYPE.JPG)
				img = ImageIO.read(new File(Path));
			else if (type.equals(TYPE.RAW))
				// img = ImageIO.read ( new ByteArrayInputStream ( new
				// FileInputStream(new File(Path))) );
				;
			else if (type.equals(TYPE.BMP))
				;
			else
				img = ImageIO.read(new File(Path));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No pudo leerse le file");
		}

		return img;
	}



	public static boolean writeImage(BufferedImage img, String Path, TYPE type) {
		try {
			ImageIO.write(img, type.toString(), new File(Path));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
