/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
package de.a_sitko.meldi;

import javax.swing.*;

/**
 * Class for a error message system
 */
public class Error {
    static JFrame frame = new JFrame(Texts.TITLE);

    /**
     * Report a error to the user
     * @param error The error message
     */
    public static void report(String error){
        //TODO Open Window to show the error!
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame,
                error,
                Texts.TITLE_ERROR,
                JOptionPane.ERROR_MESSAGE);
    }
}
