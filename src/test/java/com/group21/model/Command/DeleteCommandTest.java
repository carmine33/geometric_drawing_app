/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

/**
 *
 * @author carmi
 */
import com.group21.model.Shape.ShapeRectangle;
import com.group21.model.Shape.ShapeBase;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteCommandTest {

    @Test
    void testExecuteDeletesSelectedShape() {
        // Setup
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle rect = new ShapeRectangle(10, 10, 50, 50, Color.BLUE, Color.BLACK, 1.0);
        shapes.add(rect);

        ShapeSelector selector = new ShapeSelector(shapes, rect);

        // Pre-condizioni
        assertEquals(rect, selector.getSelectedShape());
        assertTrue(shapes.contains(rect));

        // Execute command
        DeleteCommand command = new DeleteCommand(selector);
        command.execute();

        // Post-condizioni
        assertFalse(shapes.contains(rect));
    }
}