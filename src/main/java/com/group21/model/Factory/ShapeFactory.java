/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Factory;

/**
 *
 * @author matte
 */
public class ShapeFactory {

    public static Creator getCreator(String mode) {
        switch (mode) {
            case "Line":
                return new ConcreteCreatorLine();
            case "Rectangle":
                return new ConcreteCreatorRectangle();
            case "Ellipse":
                return new ConcreteCreatorEllipse();
            case "Text":
                return new ConcreteCreatorText();
            case "IrregularPolygon":
                return new ConcreteCreatorIrregularPolygon();
            default:
                throw new IllegalArgumentException("Unsupported shape mode: " + mode);
        }
    }
}
