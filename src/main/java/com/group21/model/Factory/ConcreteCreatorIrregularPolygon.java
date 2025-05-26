/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Factory;

import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapePolygon;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author matte
 */
public class ConcreteCreatorIrregularPolygon extends Creator {

    @Override
    public ShapeBase createShape(double x, double y, Color strokeColor, Color fillColor) {
        // Crea un poligono vuoto con il primo punto già incluso
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x, y));
        return new ShapePolygon(points, fillColor, strokeColor, 1.0);
    }

}
