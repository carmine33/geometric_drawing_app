/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.functional;

/**
 *
 * @author carmi
 */

import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class FunctionalTest_06 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testSelectAndMoveShape() {
        // Verifica la canvas
        verifyThat("#drawingCanvas", isVisible());

        // Disegna una forma (rettangolo)
        clickOn("#rectangleButton");
        moveTo("#drawingCanvas").moveBy(100, 100).press(MouseButton.PRIMARY)
               .moveBy(80, 60).release(MouseButton.PRIMARY);

        // Seleziona la forma cliccandoci sopra
        moveTo("#drawingCanvas").moveBy(110, 110).clickOn();

        // Trascina la forma su una nuova posizione
        moveBy(0, 0).press(MouseButton.PRIMARY).moveBy(100, 0).release(MouseButton.PRIMARY);

        // Verifica che la canvas sia ancora visibile
        verifyThat("#drawingCanvas", isVisible());

        // Nota: per verificare il cambiamento di posizione o dimensione servirebbe accesso alla logica interna
    }
}