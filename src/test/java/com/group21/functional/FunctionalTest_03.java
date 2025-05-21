/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.functional;

/**
 *
 * @author carmi
 */
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class FunctionalTest_03 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testShapeSelectionAndDrawing() {
        // Verifica canvas e toolbar
        verifyThat("#drawingCanvas", isVisible());
        verifyThat("#lineButton", isVisible());
        verifyThat("#rectangleButton", isVisible());
        verifyThat("#ellipseButton", isVisible());

        // Disegna una linea
        clickOn("#lineButton");
        moveTo("#drawingCanvas").moveBy(20, 20).press(MouseButton.PRIMARY)
               .moveBy(100, 0).release(MouseButton.PRIMARY);

        // Disegna un rettangolo
        clickOn("#rectangleButton");
        moveTo("#drawingCanvas").moveBy(40, 40).press(MouseButton.PRIMARY)
               .moveBy(80, 60).release(MouseButton.PRIMARY);

        // Disegna un'ellisse
        clickOn("#ellipseButton");
        moveTo("#drawingCanvas").moveBy(60, 60).press(MouseButton.PRIMARY)
               .moveBy(100, 40).release(MouseButton.PRIMARY);

        // Verifica che la canvas sia ancora visibile dopo i disegni
        verifyThat("#drawingCanvas", isVisible());
    }
}
