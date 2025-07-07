/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
package de.a_sitko.meldi;

public class Texts {
    /**
     * Text shown as long as the hand is not raised.
     * Displayed on the button
     */
    public static final String RAISE = "Melden 👋";
    /**
     * Text shown as long as the hand is raised and not first place on the list.
     * Displayed on the button.
     */
    public static final String WAIT = "Warte bitte! Noch %d vor dir! ⌛";
    /**
     * Text shown as long as the hand is raised and you are first place on the list.
     * Displayed on the button.
     */
    public static final String READY = "Du bist der Nächste!";
    /**
     * Label for the Name of the user
     */
    public static final String USER = "Name: ";
    /**
     * Label for the Device ID of the user
     */
    public static final String DEVICE = "\nGerät: ";
    /**
     * Label for the admin users shot
     */
    public static final String USER_ID = "ID: ";
    /**
     * The Application Title
     */
    public static final String TITLE = "Meldi";
    /**
     * The title of a error message box
     */
    public static final String TITLE_ERROR = "Meldi - Error";
    /**
     * Shown in the details tab if no connection can be made
     * 🚫 = uD83D uDEAB
     */
    public static final String NO_CONNECTION = "Keine Verbindung! \uD83D\uDEAB";
    /**
     * Shown in the details tab if a connection can be made
     * 🌎 = uD83C uDF0E
     */
    public static final String CONNECTED = "Verbunden! \uD83C\uDF0E";
    /**
     * Shown in the details tab if the binary should be updated
     */
    public static final String UPDATE_AVAL = "Update verfügbar!";
    /**
     * Shown in the details tab if a connection can be made
     * Not used
     */
    public static final String CONNECTION_ERROR = "Konnte keine Verbindung zum Server aufbauen! Ist ihr PC mit dem Internet verbunden oder ihr Klient zu alt!";
    /**
     * Shown in the details tab if a connection can be made
     */
    public static final String OLD_SERVER = "Zurück in die Zukunft!";
}
