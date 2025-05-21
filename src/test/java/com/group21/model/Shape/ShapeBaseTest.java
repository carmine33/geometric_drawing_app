/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author carmi
 */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeBaseTest {

    static class DummyShape extends ShapeBase {
        public DummyShape(double x, double y, double width, double height, Color fill, Color stroke, double strokeWidth) {
            super(x, y, width, height, fill, stroke, strokeWidth);
        }

        @Override
        public void draw(GraphicsContext gc) {
            // dummy draw logic
        }

        @Override
        public ShapeBase copy() {
            return this;
        }

        @Override
        public boolean containsPoint(double x, double y) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    @Test
    void testGettersAndSetters() {
        DummyShape shape = new DummyShape(10, 20, 100, 50, Color.BLUE, Color.BLACK, 2.0);
        shape.setX(30);
        shape.setY(40);
        shape.setWidth(150);
        shape.setHeight(60);
        shape.setStrokeColor(Color.RED);
        shape.setFillColor(Color.GREEN);
        shape.setStrokeWidth(2.5);
        shape.setType("rectangle");

        assertEquals(30, shape.getX());
        assertEquals(40, shape.getY());
        assertEquals(150, shape.getWidth());
        assertEquals(60, shape.getHeight());
        assertEquals(Color.RED, shape.getStrokeColor());
        assertEquals(Color.GREEN, shape.getFillColor());
        assertEquals(2.5, shape.getStrokeWidth());
        assertEquals("rectangle", shape.getType());
    }

    @Test
    void testTranslate() {
        DummyShape shape = new DummyShape(0, 0, 10, 10, Color.BLUE, Color.BLACK, 1.0);
        shape.translate(5, 10);
        assertEquals(5, shape.getX());
        assertEquals(10, shape.getY());
    }
}
