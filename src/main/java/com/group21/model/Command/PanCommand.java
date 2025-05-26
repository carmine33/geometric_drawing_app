package com.group21.model.Command;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;

public class PanCommand implements Command {
    private double lastMouseX, lastMouseY;
    private boolean active = false;

    private final Pane canvas;
    private final ScrollPane scrollPane;

    public PanCommand(Pane canvas, ScrollPane scrollPane) {
        this.canvas = canvas;
        this.scrollPane = scrollPane;
    }

    @Override
    public void execute() {
        if (active) {
            // Se è già attivo, lo disattivo
            canvas.setOnMousePressed(null);
            canvas.setOnMouseDragged(null);
            canvas.setOnMouseReleased(null);
            active = false;
        } else {
            // Altrimenti attivo il pan
            canvas.setOnMousePressed(event -> {
                lastMouseX = event.getSceneX();
                lastMouseY = event.getSceneY();
                event.consume();
            });

            canvas.setOnMouseDragged(event -> {
                double deltaX = lastMouseX - event.getSceneX();
                double deltaY = lastMouseY - event.getSceneY();

                scrollPane.setHvalue(clamp(scrollPane.getHvalue() + deltaX / canvas.getWidth(), 0, 1));
                scrollPane.setVvalue(clamp(scrollPane.getVvalue() + deltaY / canvas.getHeight(), 0, 1));

                lastMouseX = event.getSceneX();
                lastMouseY = event.getSceneY();
                event.consume();
            });

            canvas.setOnMouseReleased(event -> event.consume());

            active = true;
        }
    }

    @Override
    public void undo() {
        // Metodo richiesto dall'interfaccia Command, ma lasciato vuoto
    }

    public boolean isActive() {
        return active;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
