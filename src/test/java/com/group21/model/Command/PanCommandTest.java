/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;


/**
 *
 * @author mikel
 */

/*
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler; // Per catturare gli event handler
import javafx.geometry.BoundingBox; // Per BoundingBox
import javafx.geometry.Bounds; // Per Bounds
import javafx.scene.input.MouseButton; // Per MouseButton
import javafx.event.EventType; // Per EventType
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PanCommandTest {

    private Pane canvas;
    private ScrollPane scrollPane;
    private PanCommand panCommand;

    @Before
    public void setUp() {
   
        canvas = new Pane();
        scrollPane = new ScrollPane();
        panCommand = new PanCommand(canvas, scrollPane);
    }

    @Test
    public void testExecuteActivatesPan() {
        assertFalse(panCommand.isActive());

        panCommand.execute();

        assertTrue(panCommand.isActive());
        assertNotNull(canvas.getOnMousePressed());
        assertNotNull(canvas.getOnMouseDragged());
        assertNotNull(canvas.getOnMouseReleased());
    }

    @Test
    public void testExecuteDeactivatesPanIfAlreadyActive() {
        panCommand.execute(); // attiva
        assertTrue(panCommand.isActive());

        panCommand.execute(); // disattiva
        assertFalse(panCommand.isActive());
        assertNull(canvas.getOnMousePressed());
        assertNull(canvas.getOnMouseDragged());
        assertNull(canvas.getOnMouseReleased());
    }

    @Test
    public void testClampFunctionIsCorrect() throws Exception {
        // accedo tramite reflection al metodo privato
        java.lang.reflect.Method clampMethod = PanCommand.class.getDeclaredMethod("clamp", double.class, double.class, double.class);
        clampMethod.setAccessible(true);

        assertEquals(0.0, (double) clampMethod.invoke(panCommand, -1.0, 0.0, 1.0), 0.001);
        assertEquals(1.0, (double) clampMethod.invoke(panCommand, 2.0, 0.0, 1.0), 0.001);
        assertEquals(0.5, (double) clampMethod.invoke(panCommand, 0.5, 0.0, 1.0), 0.001);
    }
}  */



