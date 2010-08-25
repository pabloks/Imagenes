package com.itba.imagenes.tp1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

enum TYPE{
	TIF, RAW, BMP;
}

public class Image {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage a = Image.getImage("/home/pabloks/workspace/Imagenes/workspace/TP1/images/", TYPE.TIF);
		
	}
	
	public static BufferedImage getImage(String Path, TYPE type ){
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File(Path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No pudo leerse le file");
		}
		
		return img;
	}

}
