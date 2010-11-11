package com.itba.imagenes.susan;

public class Color {

	public static final Color BLACK = new Color(0);
	public static final Color WHITE = new Color(255);

	public float red;
	public float green;
	public float blue;

	public Color(final float red, final float green, final float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(final float gray) {
		this.red = gray;
		this.green = gray;
		this.blue = gray;
	}

	public float getRed() {
		return red;
	}

	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}

	public Color convertToGray() {
		return new Color((float) Math.sqrt(red * red + green * green + blue
				* blue));
	}

	@Override
	public String toString() {
		return "Color [red=" + red + ", green=" + green + ", blue=" + blue
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(blue);
		result = prime * result + Float.floatToIntBits(green);
		result = prime * result + Float.floatToIntBits(red);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color other = (Color) obj;
		if (Float.floatToIntBits(blue) != Float.floatToIntBits(other.blue))
			return false;
		if (Float.floatToIntBits(green) != Float.floatToIntBits(other.green))
			return false;
		if (Float.floatToIntBits(red) != Float.floatToIntBits(other.red))
			return false;
		return true;
	}

	interface ChannelTransformation {
		public float transform(final float value, final Channel channel);
	}

	Color transformColor(final ChannelTransformation transformation,
			final Channel channel) {
		switch (channel) {
		case RED:
			red = transformation.transform(red, Channel.RED);
			break;
		case GREEN:
			green = transformation.transform(green, Channel.GREEN);
			break;
		case BLUE:
			blue = transformation.transform(blue, Channel.BLUE);
			break;
		case BRIGHTNESS:
			red = transformation.transform(red, Channel.BRIGHTNESS);
			green = red;
			blue = red;
			break;
		case ALL:
			red = transformation.transform(red, Channel.RED);
			green = transformation.transform(green, Channel.GREEN);
			blue = transformation.transform(blue, Channel.BLUE);
			break;
		default:
			break;
		}
		return this;
	}
}