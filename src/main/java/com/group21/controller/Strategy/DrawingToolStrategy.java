/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group21.controller.Strategy;

/**
 *
 * @author carmi
 */

import com.group21.model.Shape.ShapeBase;
import javafx.scene.input.MouseEvent;

public interface DrawingToolStrategy {
    void onMousePressed(MouseEvent e);
    void onMouseDragged(MouseEvent e);
    void onMouseReleased(MouseEvent e);
    void onMouseMoved(MouseEvent e);
    ShapeBase getPreviewShape();
    void setOffset(double offsetX, double offsetY, double zoomFactor);

}

