/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.model.Shape.*;

/**
 *
 * @author matte
 */
public class ToFrontCommand implements Command {

    private ShapeSelector shape;
    private double index;
    private double size;

    public ToFrontCommand(ShapeSelector shape, double index, double size) {
        this.shape = shape;
        this.index = index;
        this.size = size;

    }

    @Override
    public void execute() {

        this.shape.toFront(size);
    }

    @Override
    public void undo() {

        ShapeBase oldShape = (ShapeBase) this.shape.getMemory().popStackShape();
        this.shape.getShape().remove(oldShape);
        this.shape.getShape().add((int) index, oldShape);

    }

}
