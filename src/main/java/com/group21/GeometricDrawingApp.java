/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.group21;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GeometricDrawingApp extends Application {

    public static Stage stage;

    //static public Select selectShape;   
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage)throws Exception{
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        primaryStage.setTitle("Geometric Drawing App - UNISA Group21");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

}

