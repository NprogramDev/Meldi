/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
package de.a_sitko.meldi.window;

import javafx.scene.Node;

/**
 * Why is this not in javafx?
 */
public interface ParentBox {
    /**
     * Adds a child to the javafx box
     * @param child The child
     */
    void append(Node child);
}
