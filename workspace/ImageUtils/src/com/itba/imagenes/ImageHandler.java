package com.itba.imagenes;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedImageAdapter;

@SuppressWarnings("restriction")
public class ImageHandler {

	public static BufferedImage read(String name, String format,
			ParamsReader params) {

		BufferedImage image = null;
		File imageFile = new File(name + "." + format);

		if (format.equalsIgnoreCase("raw")) {
			FileInputStream imageFileStream = null;

			try {
				imageFileStream = new FileInputStream(imageFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedImage img = new BufferedImage(params.width, params.height,
					BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < params.height; i++) {
				for (int j = 0; j < params.width; j++) {
					try {
						img.setRGB(j, i, imageFileStream.read());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			image = img;
		} else if (format.equalsIgnoreCase("pgm")
				|| format.equalsIgnoreCase("tif")) {
			RenderedImage rendimg = JAI.create("fileload", name + "." + format);
			BufferedImage bufferedImage = new RenderedImageAdapter(rendimg)
					.getAsBufferedImage();

			image = bufferedImage;
		} else {
			try {
				image = ImageIO.read(imageFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return image;
	}

	public static void write(BufferedImage img, String format, String name) {

		File outputImageFile = new File(name + "." + format);

		try {
			ImageIO.write(img, format, outputImageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}