package com.itba.imagenes.fft;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ij.plugin.*;

import com.pearsoneduc.ip.op.FFTException;

public class MyFFT {
	static BufferedImage aux1 = null;
	static BufferedImage aux2 = null;
	static FFT_ auxFFT = null;
	
	public static void main(String[] args) {
		FFT fft = new FFT();
		
		fft.run("swap");
	}
	
	
	public static BufferedImage applyFFT(BufferedImage input)
	throws FFTException {

		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
		
				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
		auxFFT = new FFT_(img);
		auxFFT.transform();
		
		return auxFFT.getSpectrum();
	}
	
	public static BufferedImage reloadImage(BufferedImage input) throws FFTException{
		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
		
				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
//		FFT_ aux = new FFT_(img);
		auxFFT.copyReal(img);
		
		auxFFT.transform();
		
		return auxFFT.toImage(null);
		
	}
	
	public static BufferedImage getUnshiftedSpectrum(BufferedImage input)
	throws FFTException {

		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
		
				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
		FFT_ ifft = new FFT_(img);
		ifft.transform();
		return ifft.getUnshiftedSpectrum();
	}
	

	public static BufferedImage getSpectrum(BufferedImage input)
			throws FFTException {

		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {

				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
		FFT_ ifft = new FFT_(img);
		ifft.transform();
		return ifft.getSpectrum();
	}
	
	public static BufferedImage getMagnitud(BufferedImage input)
	throws FFTException {

		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
		
				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
		
		FFT_ ifft = new FFT_(img);
		ifft.transform();

		BufferedImage aux = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double[] auxv = new double[3];
		
		for (int i = 0; i < aux.getWidth(); i++) {
			for (int j = 0; j < aux.getHeight(); j++) {
		
				auxv[0] = auxv[1] = auxv[2] = ifft.getMagnitude(i, j);
				aux.getRaster().setPixel(i, j, auxv);
			}
		}
		
		return aux;
	}

	
	public static BufferedImage getPhase(BufferedImage input)
	throws FFTException {

		BufferedImage img = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
		
				img.setRGB(i, j, input.getRGB(i, j));
			}
		}
		
		FFT_ ifft = new FFT_(img);
		ifft.transform();

		BufferedImage aux = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double[] auxv = new double[3];
		
		for (int i = 0; i < aux.getWidth(); i++) {
			for (int j = 0; j < aux.getHeight(); j++) {
		
				auxv[0] = auxv[1] = auxv[2] = ifft.getPhase(i, j);
				aux.getRaster().setPixel(i, j, auxv);
			}
		}
		
		return aux;
	}

	public static BufferedImage getAux1(){
		return aux1;
	}
	
	public static BufferedImage getAux2(){
		return aux2;
	}

	public static void mixImages(BufferedImage input1, BufferedImage input2) throws FFTException, IOException {

		// FFT to input1
		BufferedImage img1 = new BufferedImage(input1.getWidth(),
				input1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < input1.getWidth(); i++) {
			for (int j = 0; j < input1.getHeight(); j++) {

				img1.setRGB(i, j, input1.getRGB(i, j));
			}
		}
		
		FFT_ ifft1 = new FFT_(img1);
		ifft1.transform();

		// FFT to input2
		BufferedImage img2 = new BufferedImage(input2.getWidth(),
				input2.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < input2.getWidth(); i++) {
			for (int j = 0; j < input2.getHeight(); j++) {
				img2.setRGB(i, j, input2.getRGB(i, j));
			}
		}
		
		FFT_ ifft2 = new FFT_(img2);
		FFT_ auxf2 =  new FFT_(img2);
		ifft2.transform();
		auxf2.transform();

//		// Set ifft1 phase to ifft2
		for (int i = 0; i < input2.getWidth(); i++) {
			for (int j = 0; j < input2.getHeight(); j++) {
				ifft2.setPhase(i, j, ifft1.getPhase(i, j));
			}
		}
//		
		// Set ifft2 phase to ifft1
		for (int i = 0; i < input1.getWidth(); i++) {
			for (int j = 0; j < input1.getHeight(); j++) {
				ifft1.setPhase(i, j, auxf2.getPhase(i, j));
			}
		}
		
		ifft2.transform();
		ifft1.transform();
		
		aux1 = ifft1.toImage(null);
		aux2 = ifft2.toImage(null);
	}
}