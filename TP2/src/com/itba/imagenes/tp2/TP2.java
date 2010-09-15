package com.itba.imagenes.tp2;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class TP2 {

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

}
