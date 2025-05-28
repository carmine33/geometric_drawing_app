/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

/**
 *
 * @author carmi
 */


import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeRectangle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeSelectorTest {

    private ShapeSelector shapeSelector;
    private List<ShapeBase> shapeList;
    private ShapeRectangle shape;

    @BeforeEach
    public void setUp() {
        shapeList = new ArrayList<>();
        shape = new ShapeRectangle(10, 10, 20, 20, null, null, 1.0);
        shapeList.add(shape);
        shapeSelector = new ShapeSelector(shapeList, shape, null, null);
    }

    @Test
    public void testDeleteShape() {
        shapeSelector.deleteShape();
        assertTrue(shapeList.isEmpty(), "Shape list should be empty after deletion");
    }

    @Test
    public void testToFront() {
        ShapeRectangle second = new ShapeRectangle(30, 30, 20, 20, null, null, 1.0);
        shapeList.add(second);
        shapeSelector.setSelectedShape(shape);
        shapeSelector.toFront(0);
        assertEquals(second, shapeList.get(0), "Second shape should now be at index 0");
        assertEquals(shape, shapeList.get(1), "Selected shape should be moved forward");
    }

    @Test
    public void testToBack() {
        ShapeRectangle first = new ShapeRectangle(5, 5, 20, 20, null, null, 1.0);
        shapeList.add(0, first);
        shapeSelector.setSelectedShape(shape);
        shapeSelector.toBack(0);
        assertEquals(shape, shapeList.get(0), "Selected shape should be moved backward");
        assertEquals(first, shapeList.get(1), "First shape should now be after selected shape");
    }

    @Test
    public void testCopyAndPasteShape() {
        shapeSelector.setSelectedShape(shape);
        shapeSelector.copyShape();
        ShapeBase pasted = shapeSelector.pasteShape();
        assertNotNull(pasted, "Pasted shape should not be null");
        assertEquals(2, shapeList.size(), "List should contain original and copied shape");
    }

    @Test
    public void testSetAndGetSelectedShape() {
        shapeSelector.setSelectedShape(null);
        assertNull(shapeSelector.getSelectedShape(), "Selected shape should be null after reset");
        shapeSelector.setSelectedShape(shape);
        assertEquals(shape, shapeSelector.getSelectedShape(), "Selected shape should be the one set");
    }
}

