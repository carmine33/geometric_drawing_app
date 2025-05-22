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

public class ShapeSelectorTest {

    @Test
    void testSetAndGetSelectedShape() {
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle rect = new ShapeRectangle(0, 0, 100, 100, Color.BLACK, Color.BLUE, 1.0);
        ShapeSelector selector = new ShapeSelector(shapes, null,null,null);

        selector.setSelectedShape(rect);
        assertEquals(rect, selector.getSelectedShape());
    }

    @Test
    void testDeleteShapeRemovesFromListAndClearsSelection() {
        List<ShapeBase> shapes = new ArrayList<>();
        ShapeRectangle rect = new ShapeRectangle(0, 0, 100, 100, Color.BLACK, Color.BLUE, 1.0);
        shapes.add(rect);

        ShapeSelector selector = new ShapeSelector(shapes, rect,null,null);

        assertTrue(shapes.contains(rect));
        assertEquals(rect, selector.getSelectedShape());

        selector.deleteShape();

        assertFalse(shapes.contains(rect));
    }
}
