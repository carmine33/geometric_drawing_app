/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

/**
 *
 * @author carmi
 */
public class CutCommand implements Command{
    
     private ShapeSelector shape;
    
    public CutCommand(ShapeSelector shape) {
        this.shape = shape;
    }
    
    @Override
    public void execute() {
         shape.cutShape();
    }
  
}
