/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import com.group21.model.Shape.*;
import com.group21.model.Memory.*;

/**
 *
 * @author matte
 */
public class ShapeSelector {

    private List<ShapeBase> list;
    private ShapeBase selectedShape;
    private Memory memory;

    public ShapeSelector(List<ShapeBase> listShape, ShapeBase selectedShape) {
        this.list = listShape;
        this.selectedShape = selectedShape;
        this.memory = new Memory();

    }
    
     public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(ShapeBase selectedShape) {
        this.selectedShape = selectedShape;
    }

    public void deleteShape() {

        if (this.selectedShape == null) {
            return;
        }

        this.list.remove(this.selectedShape);
        this.memory.addStackShape(this.selectedShape);
    }

}
