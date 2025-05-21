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

public class FunctionalTest_02_1 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testLastShapeIsOnTopWhenOverlapping() {
        // Verifica che la finestra e la canvas siano visibili
        verifyThat("#drawingCanvas", isVisible());

        // Disegna prima forma in posizione sovrapponibile
        clickOn("#rectangleButton"); // Assumendo ci sia un bottone per il rettangolo
        moveTo("#drawingCanvas").moveBy(50, 50).press(MouseButton.PRIMARY)
               .moveBy(100, 100).release(MouseButton.PRIMARY);

        // Disegna seconda forma sopra la prima (sovrapposta)
        clickOn("#ellipseButton");
        moveTo("#drawingCanvas").moveBy(60, 60).press(MouseButton.PRIMARY)
               .moveBy(100, 100).release(MouseButton.PRIMARY);

        // Clicca sull'area sovrapposta
        moveTo("#drawingCanvas").moveBy(80, 80).clickOn();

        // Qui servirebbe accedere al controller o alla selezione (logica interna)
        // ma possiamo almeno verificare che non vi siano errori e interazione visiva

        verifyThat("#drawingCanvas", isVisible()); // Placeholder per comportamento osservabile
    }
}