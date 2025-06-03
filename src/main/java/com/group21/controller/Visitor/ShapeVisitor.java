/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group21.controller.Visitor;
import com.group21.model.Shape.*;

/**
 *
 * @author Loren
 */
public interface ShapeVisitor {
    void visit(ShapeRectangle rectangle);
    void visit(ShapeEllipse ellipse);
    void visit(ShapeLine line);
    void visit(ShapePolygon polygon);
    void visit(ShapeTextBox textBox);
}
