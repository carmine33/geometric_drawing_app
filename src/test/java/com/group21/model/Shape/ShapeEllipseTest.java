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

public class ShapeEllipseTest {

    private GraphicsContext dummyGC() {
        return new Canvas(200, 200).getGraphicsContext2D();
    }

    @Test
    void testEllipse() {
        ShapeEllipse ellipse = new ShapeEllipse(30, 40, 80, 60, Color.BLUE, Color.GREEN, 5);
        assertEquals(30, ellipse.getX());
        assertEquals(40, ellipse.getY());
        ellipse.draw(dummyGC());
        assertTrue(ellipse.containsPoint(70, 70));
        assertFalse(ellipse.containsPoint(200, 200));
        assertNotNull(ellipse.copy());
        assertNotNull(ellipse.clone());
    }
}

