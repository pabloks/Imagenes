package com.itba.imagenes.fft;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.op.ImageFFT;

public class MyFFT {

	public static BufferedImage applyFFT(BufferedImage input)
			throws FFTException {

		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {

				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
		ImageFFT ifft = new ImageFFT(img);
		ifft.transform();
		return ifft.getSpectrum();
	}

	public static BufferedImage mixImages() throws FFTException, IOException {

		BufferedImage input1 = ImageIO
				.read(new File(
						"/Users/iandrono/Documents/ITBA/Imagenes/Imagenes/TP2/cln1.gif"));
		BufferedImage input2 = ImageIO
				.read(new File(
						"/Users/iandrono/Documents/ITBA/Imagenes/Imagenes/TP2/ftw.jpg"));

		// FFT to input1
		BufferedImage img1 = new BufferedImage(input1.getWidth(),
				input1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < input1.getWidth(); i++) {
			for (int j = 0; j < input1.getHeight(); j++) {

				img1.setRGB(i, j, input1.getRGB(i, j));
			}
		}
		ImageFFT ifft1 = new ImageFFT(img1);
		ifft1.transform();

		// FFT to input2
		BufferedImage img2 = new BufferedImage(input2.getWidth(),
				input2.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < input2.getWidth(); i++) {
			for (int j = 0; j < input2.getHeight(); j++) {

				img2.setRGB(i, j, input2.getRGB(i, j));
			}
		}
		ImageFFT ifft2 = new ImageFFT(img1);
		ifft2.transform();

		// Set ifft1 phase to ifft2
		for (int i = 0; i < input2.getWidth(); i++) {
			for (int j = 0; j < input2.getHeight(); j++) {
				ifft2.setPhase(i, j, ifft1.getPhase(i, j));
			}
		}
		// ifft2.transform();
		return ifft2.toImage(null);
	}
}