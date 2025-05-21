/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Decorator;

/**
 *
 * @author carmi
 */

import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaneTest {

    @Test
    void testGetPaneReturnsInjectedPane() {
        BorderPane borderPane = new BorderPane();
        Pane customPane = new Pane(borderPane);

        assertNotNull(customPane.getPane());
        assertEquals(borderPane, customPane.getPane(), "Should return the same BorderPane passed to the constructor");
    }
}

