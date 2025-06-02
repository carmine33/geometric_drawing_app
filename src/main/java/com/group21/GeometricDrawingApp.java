/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.group21;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        // Carica l’icona e assegnala alla finestra
        Image icon = new Image(getClass().getResourceAsStream("/icons/app_icon.png"));
        primaryStage.getIcons().add(icon);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        //per caricare il tema
        scene.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
        primaryStage.show();

    }

}

