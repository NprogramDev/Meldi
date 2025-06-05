package de.a_sitko.meldi;

import javax.swing.*;

public class Error {
    static JFrame frame = new JFrame(Texts.TITLE);
    public static void report(String error){
        //TODO Open Window to show the error!
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame,
                error,
                Texts.TITLE_ERROR,
                JOptionPane.ERROR_MESSAGE);
    }
}
