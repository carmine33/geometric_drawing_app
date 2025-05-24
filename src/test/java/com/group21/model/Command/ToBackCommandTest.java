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
public class ToBackCommandTest {

    @Test
    void testExecuteMovesShapeToBack() {
        // Setup initial shape list
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle r1 = new ShapeRectangle(0, 0, 50, 50, Color.BLACK, Color.BLUE, 1.0);
        ShapeRectangle r2 = new ShapeRectangle(10, 10, 50, 50, Color.BLACK, Color.RED, 1.0);
        shapes.add(r1);
        shapes.add(r2);

        // r2 is selected, currently at index 1
        ShapeSelector selector = new ShapeSelector(shapes, r2,null,null);
        ToBackCommand command = new ToBackCommand(selector, 1);

        // Execute should move r2 to index 0
        command.execute();
        assertEquals(r2, shapes.get(0));
        assertEquals(r1, shapes.get(1));
    }

    @Test
    void testUndoRestoresOriginalPosition() {
        // Setup initial shape list
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle r1 = new ShapeRectangle(0, 0, 50, 50, Color.BLACK, Color.BLUE, 1.0);
        ShapeRectangle r2 = new ShapeRectangle(10, 10, 50, 50, Color.BLACK, Color.RED, 1.0);
        shapes.add(r1);
        shapes.add(r2);

        // r2 is selected, currently at index 1
        ShapeSelector selector = new ShapeSelector(shapes, r2,null,null);
        ToBackCommand command = new ToBackCommand(selector, 1);

        // Execute: r2 goes to index 0
        command.execute();
        assertEquals(r2, shapes.get(0));
        assertEquals(r1, shapes.get(1));

        // Undo: r2 should return to index 1
        command.undo();
        assertEquals(r1, shapes.get(0));
        assertEquals(r2, shapes.get(1));
    }
}
