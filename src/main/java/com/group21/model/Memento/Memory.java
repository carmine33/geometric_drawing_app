/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Memento;

import java.util.Stack;
import com.group21.model.Shape.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matte
 */

public class Memory {

    private ShapeBase copiedShape;
    private Stack<Memento> history;

    public Memory() {
        this.history = new Stack<>();
    }

    public void setCopiedShape(ShapeBase shape) {
        this.copiedShape = shape;
    }

    public ShapeBase getCopiedShape() {
        return this.copiedShape;
    }

    public void saveState(List<ShapeBase> shapes) {
        history.push(new Memento(shapes));
    }

    public List<ShapeBase> restoreLastState() {
        if (!history.isEmpty()) {
            return history.pop().getSavedShapes();
        }
        return new ArrayList<>();
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }
}

