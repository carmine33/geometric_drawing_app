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

public class FunctionalTest_02_2 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testDeleteSelectedShape() {
        // Verifica che la canvas sia visibile
        verifyThat("#drawingCanvas", isVisible());

        // Disegna una forma
        clickOn("#rectangleButton");
        moveTo("#drawingCanvas").moveBy(100, 100).press(MouseButton.PRIMARY)
               .moveBy(100, 100).release(MouseButton.PRIMARY);

        // Seleziona la forma cliccandoci sopra
        moveTo("#drawingCanvas").moveBy(120, 120).clickOn();

        // Cancella la forma selezionata
        clickOn("#deleteButton");

        // Verifica che la canvas sia ancora visibile dopo l'eliminazione
        verifyThat("#drawingCanvas", isVisible());
    }
}
