/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.controller.Visitor.MirrorVisitor;
import com.group21.model.Shape.ShapeBase;

/**
 *
 * @author Loren
 */
public class MirrorCommand implements Command{
    
    private final ShapeBase shape;
    private final boolean horizontal;
    private final boolean vertical;
    private final double centerX;
    private final double centerY;

    public MirrorCommand(ShapeBase shape, boolean horizontal, boolean vertical, double centerX, double centerY) {
        this.shape = shape;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void execute() {
        MirrorVisitor visitor = new MirrorVisitor(horizontal, vertical, centerX, centerY);
        shape.accept(visitor);
    }
}
