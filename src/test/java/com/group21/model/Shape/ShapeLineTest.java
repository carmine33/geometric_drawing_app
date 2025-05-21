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

public class ShapeLineTest {

    private GraphicsContext dummyGC() {
        return new Canvas(200, 200).getGraphicsContext2D();
    }

    @Test
    void testLine() {
        ShapeLine line = new ShapeLine(
            0, 0,        // startX, startY
            100, 100,    // width, height
            100, 100,    // endX, endY
            Color.GRAY,
            2.0
        );

        assertEquals(100, line.getEndX());
        assertEquals(100, line.getEndY());

        assertDoesNotThrow(() -> line.draw(dummyGC()));

        assertTrue(line.containsPoint(0, 0), "Expected start point to be on line");
        assertTrue(line.containsPoint(100, 100), "Expected end point to be on line");
        assertFalse(line.containsPoint(50, 0), "Expected point off the line");

        assertNotNull(line.copy());
        assertNotNull(line.clone());
    }
}
