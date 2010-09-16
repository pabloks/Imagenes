package com.itba.imagenes.tp2;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.itba.imagenes.ParamsReader;

public class RandomGenerator {

	static Random rand = new Random();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParamsReader params = null;
		try {
			ParamsReader.workPath += "tp2/";
			params = new ParamsReader(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedImage inputImage1 = params.loadNextImage();
		BufferedImage output = null;

		// output = noiseGauss(inputImage1, 0, 0.05);
		// output = noiseRayleigh(inputImage1, 0.5);
		// output = noiseExp(inputImage1, 0.05);
		output = noiseSaltPepper(inputImage1, 0.05, 0.95);

		params.saveImage(output);
	}

	public static BufferedImage noiseSaltPepper(BufferedImage img, double p0,
			double p1) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		double newRand;

		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);

				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;

				newRand = rand.nextDouble();
				if (newRand <= p0)
					newPixel = 0;
				else if (newRand >= p1)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static BufferedImage noiseGauss(BufferedImage img, double mean,
			double stddev) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);
				// Saco el gris
				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;
				newPixel += gaussianPDF(mean, stddev);

				if (newPixel < 0 || newPixel > 255)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static BufferedImage noiseRayleigh(BufferedImage img, double sigma) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);
				// Saco el gris
				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;
				newPixel *= nextRayleigh(sigma);

				if (newPixel < 0 || newPixel > 255)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	public static BufferedImage noiseExp(BufferedImage img, double lambda) {
		BufferedImage noised = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		double[] rgbPixels = new double[3];
		double newPixel;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.getRaster().getPixel(i, j, rgbPixels);
				// Saco el gris
				newPixel = (rgbPixels[0] + rgbPixels[1] + rgbPixels[2]) / 3;
				newPixel *= exp(lambda);

				if (newPixel < 0 || newPixel > 255)
					newPixel = 255;

				rgbPixels[0] = newPixel;
				rgbPixels[1] = newPixel;
				rgbPixels[2] = newPixel;

				noised.getRaster().setPixel(i, j, rgbPixels);
			}
		}

		return noised;
	}

	// Returns a Rayleigh pseudorandom deviate
	public static double nextRayleigh(double sigma) {
		Double ret = Math.sqrt(-2.0D
				* Math.log(1.0D - RandomGenerator.rand.nextDouble()))
				* sigma;

		ret = (ret / Math.pow(sigma, 2))
				* Math.exp((-Math.pow(ret, 2)) / (2 * Math.pow(sigma, 2)));

		return ret;
	}

	public static double gaussianPDF(double mean, double stddev) {
		double gauss = gaussian(mean, stddev);
		double ret = 0;

		ret = (1 / (stddev * Math.sqrt(2 * Math.PI)))
				* Math.exp((Math.pow(-gauss - mean, 2))
						/ (2 * Math.pow(stddev, 2)));

		return ret;
	}

	/**
	 * Return real number uniformly in [0, 1).
	 */
	public static double uniform() {
		return Math.random();
	}

	/**
	 * Return real number uniformly in [a, b).
	 */
	public static double uniform(double a, double b) {
		return a + Math.random() * (b - a);
	}

	/**
	 * Return an integer uniformly between 0 and N-1.
	 */
	public static int uniform(int N) {
		return (int) (Math.random() * N);
	}

	/**
	 * Return a boolean, which is true with probability p, and false otherwise.
	 */
	public static boolean bernoulli(double p) {
		return Math.random() < p;
	}

	/**
	 * Return a boolean, which is true with probability .5, and false otherwise.
	 */
	public static boolean bernoulli() {
		return bernoulli(0.5);
	}

	/**
	 * Return a real number with a standard Gaussian distribution.
	 */
	public static double gaussian() {
		// use the polar form of the Box-Muller transform
		double r, x, y;
		do {
			x = uniform(-1.0, 1.0);
			y = uniform(-1.0, 1.0);
			r = x * x + y * y;
		} while (r >= 1 || r == 0);
		return x * Math.sqrt(-2 * Math.log(r) / r);

		// Remark: y * Math.sqrt(-2 * Math.log(r) / r)
		// is an independent random gaussian
	}

	/**
	 * Return a real number from a gaussian distribution with given mean and
	 * stddev
	 */
	public static double gaussian(double mean, double stddev) {
		return mean + stddev * gaussian();
	}

	/**
	 * Return an integer with a geometric distribution with mean 1/p.
	 */
	public static int geometric(double p) {
		// using algorithm given by Knuth
		return (int) Math.ceil(Math.log(uniform()) / Math.log(1.0 - p));
	}

	/**
	 * Return an integer with a Poisson distribution with mean lambda.
	 */
	public static int poisson(double lambda) {
		// using algorithm given by Knuth
		// see http://en.wikipedia.org/wiki/Poisson_distribution
		int k = 0;
		double p = 1.0;
		double L = Math.exp(-lambda);
		do {
			k++;
			p *= uniform();
		} while (p >= L);
		return k - 1;
	}

	/**
	 * Return a real number with a Pareto distribution with parameter alpha.
	 */
	public static double pareto(double alpha) {
		return Math.pow(1 - uniform(), -1.0 / alpha) - 1.0;
	}

	/**
	 * Return a real number with a Cauchy distribution.
	 */
	public static double cauchy() {
		return Math.tan(Math.PI * (uniform() - 0.5));
	}

	/**
	 * Return a number from a discrete distribution: i with probability a[i].
	 */
	public static int discrete(double[] a) {
		// precondition: sum of array entries equals 1
		double r = Math.random();
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i];
			if (sum >= r)
				return i;
		}
		assert (false);
		return -1;
	}

	/**
	 * Return a real number from an exponential distribution with rate lambda.
	 */
	public static double exp(double lambda) {
		return -Math.log(1 - Math.random()) / lambda;
	}
}