/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.paint.Color;
import com.group21.model.Shape.*;

/**
 *
 * @author matte
 */
public class ToFrontCommandTest {

    @Test
    void testExecuteMovesShapeToFront() {
        // Setup initial shape list
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle r1 = new ShapeRectangle(0, 0, 50, 50, Color.BLACK, Color.BLUE, 1.0);
        ShapeRectangle r2 = new ShapeRectangle(10, 10, 50, 50, Color.BLACK, Color.RED, 1.0);
        shapes.add(r1);
        shapes.add(r2);

        // r1 is selected, initially at index 0
        ShapeSelector selector = new ShapeSelector(shapes, r1,null,null);
        ToFrontCommand command = new ToFrontCommand(selector, 0, shapes.size());

        // Execute: r1 goes to front (end of list)
        command.execute();
        assertEquals(r2, shapes.get(0));
        assertEquals(r1, shapes.get(1)); // now r1 is at index 1 (top/front)
    }

    @Test
    void testUndoRestoresOriginalPosition() {
        // Setup initial shape list
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle r1 = new ShapeRectangle(0, 0, 50, 50, Color.BLACK, Color.BLUE, 1.0);
        ShapeRectangle r2 = new ShapeRectangle(10, 10, 50, 50, Color.BLACK, Color.RED, 1.0);
        shapes.add(r1);
        shapes.add(r2);

        // r1 is selected, initially at index 0
        ShapeSelector selector = new ShapeSelector(shapes, r1,null,null);
        ToFrontCommand command = new ToFrontCommand(selector, 0, shapes.size());

        // Execute: r1 to front
        command.execute();
        assertEquals(r1, shapes.get(1));

        // Undo: r1 should go back to index 0
        command.undo();
        assertEquals(r1, shapes.get(0));
        assertEquals(r2, shapes.get(1));
    }
}
