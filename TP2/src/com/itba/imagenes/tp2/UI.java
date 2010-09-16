package com.itba.imagenes.tp2;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.itba.imagenes.Color;
import com.itba.imagenes.ImageMask;
import com.itba.imagenes.ImageUtils;

// do not import the package when you are already in it
// everything inside the package is accessible as-is
//import ntu.ac.uk.visualization.*;

public class UI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel lblname = null;
	private JMenuBar jJMenuBar = null;
	private JMenu menuFile = null;
	private JMenuItem menuItemOpen = null;
	private JMenuItem menuItemClose = null;
	private JButton btnActionjButton1 = null;
	private JButton btnActionjButtonBW = null;
	private JButton btnActionjButtonHist = null;
	private JButton btnActionjButtonFilt3 = null;
	private JButton btnActionjButtonFilt3l = null;
	private JButton btnActionjButtonFilt5 = null;
	private JButton btnActionjButtonFilt52l = null;
	private JButton btnActionjButtonFilt52h = null;

	private JButton btnActionjButtonGauss = null;
	private JButton btnActionjButtonRay = null;
	private JButton btnActionjButtonExp = null;
	private JButton btnActionjButtonSalt = null;

	private JPanel jContentPane1 = null;
	private JTextField PathImage = null;
	private BufferedImage _image;
	private BufferedImage _lastImage;
	private boolean useOriginal = true;
	String namePathImage = null;
	JFileChooser chooser = new JFileChooser(".");

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @throws IOException
	 * @throws IOException
	 */
	private JPanel getJContentPane() throws IOException {
		if (jContentPane == null) {
			lblname = new JLabel();
			lblname.setBounds(new Rectangle(19, 10, 168, 16));
			lblname.setText("Choose an image");
			jContentPane = new JPanel();

			jContentPane.setLayout(null);
			jContentPane.add(lblname, null);
			jContentPane.add(getBtnActionjButton1(), null);
			jContentPane.add(getBtnActionjButtonBW(), null);
			jContentPane.add(getBtnActionjButtonHist(), null);
			jContentPane.add(getBtnActionjButtonFilt3high(), null);
			jContentPane.add(getBtnActionjButtonFilt3low(), null);
			jContentPane.add(getBtnActionjButtonFilt5high(), null);
			jContentPane.add(getBtnActionjButtonFilt5low(), null);
			jContentPane.add(getBtnActionjButtonFilt52high(), null);
			jContentPane.add(getBtnActionjButtonFilt52low(), null);

			jContentPane.add(btnActionjButtonGauss(), null);
			jContentPane.add(btnActionjButtonRay(), null);
			jContentPane.add(btnActionjButtonExp(), null);
			jContentPane.add(btnActionjButtonSalt(), null);
			jContentPane.add(getBtnActionjButtonMean(), null);
			

			setRadios(jContentPane);

			// jContentPane.add(jLabel, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getMenuFile());
		}
		return jJMenuBar;
	}

	private BufferedImage getSelectedImage() {
		if (useOriginal)
			return _image;

		return _lastImage;
	}

	private void setRadios(JPanel panel) {
		// Create the radio buttons.
		JRadioButton originalButton = new JRadioButton("Original Image");
		originalButton.setMnemonic('o');
		originalButton.setActionCommand("original");
		originalButton.setSelected(true);
		originalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				useOriginal = true;
			}
		});
		originalButton.setBounds(20, 60, 200, 15);

		JRadioButton lastButton = new JRadioButton("Last Transform");
		lastButton.setMnemonic('l');
		lastButton.setActionCommand("last");
		lastButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				useOriginal = false;
			}
		});
		lastButton.setBounds(20, 80, 200, 15);

		ButtonGroup group = new ButtonGroup();
		group.add(originalButton);
		group.add(lastButton);

		panel.add(originalButton, null);
		panel.add(lastButton, null);
	}

	/**
	 * This method initializes menuFile
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new JMenu();
			menuFile.setText("File");
			menuFile.add(getMenuItemOpen());
			menuFile.add(getMenuItemClose());
		}
		return menuFile;
	}

	/**
	 * This method initializes menuItemOpen
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuItemOpen() {
		if (menuItemOpen == null) {
			menuItemOpen = new JMenuItem();
			menuItemOpen.setText("Open");
			menuItemOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed() by Open");
					// TODO Auto-generated Event stub actionPerformed()
					openDialog();
				}
			});
		}
		return menuItemOpen;
	}

	/**
	 * This method initializes menuItemClose
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuItemClose() {
		if (menuItemClose == null) {
			menuItemClose = new JMenuItem();
			menuItemClose.setText("Close");
			menuItemClose
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed()");
							// TODO Auto-generated Event stub actionPerformed()
							System.exit(0);
						}
					});
		}
		return menuItemClose;
	}

	private JButton getBtnActionjButtonBW() {
		if (btnActionjButtonBW == null) {
			btnActionjButtonBW = new JButton();
			btnActionjButtonBW.setBounds(new Rectangle(301, 10, 140, 16));
			btnActionjButtonBW.setText("Black And White");
			btnActionjButtonBW
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							_lastImage = ImageUtils.blackAndWhite(
									getSelectedImage(), Color.RED.toString());
							openImage(_lastImage, "Black And White");
						}
					});
		}
		return btnActionjButtonBW;
	}

	private JButton getBtnActionjButtonHist() {
		if (btnActionjButtonHist == null) {
			btnActionjButtonHist = new JButton();
			btnActionjButtonHist.setBounds(new Rectangle(301, 30, 140, 16));
			btnActionjButtonHist.setText("Histogram");
			btnActionjButtonHist
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							BufferedImage _hist = ImageUtils.getHistogram(
									getSelectedImage(), Color.RED.toString());
							openImage(_hist, "Histogram");
						}
					});
		}
		return btnActionjButtonHist;
	}

	private JButton getBtnActionjButtonFilt3high() {
		if (btnActionjButtonFilt3 == null) {
			btnActionjButtonFilt3 = new JButton();
			btnActionjButtonFilt3.setBounds(new Rectangle(301, 50, 140, 16));
			btnActionjButtonFilt3.setText("Filter 3x3 High");
			btnActionjButtonFilt3
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							double[][] values = { { -1, -1, -1 },
									{ -1, 8, -1 }, { -1, -1, -1 } };
							ImageMask mask = new ImageMask(values, 3, 3, 9);
							_lastImage = ImageUtils.filterImage(
									getSelectedImage(), mask);
							openImage(_lastImage, "Filter 3x3 High");
						}
					});
		}
		return btnActionjButtonFilt3;
	}

	private JButton getBtnActionjButtonFilt3low() {
		if (btnActionjButtonFilt3l == null) {
			btnActionjButtonFilt3l = new JButton();
			btnActionjButtonFilt3l.setBounds(new Rectangle(301, 70, 140, 16));
			btnActionjButtonFilt3l.setText("Filter 3x3 Low");
			btnActionjButtonFilt3l
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							double[][] values = { { 1, 1, 1 }, { 1, 1, 1 },
									{ 1, 1, 1 } };
							ImageMask mask = new ImageMask(values, 3, 3, 9);
							_lastImage = ImageUtils.filterImage(
									getSelectedImage(), mask);
							openImage(_lastImage, "Filter 3x3 Low");
						}
					});
		}
		return btnActionjButtonFilt3l;
	}

	private JButton getBtnActionjButtonFilt5high() {
		if (btnActionjButtonFilt5 == null) {
			btnActionjButtonFilt5 = new JButton();
			btnActionjButtonFilt5.setBounds(new Rectangle(301, 90, 140, 16));
			btnActionjButtonFilt5.setText("Filter 5x5 High");
			btnActionjButtonFilt5
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							double[][] values = { { -1, -1, -1, -1, -1 },
									{ -1, -1, -1, -1, -1 },
									{ -1, -1, 24, -1, -1 },
									{ -1, -1, -1, -1, -1 },
									{ -1, -1, -1, -1, -1 } };
							ImageMask mask = new ImageMask(values, 5, 5, 9);
							_lastImage = ImageUtils.filterImage(
									getSelectedImage(), mask);
							openImage(_lastImage, "Filter 5x5 High");
						}
					});
		}
		return btnActionjButtonFilt5;
	}

	private JButton getBtnActionjButtonFilt5low() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(301, 110, 140, 16));
		button.setText("Filter 5x5 Low");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println("actionPerformed() by Browse");
				// TODO Auto-generated Event stub actionPerformed()
				double[][] values = { { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 },
						{ 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 } };
				ImageMask mask = new ImageMask(values, 5, 5, 9);
				_lastImage = ImageUtils.filterImage(getSelectedImage(), mask);
				openImage(_lastImage, "Filter 5x5 Low");
			}
		});

		return button;
	}

	private JButton getBtnActionjButtonFilt52low() {
		if (btnActionjButtonFilt52l == null) {
			btnActionjButtonFilt52l = new JButton();
			btnActionjButtonFilt52l.setBounds(new Rectangle(301, 130, 140, 16));
			btnActionjButtonFilt52l.setText("Filter 5x2 Low");
			btnActionjButtonFilt52l
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							double[][] values = { { 1, 1 }, { 1, 1 }, { 1, 1 },
									{ 1, 1 }, { 1, 1 } };
							ImageMask mask = new ImageMask(values, 5, 2, 9);
							_lastImage = ImageUtils.filterImage(
									getSelectedImage(), mask);
							openImage(_lastImage, "Filter 5x2 Low");
						}
					});
		}
		return btnActionjButtonFilt52l;
	}

	private JButton getBtnActionjButtonFilt52high() {
		if (btnActionjButtonFilt52h == null) {
			btnActionjButtonFilt52h = new JButton();
			btnActionjButtonFilt52h.setBounds(new Rectangle(301, 150, 140, 16));
			btnActionjButtonFilt52h.setText("Filter 5x2 High");
			btnActionjButtonFilt52h
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							double[][] values = { { -1, -1 }, { -1, -1 },
									{ 4, 4 }, { -1, -1 }, { -1, -1 } };
							ImageMask mask = new ImageMask(values, 5, 2, 9);
							_lastImage = ImageUtils.filterImage(
									getSelectedImage(), mask);
							openImage(_lastImage, "Filter 5x2 High");
						}
					});
		}
		return btnActionjButtonFilt52h;
	}

	private JButton btnActionjButtonGauss() {
		if (btnActionjButtonGauss == null) {
			btnActionjButtonGauss = new JButton();
			btnActionjButtonGauss.setBounds(new Rectangle(301, 170, 140, 16));
			btnActionjButtonGauss.setText("Noise Gauss");
			btnActionjButtonGauss
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							_lastImage = RandomGenerator.noiseGauss(
									getSelectedImage(), 0, 1);

							openImage(_lastImage, "Noise Gauss");
						}
					});
		}
		return btnActionjButtonGauss;
	}

	private JButton btnActionjButtonRay() {
		if (btnActionjButtonRay == null) {
			btnActionjButtonRay = new JButton();
			btnActionjButtonRay.setBounds(new Rectangle(301, 190, 140, 16));
			btnActionjButtonRay.setText("Noise Ray");
			btnActionjButtonRay
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							_lastImage = RandomGenerator.noiseRayleigh(
									getSelectedImage(), 0.5);

							openImage(_lastImage, "Noise Ray");
						}
					});
		}
		return btnActionjButtonRay;
	}

	private JButton btnActionjButtonExp() {
		if (btnActionjButtonExp == null) {
			btnActionjButtonExp = new JButton();
			btnActionjButtonExp.setBounds(new Rectangle(301, 210, 140, 16));
			btnActionjButtonExp.setText("Noise Exp");
			btnActionjButtonExp
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							_lastImage = RandomGenerator.noiseExp(
									getSelectedImage(), 0.05);

							openImage(_lastImage, "Noise Exp");
						}
					});
		}
		return btnActionjButtonExp;
	}

	private JButton btnActionjButtonSalt() {
		if (btnActionjButtonSalt == null) {
			btnActionjButtonSalt = new JButton();
			btnActionjButtonSalt.setBounds(new Rectangle(301, 230, 140, 16));
			btnActionjButtonSalt.setText("Noise Salt & Pepper");
			btnActionjButtonSalt
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()

							_lastImage = RandomGenerator.noiseSaltPepper(
									getSelectedImage(), 0.05, 0.95);

							openImage(_lastImage, "Noise Salt & Pepper");
						}
					});
		}
		return btnActionjButtonSalt;
	}
	
	private JButton getBtnActionjButtonMean() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(301, 250, 140, 16));
		button.setText("Mean Filter");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				_lastImage = ImageUtils.filterMeanImage(getSelectedImage(), 1);
				openImage(_lastImage, "Mean Filter");
			}
		});

		return button;
	}

	private void openImage(BufferedImage image, String Title) {
		// System.out.println("namePathImage = " + namePathImage);
		JPanel panel = null;

		try {
			panel = getJContentPane1(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JDialog d = new JDialog(this, Title, false);
		d.getContentPane().add(panel);
		d.pack();
		d.setLocationRelativeTo(this);
		d.setVisible(true);
	}

	/**
	 * This method initializes btnActionjButton1 - Browse
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnActionjButton1() {
		if (btnActionjButton1 == null) {
			btnActionjButton1 = new JButton();
			btnActionjButton1.setBounds(new Rectangle(201, 10, 98, 16));
			btnActionjButton1.setText("Browse");
			btnActionjButton1
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							openDialog();
						}
					});
		}
		return btnActionjButton1;
	}

	private void openDialog() {
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			// System.out.println("selectedFile = " +
			// chooser.getSelectedFile());
			namePathImage = chooser.getSelectedFile().getAbsolutePath();
			// System.out.println("namePathImage = " + namePathImage);
			JPanel panel = null;
			try {
				_image = ImageIO.read(new File(namePathImage));
				panel = getJContentPane1();
			} catch (IOException e) {
				System.out.println("io error: " + e.getMessage());
				return;
			}
			PathImage.setText(namePathImage);
			// JInternalFrames are designed to go in a JDesktopPane
			// Let's use a JDialog for now...
			JDialog d = new JDialog(this, "Image Dialog", false);
			d.getContentPane().add(panel);
			d.pack();
			d.setLocationRelativeTo(this);
			d.setVisible(true);
		}
	}

	/**
	 * This method initializes jContentPane1 - Image
	 * 
	 * @return javax.swing.JPanel
	 * 
	 * @throws IOException
	 */
	private JPanel getJContentPane1() throws IOException {
		if (_image != null) {
			return getJContentPane1(_image);
		}
		return jContentPane1;
	}

	private JPanel getJContentPane1(BufferedImage image) throws IOException {
		if (jContentPane1 == null) {
			jContentPane1 = new JPanel();
			jContentPane1.setLayout(new BorderLayout());
			jContentPane1.add(getPathImage(), BorderLayout.NORTH);
			System.out.println("Name Image Path = " + namePathImage);
		}
		// remove the last label before we add a new one
		if (jContentPane1.getComponentCount() > 1)
			jContentPane1.remove(1);
		JLabel label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		if (image != null) {
			ImageIcon icon = new ImageIcon(image);
			label.setIcon(icon);
		} else {
			label.setText("namePathImage = " + namePathImage);
		}
		jContentPane1.add(label, BorderLayout.CENTER);
		jContentPane1.revalidate();
		return jContentPane1;
	}

	/**
	 * This method initializes jTextField - Get path image
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getPathImage() {
		if (PathImage == null) {
			PathImage = new JTextField();
			PathImage.setBounds(new java.awt.Rectangle(18, 36, 281, 210));
			PathImage.setText("No opened File ");
			PathImage.setColumns(30);
			PathImage.setBorder(javax.swing.BorderFactory.createLineBorder(
					java.awt.Color.white, 5));
		}
		return PathImage;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new UI();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UI thisClass;
				try {
					thisClass = new UI();
					thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					thisClass.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * This is the default constructor
	 * 
	 * @throws IOException
	 */
	public UI() throws IOException {
		super();
		chooser.setApproveButtonText("Open");
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		this.setSize(471, 319);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Image Annotation Tool");
	}
}
