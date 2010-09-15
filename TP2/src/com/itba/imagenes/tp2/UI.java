package com.itba.imagenes.tp2;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
 
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
    private JLabel jLabel = null;
    private JPanel jContentPane1 = null;
    private JTextField PathImage = null;
    private BufferedImage _image;
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
//            jLabel = new JLabel();
//            jLabel.setText("Domain");
//            jLabel.setBounds(new Rectangle(360, 26, 53, 31));
            lblname = new JLabel();
            lblname.setBounds(new Rectangle(19, 10, 168, 16));
            lblname.setText("Choose an image");
            jContentPane = new JPanel();
 
            jContentPane.setLayout(null);
            jContentPane.add(lblname, null);
            jContentPane.add(getBtnActionjButton1(), null);
//            jContentPane.add(jLabel, null);
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
            menuItemClose.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()");
                    // TODO Auto-generated Event stub actionPerformed()
                    System.exit(0);
                }
            });
        }
        return menuItemClose;
    }
    
    
    private JButton getBtnActionjButton4() {
        if (btnActionjButton1 == null) {
            btnActionjButton1 = new JButton();
            btnActionjButton1.setBounds(new Rectangle(201, 10, 98, 16));
            btnActionjButton1.setText("Black And White");
            btnActionjButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed() by Browse");
                    // TODO Auto-generated Event stub actionPerformed()
                    openDialog();
                }
            });
        }
        return btnActionjButton1;
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
            btnActionjButton1.addActionListener(new java.awt.event.ActionListener() {
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
            //System.out.println("selectedFile = " + chooser.getSelectedFile());
            namePathImage = chooser.getSelectedFile().getAbsolutePath();
            //System.out.println("namePathImage = " + namePathImage);
            JPanel panel = null;
            try {
            	_image = ImageIO.read(new File(namePathImage));
                panel = getJContentPane1();
            } catch(IOException e) {
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
        if(_image != null) {
            return getJContentPane1(_image);
        }
        return jContentPane1;
    }
    
    private JPanel getJContentPane1(BufferedImage image) throws IOException {
        if (jContentPane1 == null) {
            jContentPane1 = new JPanel();
            jContentPane1.setLayout(new BorderLayout());
            jContentPane1.add(getPathImage(), BorderLayout.NORTH);
            System.out.println("Name Image Path = "+namePathImage);
        }
        // remove the last label before we add a new one
        if(jContentPane1.getComponentCount() > 1)
            jContentPane1.remove(1);
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        if(image != null) {
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
            PathImage.setText( "No opened File ");
            PathImage.setColumns(30);
            PathImage.setBorder(javax.swing.BorderFactory.createLineBorder(
                                      java.awt.Color.white,5));
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
