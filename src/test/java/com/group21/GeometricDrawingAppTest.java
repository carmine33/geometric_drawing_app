/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
/**
 *
 * @author carmi
 */



public class GeometricDrawingAppTest {

    @Test
    public void testMainWindowLaunch() {
        assertDoesNotThrow(() -> GeometricDrawingApp.main(new String[]{}));
    }
}

