package com.itba.imagenes.UI;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.itba.imagenes.ColorEnum;
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
	private JButton btnActionjButtonIso = null;
	private JButton btnActionjButtonAniso = null;

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
			lblname.setBounds(new Rectangle(20, 10, 168, 16));
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
			jContentPane.add(getBtnActionjButtonRoberts(), null);
			jContentPane.add(getBtnActionjButtonPrewitt(), null);
			jContentPane.add(getBtnActionjButtonSobel(), null);
			jContentPane.add(getBtnActionjButtonBorder1(), null);
			jContentPane.add(getBtnActionjButtonBorder2(), null);
			jContentPane.add(getBtnActionjButtonBorder3(), null);
			jContentPane.add(getBtnActionjButtonBorder4(), null);
			jContentPane.add(getBtnActionjButtonBorderLaplace(), null);
			jContentPane.add(getBtnActionjButtonBorderCrossByCero(), null);
			jContentPane.add(getBtnActionjButtonBorderLaplaceV(), null);
			
			jContentPane.add(getBtnActionjButtonIso(), null);
			jContentPane.add(getBtnActionjButtonAniso(), null);
			jContentPane.add(getBtnActionjButtonUmb(), null);
			jContentPane.add(getBtnActionjButtonHough(), null);

			jContentPane
					.add(getLabel("Filters", new Rectangle(20, 120, 50, 20)));
			jContentPane
					.add(getLabel("Noise", new Rectangle(200, 120, 50, 20)));
			jContentPane.add(getLabel("Border detector", new Rectangle(380,
					120, 200, 20)));

			jContentPane.add(getLabel("Utilities", new Rectangle(200, 220, 200,
					20)));

			jContentPane.add(getLabel("Apply to",
					new Rectangle(20, 50, 100, 20)));

			setRadios(jContentPane);

			// jContentPane.add(jLabel, null);
		}
		return jContentPane;
	}

	private JLabel getLabel(String desc, Rectangle pos) {
		JLabel label = new JLabel(desc);
		label.setBounds(pos);

		return label;
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
		originalButton.setBounds(80, 54, 200, 15);

		JRadioButton lastButton = new JRadioButton("Last Transform");
		lastButton.setMnemonic('l');
		lastButton.setActionCommand("last");
		lastButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				useOriginal = false;
			}
		});
		lastButton.setBounds(80, 74, 200, 15);

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
							System.exit(0);
						}
					});
		}
		return menuItemClose;
	}

	private JButton getBtnActionjButtonBW() {
		if (btnActionjButtonBW == null) {
			btnActionjButtonBW = new JButton();
			btnActionjButtonBW.setBounds(new Rectangle(200, 240, 140, 16));
			btnActionjButtonBW.setText("Black And White");
			btnActionjButtonBW
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							_lastImage = ImageUtils.blackAndWhite(
									getSelectedImage(),
									ColorEnum.RED.toString());
							openImage(_lastImage, "Black And White");
						}
					});
		}
		return btnActionjButtonBW;
	}

	private JButton getBtnActionjButtonHist() {
		if (btnActionjButtonHist == null) {
			btnActionjButtonHist = new JButton();
			btnActionjButtonHist.setBounds(new Rectangle(200, 260, 140, 16));
			btnActionjButtonHist.setText("Histogram");
			btnActionjButtonHist
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed() by Browse");
							// TODO Auto-generated Event stub actionPerformed()
							BufferedImage _hist = ImageUtils.getHistogram(
									getSelectedImage(),
									ColorEnum.RED.toString());
							openImage(_hist, "Histogram");
						}
					});
		}
		return btnActionjButtonHist;
	}

	private JButton getBtnActionjButtonUmb() {
		JButton btnActionjButtonUmb = new JButton();
		btnActionjButtonUmb.setBounds(new Rectangle(200, 280, 140, 16));
		btnActionjButtonUmb.setText("Umbral");
		btnActionjButtonUmb
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {

						if (getSelectedImage() == null) {
							JOptionPane
									.showMessageDialog(
											null,
											"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
											"Error: no hay imagen de entrada",
											JOptionPane.ERROR_MESSAGE);
							return;
						}

						String paramsResult = JOptionPane.showInputDialog(null,
								"Introduzca parametros: scalar", "Parametros",
								JOptionPane.OK_CANCEL_OPTION);

						if (paramsResult == null)
							return;

						try {
							String[] params = paramsResult.split(",");

							if (params.length != 1)
								throw new Exception(
										"Cantidad de parametros incorrecta");

							_lastImage = ImageUtils.UmbralFunction(
									getSelectedImage(),
									Double.parseDouble(params[0]));

							openImage(_lastImage, "Umbral");
						} catch (Exception ex) {
							JOptionPane
									.showMessageDialog(
											null,
											"Parametros incorrectos, respete el formato pedido!",
											"Error: parametros incorrectos",
											JOptionPane.ERROR_MESSAGE);
						}
					}
				});

		return btnActionjButtonUmb;
	}

	private JButton getBtnActionjButtonHough() {
		JButton btnActionjButtonHough = new JButton();
		btnActionjButtonHough.setBounds(new Rectangle(200, 300, 140, 16));
		btnActionjButtonHough.setText("Hough");
		btnActionjButtonHough
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {

						if (getSelectedImage() == null) {
							JOptionPane
									.showMessageDialog(
											null,
											"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
											"Error: no hay imagen de entrada",
											JOptionPane.ERROR_MESSAGE);
							return;
						}

						String paramsResult = JOptionPane.showInputDialog(null,
								"Introduzca parametros: delta", "Parametros",
								JOptionPane.OK_CANCEL_OPTION);

						if (paramsResult == null)
							return;

						try {
							String[] params = paramsResult.split(",");

							if (params.length != 1)
								throw new Exception(
										"Cantidad de parametros incorrecta");

							_lastImage = ImageUtils.Hough(getSelectedImage(),
									Double.parseDouble(params[0]));

							openImage(_lastImage, "Hough");
						} catch (Exception ex) {
							JOptionPane
									.showMessageDialog(
											null,
											"Parametros incorrectos, respete el formato pedido!",
											"Error: parametros incorrectos",
											JOptionPane.ERROR_MESSAGE);
						}
					}
				});

		return btnActionjButtonHough;
	}

	private JButton getBtnActionjButtonFilt3high() {
		if (btnActionjButtonFilt3 == null) {
			btnActionjButtonFilt3 = new JButton();
			btnActionjButtonFilt3.setBounds(new Rectangle(20, 140, 140, 16));
			btnActionjButtonFilt3.setText("3x3 High");
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
			btnActionjButtonFilt3l.setBounds(new Rectangle(20, 160, 140, 16));
			btnActionjButtonFilt3l.setText("3x3 Low");
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
			btnActionjButtonFilt5.setBounds(new Rectangle(20, 180, 140, 16));
			btnActionjButtonFilt5.setText("5x5 High");
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
		button.setBounds(new Rectangle(20, 200, 140, 16));
		button.setText("5x5 Low");
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
			btnActionjButtonFilt52l.setBounds(new Rectangle(20, 220, 140, 16));
			btnActionjButtonFilt52l.setText("5x2 Low");
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
			btnActionjButtonFilt52h.setBounds(new Rectangle(20, 240, 140, 16));
			btnActionjButtonFilt52h.setText("5x2 High");
			btnActionjButtonFilt52h
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
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
			btnActionjButtonGauss.setBounds(new Rectangle(200, 140, 140, 16));
			btnActionjButtonGauss.setText("Gauss");
			btnActionjButtonGauss
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (getSelectedImage() == null) {
								JOptionPane
										.showMessageDialog(
												null,
												"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
												"Error: no hay imagen de entrada",
												JOptionPane.ERROR_MESSAGE);
								return;
							}

							String paramsResult = JOptionPane.showInputDialog(
									null, "Introduzca parametros: mean,stdev",
									"Parametros", JOptionPane.OK_CANCEL_OPTION);

							if (paramsResult == null)
								return;

							try {
								String[] params = paramsResult.split(",");

								if (params.length != 2)
									throw new Exception(
											"Cantidad de parametros incorrecta");

								_lastImage = ImageUtils.noiseGauss(
										getSelectedImage(),
										Double.parseDouble(params[0]),
										Double.parseDouble(params[1]));

								openImage(_lastImage, "Noise Gauss");
							} catch (Exception ex) {
								JOptionPane
										.showMessageDialog(
												null,
												"Parametros incorrectos, respete el formato pedido!",
												"Error: parametros incorrectos",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			btnActionjButtonGauss.setToolTipText("mean,stdev");
		}
		return btnActionjButtonGauss;
	}

	private JButton btnActionjButtonRay() {
		if (btnActionjButtonRay == null) {
			btnActionjButtonRay = new JButton();
			btnActionjButtonRay.setBounds(new Rectangle(200, 160, 140, 16));
			btnActionjButtonRay.setText("Ray");
			btnActionjButtonRay
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (getSelectedImage() == null) {
								JOptionPane
										.showMessageDialog(
												null,
												"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
												"Error: no hay imagen de entrada",
												JOptionPane.ERROR_MESSAGE);
								return;
							}

							String paramsResult = JOptionPane.showInputDialog(
									null, "Introduzca parametros: sigma",
									"Parametros", JOptionPane.OK_CANCEL_OPTION);

							if (paramsResult == null)
								return;

							try {
								String[] params = paramsResult.split(",");

								if (params.length != 1)
									throw new Exception(
											"Cantidad de parametros incorrecta");

								_lastImage = ImageUtils.noiseRayleigh(
										getSelectedImage(),
										Double.parseDouble(params[0]));

								openImage(_lastImage, "Noise Ray");
							} catch (Exception ex) {
								JOptionPane
										.showMessageDialog(
												null,
												"Parametros incorrectos, respete el formato pedido!",
												"Error: parametros incorrectos",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			btnActionjButtonRay.setToolTipText("sigma");
		}
		return btnActionjButtonRay;
	}

	private JButton btnActionjButtonExp() {
		if (btnActionjButtonExp == null) {
			btnActionjButtonExp = new JButton();
			btnActionjButtonExp.setBounds(new Rectangle(200, 180, 140, 16));
			btnActionjButtonExp.setText("Exp");
			btnActionjButtonExp
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (getSelectedImage() == null) {
								JOptionPane
										.showMessageDialog(
												null,
												"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
												"Error: no hay imagen de entrada",
												JOptionPane.ERROR_MESSAGE);
								return;
							}

							String paramsResult = JOptionPane.showInputDialog(
									null, "Introduzca parametros: lambda",
									"Parametros", JOptionPane.OK_CANCEL_OPTION);

							if (paramsResult == null)
								return;

							try {
								String[] params = paramsResult.split(",");

								if (params.length != 1)
									throw new Exception(
											"Cantidad de parametros incorrecta");

								_lastImage = ImageUtils.noiseExp(
										getSelectedImage(),
										Double.parseDouble(params[0]));

								openImage(_lastImage, "Noise Exp");
							} catch (Exception ex) {
								JOptionPane
										.showMessageDialog(
												null,
												"Parametros incorrectos, respete el formato pedido!",
												"Error: parametros incorrectos",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			btnActionjButtonExp.setToolTipText("lambda");
		}
		return btnActionjButtonExp;
	}

	private JButton btnActionjButtonSalt() {
		if (btnActionjButtonSalt == null) {
			btnActionjButtonSalt = new JButton();
			btnActionjButtonSalt.setBounds(new Rectangle(200, 200, 140, 16));
			btnActionjButtonSalt.setText("Salt & Pepper");
			btnActionjButtonSalt
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (getSelectedImage() == null) {
								JOptionPane
										.showMessageDialog(
												null,
												"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
												"Error: no hay imagen de entrada",
												JOptionPane.ERROR_MESSAGE);
								return;
							}

							String paramsResult = JOptionPane.showInputDialog(
									null, "Introduzca parametros: p0,p1",
									"Parametros", JOptionPane.OK_CANCEL_OPTION);

							if (paramsResult == null)
								return;

							try {
								String[] params = paramsResult.split(",");

								if (params.length != 2)
									throw new Exception(
											"Cantidad de parametros incorrecta");

								_lastImage = ImageUtils.noiseSaltPepper(
										getSelectedImage(),
										Double.parseDouble(params[0]),
										Double.parseDouble(params[1]));

								openImage(_lastImage, "Noise Salt & Pepper");
							} catch (Exception ex) {
								JOptionPane
										.showMessageDialog(
												null,
												"Parametros incorrectos, respete el formato pedido!",
												"Error: parametros incorrectos",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			btnActionjButtonSalt.setToolTipText("p0,p1");
		}
		return btnActionjButtonSalt;
	}

	private JButton getBtnActionjButtonMean() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(20, 270, 140, 16));
		button.setText("Mean Filter");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				_lastImage = ImageUtils.filterMeanImage(getSelectedImage(), 1);
				openImage(_lastImage, "Mean Filter");
			}
		});

		return button;
	}

	private JButton getBtnActionjButtonRoberts() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 140, 140, 16));
		button.setText("Roberts");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { 1, 0 }, { 0, -1 } };
				double[][] values2 = { { 0, 1 }, { -1, 0 } };
				ImageMask mask1 = new ImageMask(values1, 2, 2, 1);
				ImageMask mask2 = new ImageMask(values2, 2, 2, 1);

				BufferedImage img1 = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				BufferedImage img2 = ImageUtils.filterImage(getSelectedImage(),
						mask2);

				try {
					_lastImage = ImageUtils.MathOperatorFunction(img1, img2,
							"suma", 0.0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				openImage(_lastImage, "Border detector (Roberts)");
			}
		});

		return button;
	}

	private JButton getBtnActionjButtonPrewitt() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 160, 140, 16));
		button.setText("Prewitt");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };
				double[][] values2 = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);
				ImageMask mask2 = new ImageMask(values2, 3, 3, 1);

				BufferedImage img1 = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				BufferedImage img2 = ImageUtils.filterImage(getSelectedImage(),
						mask2);

				try {
					_lastImage = ImageUtils.MathOperatorFunction(img1, img2,
							"suma", 0.0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				openImage(_lastImage, "Border detector (Prewitt)");
			}
		});

		return button;
	}

	private JButton getBtnActionjButtonSobel() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 180, 140, 16));
		button.setText("Sobel");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
				double[][] values2 = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);
				ImageMask mask2 = new ImageMask(values2, 3, 3, 1);

				BufferedImage img1 = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				BufferedImage img2 = ImageUtils.filterImage(getSelectedImage(),
						mask2);

				try {
					_lastImage = ImageUtils.MathOperatorFunction(img1, img2,
							"suma", 0.0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				openImage(_lastImage, "Border detector (Sobel)");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorder1() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 200, 140, 16));
		button.setText("Border 1");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { 1, 1, 1 }, { 1, -2, 1 }, { -1, -1, -1 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

				_lastImage = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				openImage(_lastImage, "Border detector (Border 1)");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorder2() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 220, 140, 16));
		button.setText("Border 2 - Kirsh");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { 5, 5, 5 }, { -3, 0, -3 }, { -3, -3, -3 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

				_lastImage = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				openImage(_lastImage, "Border detector (Border 2 - Kirsh)");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorder3() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 240, 140, 16));
		button.setText("Border 3");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { 1, 1, 1 }, { 0,  0, 0 }, { -1, -1, -1 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

				_lastImage = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				openImage(_lastImage, "Border detector (Border 3)");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorder4() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 260, 140, 16));
		button.setText("Border 4");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

				_lastImage = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				openImage(_lastImage, "Border detector (Border 4)");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorderLaplace() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 280, 140, 16));
		button.setText("Border Laplace");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				double[][] values1 = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
				ImageMask mask1 = new ImageMask(values1, 3, 3, 1);

				_lastImage = ImageUtils.filterImage(getSelectedImage(),
						mask1);

				openImage(_lastImage, "Border detector Laplace");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorderCrossByCero() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 300, 140, 16));
		button.setText("Cross By Cero");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				_lastImage = ImageUtils.crossbycero(getSelectedImage());

				openImage(_lastImage, "Cross By Cero");
			}
		});
		return button;
	}
	
	private JButton getBtnActionjButtonBorderLaplaceV() {
		JButton button = new JButton();
		button.setBounds(new Rectangle(380, 320, 140, 16));
		button.setText("Laplace - Varianza");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				_lastImage = ImageUtils.laplacevarianza(getSelectedImage(),2);

				openImage(_lastImage, "Laplace - Varianza");
			}
		});
		return button;
	}

	private JButton getBtnActionjButtonIso() {
		if (btnActionjButtonIso == null) {
			btnActionjButtonIso = new JButton();
			btnActionjButtonIso.setBounds(new Rectangle(20, 290, 140, 16));
			btnActionjButtonIso.setText("Isotropic");
			btnActionjButtonIso
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (getSelectedImage() == null) {
								JOptionPane
										.showMessageDialog(
												null,
												"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
												"Error: no hay imagen de entrada",
												JOptionPane.ERROR_MESSAGE);
								return;
							}

							String paramsResult = JOptionPane
									.showInputDialog(
											null,
											"Introduzca parametros: lambda,iteraciones",
											"Parametros",
											JOptionPane.OK_CANCEL_OPTION);

							if (paramsResult == null)
								return;

							try {
								String[] params = paramsResult.split(",");

								if (params.length != 2)
									throw new Exception(
											"Cantidad de parametros incorrecta");

								_lastImage = ImageUtils
										.isotropicDifusionFilter(
												getSelectedImage(),
												Float.parseFloat(params[0]),
												Integer.parseInt(params[1]));
								openImage(_lastImage, "Isotropic");
							} catch (Exception ex) {
								JOptionPane
										.showMessageDialog(
												null,
												"Parametros incorrectos, respete el formato pedido!",
												"Error: parametros incorrectos",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			btnActionjButtonIso.setToolTipText("t");
		}
		return btnActionjButtonIso;
	}

	private JButton getBtnActionjButtonAniso() {
		if (btnActionjButtonAniso == null) {
			btnActionjButtonAniso = new JButton();
			btnActionjButtonAniso.setBounds(new Rectangle(20, 310, 140, 16));
			btnActionjButtonAniso.setText("Anisotropic");
			btnActionjButtonAniso
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (getSelectedImage() == null) {
								JOptionPane
										.showMessageDialog(
												null,
												"Tiene que seleccionar una imagen y tenerla abierta para aplicar un metodo",
												"Error: no hay imagen de entrada",
												JOptionPane.ERROR_MESSAGE);
								return;
							}

							String paramsResult = JOptionPane
									.showInputDialog(
											null,
											"Introduzca parametros: lambda,iterations,sigma",
											"Parametros",
											JOptionPane.OK_CANCEL_OPTION);

							if (paramsResult == null)
								return;

							try {
								String[] params = paramsResult.split(",");

								if (params.length != 3)
									throw new Exception(
											"Cantidad de parametros incorrecta");

								_lastImage = ImageUtils
										.anisotropicDifusionFilter(
												getSelectedImage(),
												Float.parseFloat(params[0]),
												Integer.parseInt(params[1]),
												Float.parseFloat(params[2]));
								openImage(_lastImage, "Anisotropic");
							} catch (Exception ex) {
								JOptionPane
										.showMessageDialog(
												null,
												"Parametros incorrectos, respete el formato pedido!",
												"Error: parametros incorrectos",
												JOptionPane.ERROR_MESSAGE);
							}
						}
					});
			btnActionjButtonAniso.setToolTipText("lambda, cn, cs, ce, cw");
		}
		return btnActionjButtonAniso;
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
		d.setLocation(0, 200);
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
			btnActionjButton1.setBounds(new Rectangle(140, 10, 98, 16));
			btnActionjButton1.setText("Browse");
			btnActionjButton1
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
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
			d.setLocation(470, 0);
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
		this.setSize(600, 430);
		this.setResizable(false);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("ITBA Imagenes TP2");
	}
}
