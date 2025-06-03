/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author carmi
 */


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeRectangleTest { 

    private GraphicsContext dummyGC() {
        return new Canvas(200, 200).getGraphicsContext2D();
    }

    @Test
    void testRectangle() {
        ShapeRectangle rect = new ShapeRectangle(10, 20, 100, 50, Color.BLACK, Color.RED, 2.0);
        assertEquals(10, rect.getX());
        assertEquals(20, rect.getY());
        assertEquals(100, rect.getWidth());
        assertEquals(50, rect.getHeight());
        rect.draw(dummyGC());
        assertTrue(rect.containsPoint(50, 40));
        assertFalse(rect.containsPoint(200, 200));
        assertNotNull(rect.copy());
    }
}

