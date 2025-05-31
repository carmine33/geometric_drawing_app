/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Strategy;

/**
 *
 * @author carmi
 */

import javafx.scene.input.MouseEvent;

public class DrawingToolContext {
    private DrawingToolStrategy strategy;

    public void setStrategy(DrawingToolStrategy strategy) {
        this.strategy = strategy;
    }

    public void handleMousePressed(MouseEvent e) {
        if (strategy != null) strategy.onMousePressed(e);
    }

    public void handleMouseDragged(MouseEvent e) {
        if (strategy != null) strategy.onMouseDragged(e);
    }

    public void handleMouseReleased(MouseEvent e) {
        if (strategy != null) strategy.onMouseReleased(e);
    }
    
    public DrawingToolStrategy getStrategy() {
        return strategy;
    }
    
    public void handleMouseMoved(MouseEvent e) {
    if (strategy != null) {
        strategy.onMouseMoved(e);
    }
}

}

