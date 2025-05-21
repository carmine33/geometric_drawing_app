/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Decorator;

/**
 *
 * @author carmi
 */
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BaseCanvasTest {

    @Test
    void testCanvasAndGraphicsContextAreNotNull() {
        BaseCanvas baseCanvas = new BaseCanvas(300, 200);
        assertNotNull(baseCanvas.getCanvas(), "Canvas should not be null");
        assertNotNull(baseCanvas.getGc(), "GraphicsContext should not be null");
    }

    @Test
    void testClearDoesNotThrow() {
        BaseCanvas baseCanvas = new BaseCanvas(300, 200);
        assertDoesNotThrow(baseCanvas::clear, "Calling clear() should not throw any exception");
    }

    @Test
    void testExecuteIsCallable() {
        BaseCanvas baseCanvas = new BaseCanvas(300, 200);
        assertDoesNotThrow(baseCanvas::execute, "Calling execute() should not throw any exception");
    }
}
