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
public class ToBackCommand implements Command {

    private ShapeSelector shape;
    private double index;

    public ToBackCommand(ShapeSelector shape, double index) {
        this.shape = shape;
        this.index = index;

    }

    @Override
    public void execute() {

        this.shape.toBack(0);
    }

    @Override
    public void undo() {
        
        ShapeBase oldShape = (ShapeBase) this.shape.getMemory().popStackShape();
        this.shape.getShape().remove(oldShape);
        this.shape.getShape().add((int) index, oldShape);

    }

}
