/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Memory;

import java.util.Stack;
import javafx.scene.control.ColorPicker;
import com.group21.model.Shape.*;

/**
 *
 * @author matte
 */
public class Memory {

    private Stack<ShapeBase> stackShape;
    private Stack<Double> stackDouble;
    private Stack<ColorPicker> stackColor;
    private ShapeBase copiedShape;

    public Memory() {
        this.stackShape = new Stack<>();
        this.stackDouble = new Stack<>();
        this.stackColor = new Stack<>();
    }

    public void addStackShape(ShapeBase selectedShape) {
        this.stackShape.add(selectedShape);
    }
    
    public ShapeBase popStackShape() {
        return this.stackShape.pop();
    }

    public void setCopiedShape(ShapeBase shape) {
    this.copiedShape = shape;
}

    public ShapeBase getCopiedShape() {
    return this.copiedShape;
    }
}
