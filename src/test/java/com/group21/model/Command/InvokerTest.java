/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import com.group21.model.Shape.*;

/**
 *
 * @author matte
 */
public class InvokerTest {

    @Test
    void testStartCommandAndUndoWithToFrontCommand() {
        // Setup initial shapes
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle rect1 = new ShapeRectangle(0, 0, 100, 100, Color.BLACK, Color.BLUE, 1.0);
        ShapeRectangle rect2 = new ShapeRectangle(10, 10, 100, 100, Color.BLACK, Color.RED, 1.0);
        shapes.add(rect1);
        shapes.add(rect2);

        // rect1 is initially selected (index 0)
        ShapeSelector selector = new ShapeSelector(shapes, rect1, null, null);

        // Create the command to move rect1 to front (end of list)
        ToFrontCommand toFrontCommand = new ToFrontCommand(selector, 0, shapes.size());

        // Create and use invoker
        Invoker invoker = new Invoker();
        invoker.setCommand(toFrontCommand);
        invoker.startCommand();  // Execute

        // rect1 should now be at the end of the list
        assertEquals(rect2, shapes.get(0));
        assertEquals(rect1, shapes.get(1));

        // Undo the command
        invoker.startUndo();

        // rect1 should be back at index 0
        assertEquals(rect1, shapes.get(0));
        assertEquals(rect2, shapes.get(1));
    }

    @Test
    void testMultipleCommandsAndUndoOrder() {
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle rect1 = new ShapeRectangle(0, 0, 100, 100, Color.BLACK, Color.BLUE, 1.0);
        ShapeRectangle rect2 = new ShapeRectangle(10, 10, 100, 100, Color.BLACK, Color.RED, 1.0);
        shapes.add(rect1);
        shapes.add(rect2);

        ShapeSelector selector = new ShapeSelector(shapes, rect2, null,null);
        Invoker invoker = new Invoker();

        // Move rect2 to back (index 0)
        ToBackCommand toBackCommand = new ToBackCommand(selector, 1);
        invoker.setCommand(toBackCommand);
        invoker.startCommand();

        assertEquals(rect2, shapes.get(0));

        // Now move it to front again
        ToFrontCommand toFrontCommand = new ToFrontCommand(selector, 0, shapes.size());
        invoker.setCommand(toFrontCommand);
        invoker.startCommand();

        assertEquals(rect2, shapes.get(1));

        // Undo the last command (move to front)
        invoker.startUndo();
        assertEquals(rect2, shapes.get(0));

        // Undo the previous command (move to back)
        invoker.startUndo();
        assertEquals(rect2, shapes.get(1)); // back to original position
    }
}
