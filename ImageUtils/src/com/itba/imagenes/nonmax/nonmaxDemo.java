package com.itba.imagenes.nonmax;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import com.itba.imagenes.hough.sobel;

public class nonmaxDemo extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image edgeImage, accImage, outputImage;
	MediaTracker tracker = null;
	PixelGrabber grabber = null;
	int width = 0, height = 0;
	String fileNames[] = { "/Users/iandrono/Documents/ITBA/Imagenes/Imagenes/TP2/lena.png" };

	javax.swing.Timer timer;
	static int mode = 1;

	int imageNumber = 0;
	static int progress = 0;
	static int orig[] = null;

	Image image[] = new Image[fileNames.length];

	JProgressBar progressBar;
	JPanel selectionPanel, controlPanel, imagePanel, progressPanel;
	JLabel origLabel, outputLabel, modeLabel, comboLabel, sigmaLabel,
			processing;
	JComboBox imSel;
	static sobel edgedetector;
	JRadioButton origRadio, edgeRadio, threshRadio;
	ButtonGroup radiogroup;

	static nonmax nonmaxOp;
	static Image edges;

	// Applet init function
	public void init() {

		tracker = new MediaTracker(this);
		for (int i = 0; i < fileNames.length; i++) {
			image[i] = getImage(this.getCodeBase(), fileNames[i]);
			image[i] = image[i].getScaledInstance(256, 256, Image.SCALE_SMOOTH);
			tracker.addImage(image[i], i);
		}
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			System.out.println("error: " + e);
		}

		Container cont = getContentPane();
		cont.removeAll();
		cont.setBackground(Color.black);
		cont.setLayout(new BorderLayout());

		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 4, 15, 0));
		controlPanel.setBackground(new Color(192, 204, 226));
		imagePanel = new JPanel();
		imagePanel.setBackground(new Color(192, 204, 226));
		progressPanel = new JPanel();
		progressPanel.setBackground(new Color(192, 204, 226));
		progressPanel.setLayout(new GridLayout(2, 1));

		comboLabel = new JLabel("IMAGE");
		comboLabel.setHorizontalAlignment(JLabel.CENTER);
		controlPanel.add(comboLabel);
		modeLabel = new JLabel("Mode");
		modeLabel.setHorizontalAlignment(JLabel.CENTER);
		controlPanel.add(modeLabel);

		processing = new JLabel("Processing...");
		processing.setHorizontalAlignment(JLabel.LEFT);
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true); // get space for the string
		progressBar.setString(""); // but don't paint it
		progressPanel.add(processing);
		progressPanel.add(progressBar);

		width = image[imageNumber].getWidth(null);
		height = image[imageNumber].getHeight(null);

		imSel = new JComboBox(fileNames);
		imageNumber = imSel.getSelectedIndex();
		imSel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageNumber = imSel.getSelectedIndex();
				origLabel.setIcon(new ImageIcon(image[imageNumber]));
				processImage();
			}
		});
		controlPanel.add(imSel, BorderLayout.PAGE_START);

		timer = new javax.swing.Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (mode == 1)
					progressBar.setValue(nonmaxOp.getProgress());
				else
					progressBar.setValue((edgedetector.getProgress() + nonmaxOp
							.getProgress()) / 2);
			}
		});

		origLabel = new JLabel("Image", new ImageIcon(image[imageNumber]),
				JLabel.CENTER);
		origLabel.setVerticalTextPosition(JLabel.BOTTOM);
		origLabel.setHorizontalTextPosition(JLabel.CENTER);
		origLabel.setForeground(Color.blue);
		imagePanel.add(origLabel);

		outputLabel = new JLabel("Non-Maximum Suppressed", new ImageIcon(
				image[imageNumber]), JLabel.CENTER);
		outputLabel.setVerticalTextPosition(JLabel.BOTTOM);
		outputLabel.setHorizontalTextPosition(JLabel.CENTER);
		outputLabel.setForeground(Color.blue);
		imagePanel.add(outputLabel);

		origRadio = new JRadioButton("Original");
		origRadio.setActionCommand("original");
		origRadio.setBackground(new Color(192, 204, 226));
		origRadio.setSelected(true);

		edgeRadio = new JRadioButton("Edge");
		edgeRadio.setActionCommand("edge");
		edgeRadio.setBackground(new Color(192, 204, 226));

		threshRadio = new JRadioButton("Threshold");
		threshRadio.setActionCommand("threshold");
		threshRadio.setBackground(new Color(192, 204, 226));

		radiogroup = new ButtonGroup();

		radiogroup.add(origRadio);
		radiogroup.add(edgeRadio);
		radiogroup.add(threshRadio);

		origRadio.addActionListener(new radiolistener());
		edgeRadio.addActionListener(new radiolistener());
		threshRadio.addActionListener(new radiolistener());

		selectionPanel = new JPanel();
		selectionPanel.setBackground(new Color(192, 204, 226));
		selectionPanel.add(origRadio);
		selectionPanel.add(edgeRadio);
		selectionPanel.add(threshRadio);
		controlPanel.add(selectionPanel);

		cont.add(controlPanel, BorderLayout.NORTH);
		cont.add(imagePanel, BorderLayout.CENTER);
		cont.add(progressPanel, BorderLayout.SOUTH);

		processImage();

	}

	class radiolistener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "original")
				mode = 1;
			if (e.getActionCommand() == "edge")
				mode = 2;
			if (e.getActionCommand() == "threshold")
				mode = 3;
			processImage();
		}
	}

	public int[] threshold(int[] original, int value) {
		for (int x = 0; x < original.length; x++) {
			if ((original[x] & 0xff) >= value)
				original[x] = 0xffffffff;
			else
				original[x] = 0xff000000;
		}
		return original;
	}

	private void processImage() {
		orig = new int[width * height];
		PixelGrabber grabber = new PixelGrabber(image[imageNumber], 0, 0,
				width, height, orig, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}
		progressBar.setMaximum(width - 4);

		processing.setText("Processing...");
		imSel.setEnabled(false);
		origRadio.setEnabled(false);
		edgeRadio.setEnabled(false);
		threshRadio.setEnabled(false);
		edgedetector = new sobel();
		nonmaxOp = new nonmax();
		timer.start();

		new Thread() {
			public void run() {
				edgedetector.init(orig, width, height);
				if (mode > 1)
					orig = edgedetector.process();
				if (mode == 3)
					orig = threshold(orig, 50);

				edges = image[imageNumber];
				createImage(new MemoryImageSource(width, height, orig, 0, width));
				origLabel.setIcon(new ImageIcon(
						createImage(new MemoryImageSource(width, height, orig,
								0, width))));
				nonmaxOp.init(orig, width, height);
				orig = nonmaxOp.process();
				final Image output = createImage(new MemoryImageSource(width,
						height, orig, 0, width));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						outputLabel.setIcon(new ImageIcon(output));
						processing.setText("Done");

						imSel.setEnabled(true);
						origRadio.setEnabled(true);
						edgeRadio.setEnabled(true);
						threshRadio.setEnabled(true);
					}
				});
			}
		}.start();
	}

}