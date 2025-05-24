/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

/**
 *
 * @author mikel
 */
public class ModStrColorCommand implements Command{

    private ShapeSelector shape;
    
    public ModStrColorCommand(ShapeSelector shape) {
        this.shape = shape;
    }    

    @Override
    public void execute() {
         shape.ModStrColorShape();
    }
    
    @Override
    public void undo() {
         
    }
}
