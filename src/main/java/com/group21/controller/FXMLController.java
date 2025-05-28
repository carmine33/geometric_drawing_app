package com.group21.controller;

import com.group21.model.Decorator.BaseCanvas;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

// Added for additional tools
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;

// Shapes
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeEllipse;
import com.group21.model.Shape.ShapeLine;
import com.group21.model.Shape.ShapeRectangle;
import com.group21.model.Shape.ShapePolygon;

import com.group21.model.Command.*;

// Import for save/load 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group21.model.Decorator.GridDecorator;
import com.group21.model.Factory.ConcreteCreatorEllipse;
import com.group21.model.Factory.ConcreteCreatorIrregularPolygon;
import com.group21.model.Factory.ConcreteCreatorLine;
import com.group21.model.Factory.ConcreteCreatorRectangle;
import com.group21.model.Factory.ConcreteCreatorText;
import com.group21.model.Factory.Creator;
import com.group21.model.Factory.ShapeFactory;
import com.group21.model.Shape.ShapeTextBox;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.util.Arrays;
import java.util.Iterator;

//import java.util.Iterator;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
// Import for Polygon shape manipulation
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon; 
import static org.locationtech.jts.math.MathUtil.clamp;


/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author claco
 */
public class FXMLController implements Initializable {
    
    //@FXML private MenuItem menuNew, menuOpen, menuSave, menuExit;
    
    @FXML private ScrollPane scrollPane;
    @FXML private Pane canvasPlaceholder;
    private BaseCanvas baseCanvas;
    @FXML private ColorPicker fillColorPicker;
    @FXML private ColorPicker strokeColorPicker;
    @FXML private Button btnRectangle;
    @FXML private Button btnEllipse;
    @FXML private Button btnLine;
    @FXML private Button btnPolygon;
    @FXML private Button btnSelect;
    @FXML private Button btnTextBox;
    @FXML private Button btnZoomIn;
    @FXML private Button btnZoomOut;
    //@FXML private Button btnPan;
    @FXML private Button btnToggleGrid;
    
    private Invoker invoker;
    private ShapeSelector selectShape;
    private List<ShapeBase> shapes = new ArrayList<>();
    ContextMenu contextMenu = new ContextMenu();
    MenuItem deleteMenu = new MenuItem("Delete");
    MenuItem copyShape = new MenuItem("Copy");
    MenuItem pasteShape = new MenuItem("Paste");
    MenuItem setStrokeWidth = new MenuItem("Set border thickness");
    MenuItem setStrokeColor = new MenuItem("Set border color");
    MenuItem setFillColor = new MenuItem("Set fill color");
    MenuItem modifyTextBox = new MenuItem("Modify text");
    MenuItem toFront = new MenuItem("ToFront");
    MenuItem toBack = new MenuItem("ToBack");

    private static final int CANVAS_MARGIN = 30; // margine uniforme
    private ShapeBase copiedShape = null;
    ShapePolygon currentPolygon = null;
    private List<Point2D> polygonPoints = new ArrayList<>();
    private String currentMouseCommand = null;
    private double lineStartX, lineStartY;
    private boolean isDrawingLine = false, isDrawingRectangle = false, isDrawingEllipse = false, 
                    isDrawingPolygon = false, isSelected = false, isDrawingTextBox=false, isPanMode=false;
    private Command command = null;
    private String mod = null;
    private double previewStartX, previewStartY;
    private double previewCurrentX, previewCurrentY;
    private ShapeBase previewShape = null;
    
    private final double[] zoomLevels = {0.25,0.5, 1.0, 1.5, 1.75};
    private int currentZoomIndex = 2; // corrisponde a 1.0
    private double zoomFactor = zoomLevels[currentZoomIndex];
    
    private Point2D lastMousePos = null;
    private double lastMouseX;
    private double lastMouseY;
    private PanCommand panCommand;
    private GridDecorator gridDecorator;
    private boolean isGridVisible = false;
    private double currentMouseX;
    private double currentMouseY;
    
    private boolean isPanning = false;
    private double panStartX, panStartY;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(4000,4000);
        baseCanvas.getCanvas().setCache(false);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        
        selectShape = new ShapeSelector(shapes, null,fillColorPicker, strokeColorPicker);
        strokeColorPicker.setValue(Color.web("#000000"));
        invoker = new Invoker();
        panCommand = new PanCommand(canvasPlaceholder, scrollPane);
        gridDecorator = new GridDecorator(baseCanvas);
        
        btnRectangle.setOnAction(e -> currentMouseCommand = "Rectangle");
        btnEllipse.setOnAction(e -> currentMouseCommand = "Ellipse");
        btnLine.setOnAction(e -> currentMouseCommand = "Line");
        btnPolygon.setOnAction(e -> currentMouseCommand = "Polygon");
        btnSelect.setOnAction(e-> currentMouseCommand = "Select");
        btnTextBox.setOnAction(e -> currentMouseCommand = "TextBox");
        
         btnZoomIn.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon || isDrawingTextBox) return;
            if (currentZoomIndex < zoomLevels.length - 1) {
                currentZoomIndex++;
                zoomFactor = zoomLevels[currentZoomIndex];
                
                updateScrollPaneViewport();
                redraw(baseCanvas.getGc());
            }
        });
        
       /* btnPan.setOnAction(e -> {
            panCommand.execute(); // alterna attivazione/disattivazione
            if (panCommand.isActive()) {
                currentMouseCommand = "Pan";
            } else {
                currentMouseCommand = null;
            }
        });*/
 
        btnZoomOut.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon || isDrawingTextBox) return;
            if (currentZoomIndex > 0) {
                currentZoomIndex--;
                zoomFactor = zoomLevels[currentZoomIndex];
                
                updateScrollPaneViewport();
                redraw(baseCanvas.getGc());
            }
        });

        btnToggleGrid.setOnAction(e -> {
            isGridVisible = !isGridVisible;
            redraw(baseCanvas.getGc());
        });
        
        baseCanvas.getCanvas().setOnMousePressed(e ->  {
            if ("Pan".equals(currentMouseCommand)) {
                isPanning = true;
                panStartX = e.getSceneX();
                panStartY = e.getSceneY();
                return; // evita conflitti con altri comandi
            }

            if(contextMenu.isShowing()){
                contextMenu.hide();
            }
                        // Calcola gli offset dovuti allo scroll
            double scrollXOffset = scrollPane.getHvalue() *
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() *
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
            if(e.isPrimaryButtonDown() && currentMouseCommand != null &&
              !currentMouseCommand.isEmpty() && !"Select".equals(currentMouseCommand)){
                
                redraw(baseCanvas.getGc());
                lineStartX = (e.getX() / zoomFactor) + scrollXOffset;
                lineStartY = (e.getY() / zoomFactor) + scrollYOffset;
                
                previewStartX = lineStartX;
                previewStartY = lineStartY;
                previewCurrentX = lineStartX;
                previewCurrentY = lineStartY;
                
                if(!isSelected && selectShape.getSelectedShape()!= null){
                    selectShape.setSelectedShape(null);
                }else if ("Line".equals(currentMouseCommand)) {
                    isDrawingLine = true;
                } else if("Rectangle".equals(currentMouseCommand)){
                    isDrawingRectangle = true;
                } else if("Ellipse".equals(currentMouseCommand)){
                    isDrawingEllipse = true;
                }else if("Polygon".equals(currentMouseCommand)){
                    isDrawingPolygon = true;
                    if (polygonPoints.isEmpty()) {
                        ConcreteCreatorIrregularPolygon creator = new ConcreteCreatorIrregularPolygon();
                        ShapePolygon newPolygon = (ShapePolygon) creator.createShape(lineStartX, lineStartY,
                            strokeColorPicker.getValue(), fillColorPicker.getValue());

                        polygonPoints = new ArrayList<>(newPolygon.getVertices());
                    }
                    polygonPoints.add(new Point2D(lineStartX, lineStartY));
                }else if("TextBox".equals(currentMouseCommand)){
                    isDrawingTextBox = true;
                }
            } else if(e.isSecondaryButtonDown()){
                if(!isDrawingPolygon && !contextMenu.isShowing()){
                    select(e);
                    initContextMenu();
                }else{                    
                    // Finalize Polygon based on the number of vertices
                    if (polygonPoints.size() == 1) {
                        // DO NOTHING: only 1 point present in the polygon shape
                    }else if (polygonPoints.size() == 2) {
                        Point2D p1 = polygonPoints.get(0);
                        Point2D p2 = polygonPoints.get(1);
                        ShapeLine line = new ShapeLine(p1.getX(), p1.getY(), 0, 0,
                                           p2.getX(), p2.getY(),
                                           strokeColorPicker.getValue(), 1);
                        shapes.add(line);
                    }else if (polygonPoints.size() >= 3) {
                        ShapePolygon polygon = new ShapePolygon(new ArrayList<>(polygonPoints),
                                                    fillColorPicker.getValue(),
                                                    strokeColorPicker.getValue(), 1);
                        shapes.add(polygon);
                            polygon.getVertices().forEach(p -> 
        adjustCanvasSizeIfNeeded(p.getX() + 100, p.getY() + 100));
                    }
                    polygonPoints.clear();
                    isDrawingPolygon = false;
                    redraw(baseCanvas.getGc());
                }
                
                redraw(baseCanvas.getGc());
            } else if (e.isPrimaryButtonDown() && "Select".equals(currentMouseCommand)){
                isSelected = true;
                // Stores OG vertices if we're selecting a polygon
                ShapeBase selected = selectShape.getSelectedShape();
                if (selected != null) {
                    selectShape.getMemory().saveState(new ArrayList<>(shapes));
                } 
                if(selected instanceof ShapePolygon){
                    ShapePolygon polygon = (ShapePolygon) selected;
                    polygon.storeOriginalVertices();
                    polygon.setResizeAnchor(polygon.getBoundingBoxTopLeft());
                }
            }
        });

        baseCanvas.getCanvas().setOnMouseReleased(e -> {
            if (isPanning) {
            isPanning = false;
            return;
        }
            lastMousePos = null;
            double scrollXOffset = scrollPane.getHvalue() *
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() *
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            double endX = (e.getX() / zoomFactor) + scrollXOffset;
            double endY = (e.getY() / zoomFactor) + scrollYOffset;

            if ("Line".equals(currentMouseCommand) && isDrawingLine) {
                isDrawingLine = false;
                ConcreteCreatorLine creator = (ConcreteCreatorLine) ShapeFactory.getCreator("Line");
                ShapeBase line = creator.createShape(lineStartX, lineStartY, endX, endY, strokeColorPicker.getValue());
                shapes.add(line);
                adjustCanvasSizeIfNeeded(endX + CANVAS_MARGIN, endY + CANVAS_MARGIN);
                redraw(baseCanvas.getGc());
            } else if ("Rectangle".equals(currentMouseCommand) && isDrawingRectangle) {
                double x = Math.min(lineStartX, endX);
                double y = Math.min(lineStartY, endY);
                double width = Math.abs(endX - lineStartX);
                double height = Math.abs(endY - lineStartY);

                if (width == 0) width = 100;
                if (height == 0) height = 40;

                isDrawingRectangle = false;

                Creator rectangleCreator = new ConcreteCreatorRectangle();
                ShapeBase rectangle = rectangleCreator.createShape(x, y, width, height,
                    strokeColorPicker.getValue(), fillColorPicker.getValue());

                shapes.add(rectangle);
                adjustCanvasSizeIfNeeded(x + width + CANVAS_MARGIN, y + height + CANVAS_MARGIN);
                redraw(baseCanvas.getGc());
            } else if ("Ellipse".equals(currentMouseCommand) && isDrawingEllipse) {
                double x = Math.min(lineStartX, endX);
                double y = Math.min(lineStartY, endY);
                double width = Math.abs(endX - lineStartX);
                double height = Math.abs(endY - lineStartY);

                if (width == 0) width = 100;
                if (height == 0) height = 40;

                isDrawingEllipse = false;

                Creator ellipseCreator = new ConcreteCreatorEllipse();
                ShapeBase ellipse = ellipseCreator.createShape(x, y, width, height,
                    strokeColorPicker.getValue(), fillColorPicker.getValue());

                shapes.add(ellipse);
                adjustCanvasSizeIfNeeded(x + width + CANVAS_MARGIN, y + height + CANVAS_MARGIN);
                redraw(baseCanvas.getGc());
            }else if ("TextBox".equals(currentMouseCommand) && isDrawingTextBox) {
                isDrawingTextBox = false;
                // 1. Inserisci il testo
                TextInputDialog textDialog = new TextInputDialog("Testo");
                textDialog.setTitle("Nuova TextBox");
                textDialog.setHeaderText("Inserisci il contenuto:");
                textDialog.setContentText("Testo:");
                Optional<String> resultText = textDialog.showAndWait();
                if (resultText.isEmpty()) return;

                // 2. Inserisci la dimensione
                TextInputDialog sizeDialog = new TextInputDialog("14");
                sizeDialog.setTitle("Dimensione font");
                sizeDialog.setHeaderText("Inserisci la dimensione del testo:");
                sizeDialog.setContentText("Dimensione:");
                Optional<String> resultSize = sizeDialog.showAndWait();
                double fontSize = 14;
                if (resultSize.isPresent()) {
                    try {
                        fontSize = Double.parseDouble(resultSize.get());
                    } catch (NumberFormatException ignored) {}
                }

                // 3. Scegli il tipo di font
                ChoiceDialog<String> fontDialog = new ChoiceDialog<>("Sans", "Sans", "Serif", "Monospace");
                fontDialog.setTitle("Tipo di font");
                fontDialog.setHeaderText("Scegli il font:");
                fontDialog.setContentText("Font:");
                Optional<String> resultFont = fontDialog.showAndWait();
                String fontFamily = "SansSerif";  // default Java name
                if (resultFont.isPresent()) {
                    switch (resultFont.get()) {
                        case "Serif": fontFamily = "Serif"; break;
                        case "Monospace": fontFamily = "Monospaced"; break;
                        default: fontFamily = "SansSerif"; break;
                    }
                }

                ConcreteCreatorText creator = new ConcreteCreatorText();
                ShapeTextBox newTextBox = creator.createShape(
                    endX, endY,
                    strokeColorPicker.getValue(),
                    fillColorPicker.getValue(),
                    resultText.get(),
                    fontSize
                );
                newTextBox.setFontSize(fontSize);
                newTextBox.setFontFamily(fontFamily);

                shapes.add(newTextBox);
                adjustCanvasSizeIfNeeded(newTextBox.getX() + newTextBox.getTextWidth() + CANVAS_MARGIN,
                         newTextBox.getY() + newTextBox.getTextHeight() + CANVAS_MARGIN);
                selectShape.setSelectedShape(newTextBox);
                redraw(baseCanvas.getGc());
    } else if("Select".equals(currentMouseCommand) && isSelected){
                select(e);
                isSelected = false; 
            }
        });
        
        baseCanvas.getCanvas().setOnMouseMoved(e -> {
            if (isDrawingPolygon && polygonPoints.size() >= 1) {
                double scrollXOffset = scrollPane.getHvalue() *
                    (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                double scrollYOffset = scrollPane.getVvalue() *
                    (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

                // Salva la posizione del mouse in coordinate del canvas
                currentMouseX = (e.getX() / zoomFactor) + scrollXOffset;
                currentMouseY = (e.getY() / zoomFactor) + scrollYOffset;

                // Redraw mostrerà anche il segmento corrente
                redraw(baseCanvas.getGc());
            }
        });


        
        baseCanvas.getCanvas().setOnMouseDragged(e -> {
            if (isPanning) {
                double deltaX = panStartX - e.getSceneX();
                double deltaY = panStartY - e.getSceneY();

                double newHValue = scrollPane.getHvalue() + deltaX / (baseCanvas.getCanvas().getWidth() * zoomFactor);
                double newVValue = scrollPane.getVvalue() + deltaY / (baseCanvas.getCanvas().getHeight() * zoomFactor);

                scrollPane.setHvalue(clamp(newHValue, 0, 1));
                scrollPane.setVvalue(clamp(newVValue, 0, 1));

                panStartX = e.getSceneX();
                panStartY = e.getSceneY();

                e.consume();
                return; // evita esecuzioni non necessarie
            }
                double scrollXOffset = scrollPane.getHvalue() * (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                double scrollYOffset = scrollPane.getVvalue() * (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
            if(!isDrawingPolygon && (isDrawingLine || isDrawingEllipse || isDrawingRectangle || isDrawingTextBox)){
                previewCurrentX = (e.getX() / zoomFactor) + scrollXOffset;
                previewCurrentY = (e.getY() / zoomFactor) + scrollYOffset;

                redraw(baseCanvas.getGc());

                GraphicsContext gc = baseCanvas.getGc();
                gc.save();

                // Applica scroll + zoom
                gc.translate(-scrollXOffset * zoomFactor, -scrollYOffset * zoomFactor);
                gc.scale(zoomFactor, zoomFactor);

                // Colori e stili
                gc.setStroke(strokeColorPicker.getValue());
                gc.setLineWidth(1.0);
                gc.setFill(fillColorPicker.getValue());

                // Disegna l'anteprima della figura
                if(isDrawingLine){
                    gc.strokeLine(previewStartX, previewStartY, previewCurrentX, previewCurrentY);

                }else if(isDrawingEllipse){
                    double x = Math.min(previewStartX, previewCurrentX);
                    double y = Math.min(previewStartY, previewCurrentY);
                    double w = Math.abs(previewCurrentX - previewStartX);
                    double h = Math.abs(previewCurrentY - previewStartY);
                    gc.fillOval(x, y, w, h);
                    gc.strokeOval(x, y, w, h);

                }else if(isDrawingRectangle){
                    double x = Math.min(previewStartX, previewCurrentX);
                    double y = Math.min(previewStartY, previewCurrentY);
                    double w = Math.abs(previewCurrentX - previewStartX);
                    double h = Math.abs(previewCurrentY - previewStartY);
                    gc.fillRect(x, y, w, h);
                    gc.strokeRect(x, y, w, h);
                }

                gc.restore();
            }else if(!isDrawingPolygon && !isDrawingLine && !isDrawingEllipse &&
                     !isDrawingRectangle && !isDrawingTextBox && isSelected){
                
                ShapeBase selected = selectShape.getSelectedShape();
                if (selected == null) return;
                
                // New mouse coordinates
double mouseX = (e.getX() / zoomFactor) + scrollXOffset;
double mouseY = (e.getY() / zoomFactor) + scrollYOffset;


                if (lastMousePos == null) {
                    lastMousePos = new Point2D(mouseX, mouseY);
                    return;
                }

                double dx = mouseX - lastMousePos.getX();
                double dy = mouseY - lastMousePos.getY();
               
                if(e.isControlDown()){
                    selectShape.getMemory().saveState(new ArrayList<>(shapes));
                    if (lastMousePos == null) {
                        lastMousePos = new Point2D(mouseX, mouseY);
                    }
                    if (selected instanceof ShapeRectangle) {
                        ShapeRectangle rect = (ShapeRectangle) selected;
                        rect.setX(rect.getX() + dx);
                        rect.setY(rect.getY() + dy);

                    } else if (selected instanceof ShapeEllipse) {
                        ShapeEllipse ellipse = (ShapeEllipse) selected;
                        ellipse.setX(ellipse.getX() + dx);
                        ellipse.setY(ellipse.getY() + dy);

                    } else if (selected instanceof ShapeLine) {
                        ShapeLine line = (ShapeLine) selected;
                        line.setX(line.getX() + dx);
                        line.setY(line.getY() + dy);
                        line.setEndX(line.getEndX() + dx);
                        line.setEndY(line.getEndY() + dy);

                    } else if (selected instanceof ShapePolygon) {
                        ShapePolygon polygon = (ShapePolygon) selected;
                        List<Point2D> movedVertices = new ArrayList<>();
                        for (Point2D pt : polygon.getVertices()) {
                            movedVertices.add(new Point2D(pt.getX() + dx, pt.getY() + dy));
                        }
                        polygon.setVertices(movedVertices);

                    } else if (selected instanceof ShapeTextBox) {
                        ShapeTextBox text = (ShapeTextBox) selected;
                        text.setX(text.getX() + dx);
                        text.setY(text.getY() + dy);
                    }

                    lastMousePos = new Point2D(mouseX, mouseY);
                }else if(!e.isControlDown()){
                    // Resize the figure according to the new mouse position
                    if (selected instanceof ShapeRectangle) {
                        selectShape.getMemory().saveState(new ArrayList<>(shapes));
                        ShapeRectangle rect = (ShapeRectangle) selected;
                        double newWidth = Math.max(10, mouseX - rect.getX());
                        double newHeight = Math.max(10, mouseY - rect.getY());
                        rect.setWidth(newWidth);
                        rect.setHeight(newHeight);

                    } else if (selected instanceof ShapeEllipse) {
                        ShapeEllipse ellipse = (ShapeEllipse) selected;
                        double newWidth = Math.max(10, mouseX - ellipse.getX());
                        double newHeight = Math.max(10, mouseY - ellipse.getY());
                        ellipse.setWidth(newWidth);
                        ellipse.setHeight(newHeight);

                    } else if (selected instanceof ShapeLine) {
                        ShapeLine line = (ShapeLine) selected;
                        line.setEndX(mouseX);
                        line.setEndY(mouseY);
                    }else if (selected instanceof ShapePolygon){
                        ShapePolygon polygon = (ShapePolygon) selected;
                        Point2D anchor = polygon.getResizeAnchor();
                        double anchorX = anchor.getX();
                        double anchorY = anchor.getY();

                        double initialWidth = polygon.getOriginalBoundingBoxWidth();
                        double initialHeight = polygon.getOriginalBoundingBoxHeight();
                        if (initialWidth == 0 || initialHeight == 0) return;

                        double newWidth = Math.max(10, e.getX() - anchorX);
                        double newHeight = Math.max(10, e.getY() - anchorY);

                        double scaleX = Math.max(0.1, newWidth / initialWidth);
                        double scaleY = Math.max(0.1, newHeight / initialHeight);

                        List<Point2D> scaled = new ArrayList<>();
                        for (Point2D pt : polygon.getOriginalVertices()) {
                            double poly_dx = pt.getX() - anchorX;
                            double poly_dy = pt.getY() - anchorY;
                            scaled.add(new Point2D(anchorX + poly_dx * scaleX, anchorY + poly_dy * scaleY));
                        }

                        polygon.setVertices(scaled);
                    }else if (selected instanceof ShapeTextBox) {
                        ShapeTextBox text = (ShapeTextBox) selected;

                        // Calcolo approssimativo: altezza del testo ≈ dimensione font
                        double newFontSize = Math.max(8, mouseY - text.getY());

                        text.setFontSize(newFontSize);
                    }
                }                
                redraw(baseCanvas.getGc());
            }
        });
    }
       
    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void redraw(GraphicsContext gc) {
        gc.clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        gc.save();
        
        // 1. Calcola gli offset reali in coordinate del canvas
        double scrollXOffset = scrollPane.getHvalue() * (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
        double scrollYOffset = scrollPane.getVvalue() * (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;


        // 2. Applica pan + zoom
        gc.translate(-scrollXOffset * zoomFactor, -scrollYOffset * zoomFactor);
        gc.scale(zoomFactor, zoomFactor);
        
        if (isGridVisible) {
            gridDecorator.drawGrid(gc, scrollXOffset, scrollYOffset, zoomFactor);
        }

        for (ShapeBase shape : shapes) {
            shape.draw(gc);
        }

        // Draw purple border around selected shape
        ShapeBase selected = selectShape.getSelectedShape();
        if (selected != null) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(3.0);

            if (selected instanceof ShapeRectangle) {
                ShapeRectangle rect = (ShapeRectangle) selected;
                gc.setLineWidth(2.0);
                gc.strokeRect(rect.getX() - 5, rect.getY() - 5,
                              rect.getWidth() + 10, rect.getHeight() + 10);

            } else if (selected instanceof ShapeEllipse) {
                ShapeEllipse ellipse = (ShapeEllipse) selected;
                double x = ellipse.getX();
                double y = ellipse.getY();
                double width = ellipse.getWidth();
                double height = ellipse.getHeight();
                gc.setLineWidth(2.0);
                gc.strokeOval(x - 5, y - 5, width + 10, height + 10);

            } else if (selected instanceof ShapeLine) {
                ShapeLine line = (ShapeLine) selected;
                gc.setLineWidth(2.0); // Thicker edge for visibility
                gc.strokeLine(line.getX(), line.getY(), line.getEndX(), line.getEndY());
            } else if (selected instanceof ShapePolygon){
                ShapePolygon polygon = (ShapePolygon) selected;
                List<Point2D> points = polygon.getVertices();
                Coordinate[] coords = points.stream()
                    .map(p -> new Coordinate(p.getX(), p.getY()))
                    .toArray(Coordinate[]::new);
                coords = Arrays.copyOf(coords, coords.length + 1);
                coords[coords.length - 1] = coords[0];

                GeometryFactory gf = new GeometryFactory();
                LinearRing ring = gf.createLinearRing(coords);
                Polygon jtsPoly = gf.createPolygon(ring);

                // Create buffer (offset)
                Geometry outline = jtsPoly.buffer(5.0); // 5 px outward

                // Extract and draw the offset polygon
                Coordinate[] offsetCoords = outline.getCoordinates();
                gc.beginPath();
                gc.setStroke(Color.RED);
                gc.setLineWidth(2.0);
                gc.moveTo(offsetCoords[0].x, offsetCoords[0].y);
                for (int i = 1; i < offsetCoords.length; i++) {
                    gc.lineTo(offsetCoords[i].x, offsetCoords[i].y);
                }
                gc.stroke();
                
            }else if (selected instanceof ShapeTextBox) {
                ShapeTextBox box = (ShapeTextBox) selected;

                double w = box.getTextWidth() + 11;   // margine orizzontale
                double h = box.getTextHeight() + 6;   // margine verticale
                double x = box.getX();
                double y = box.getY();

                gc.strokeRect(x, y, w, h);
            }
        }
         // Anteprima del poligono durante il disegno
        if (isDrawingPolygon && polygonPoints.size() >= 1 && currentMouseX >= 0 && currentMouseY >= 0) {
            gc.setStroke(strokeColorPicker.getValue());
            gc.setLineWidth(1.0);

            for (int i = 0; i < polygonPoints.size() - 1; i++) {
                Point2D p1 = polygonPoints.get(i);
                Point2D p2 = polygonPoints.get(i + 1);
                gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }

            Point2D last = polygonPoints.get(polygonPoints.size() - 1);
            gc.strokeLine(last.getX(), last.getY(), currentMouseX, currentMouseY);
        }

        gc.restore();
        if(previewShape!=null){
            previewShape.draw(gc);
        }
        canvasPlaceholder.requestLayout();
    }
    
private void adjustCanvasSizeIfNeeded(double requiredWidth, double requiredHeight) {
    double currentWidth = baseCanvas.getCanvas().getWidth();
    double currentHeight = baseCanvas.getCanvas().getHeight();

    if (requiredWidth > currentWidth || requiredHeight > currentHeight) {
        double newWidth = Math.max(currentWidth, requiredWidth);
        double newHeight = Math.max(currentHeight, requiredHeight);

        baseCanvas.getCanvas().setWidth(newWidth);
        baseCanvas.getCanvas().setHeight(newHeight);

        // Imposta il canvasPlaceholder alle stesse dimensioni del canvas
        canvasPlaceholder.setPrefSize(newWidth, newHeight);
        canvasPlaceholder.setMinSize(newWidth, newHeight);
        canvasPlaceholder.setMaxSize(newWidth, newHeight);
        canvasPlaceholder.resize(newWidth, newHeight);
        canvasPlaceholder.layout();
        scrollPane.layout();  // forza il ricalcolo della viewport della ScrollPane


        // Applica lo zoom come scaling (non va usato *zoomFactor nelle size)
        updateScrollPaneViewport();
    }
}




    
    // Select method for selecting shapes
    private void select(MouseEvent event) {
        double scrollXOffset = scrollPane.getHvalue() *
        (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
    double scrollYOffset = scrollPane.getVvalue() *
        (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

    double clickX = (event.getX() / zoomFactor) + scrollXOffset;
    double clickY = (event.getY() / zoomFactor) + scrollYOffset;

        // Scroll through the forms from newest to oldest
        for (int i = shapes.size() - 1; i >= 0; i--) {
            ShapeBase shape = shapes.get(i);

            // Check if the clicked point is inside the figure
            if (shape.containsPoint(clickX, clickY)) {
                // If so, set the figure as selected
                selectShape.setSelectedShape(shape);

                // Redraw the canvas with the new selection highlighted
                redraw(baseCanvas.getGc());
                return;
            }
        }

        // No figure found under click → deselect all
        selectShape.setSelectedShape(null);
        redraw(baseCanvas.getGc());
    }

    
    // Drop-down menù after right-click
     private void initContextMenu() {
        contextMenu.getItems().clear();
        contextMenu.getItems().addAll(deleteMenu, setStrokeColor, setStrokeWidth, setFillColor, copyShape, pasteShape,toFront,toBack);

        deleteMenu.setOnAction(e -> menuDeleteHandler());
        
        toFront.setOnAction(e -> toFront(shapes.indexOf(selectShape.getSelectedShape()), shapes.size()));
        toBack.setOnAction(e -> toBack(shapes.indexOf(selectShape.getSelectedShape())));

        setStrokeColor.setOnAction(e ->menuModifyColorStroke());

        setFillColor.setOnAction(e -> menuModifyColorFill());

        setStrokeWidth.setOnAction(e ->menuModifyWidthStroke());

        copyShape.setOnAction(e ->menuCopyHandler());

        pasteShape.setOnAction(e ->menuPasteHandler());
        

        baseCanvas.getCanvas().setOnContextMenuRequested(e -> {
    if (isDrawingPolygon) {
        e.consume();  // Impedisce l'apertura del context menu
    } else {
        contextMenu.show(baseCanvas.getCanvas(), e.getScreenX(), e.getScreenY());
    }
});
    }
    
     
    public void menuModifyColorStroke() {
        
        command = new ModColorCommand(selectShape,"stroke");
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
    } 
     
    public void menuModifyColorFill() {
        
        command = new ModColorCommand(selectShape,"fill");
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
        }

     
    
    public void menuModifyWidthStroke(){
        command = new ModStrWidthCommand(selectShape);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
    }  
    
    public void menuCopyHandler (){
        
        command = new CopyCommand(selectShape);
        command.execute();
        
    } 
     
    public void menuPasteHandler(){     
        command = new PasteCommand(selectShape);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());  
    }
     
     
     public void menuDeleteHandler() {
        command = new DeleteCommand(selectShape);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
    }
     
     //il command toFront è creato passando come argomenti la posizione della forma selezionata nella lista e la dimensione della lista stessa;
     //infine è invocato il command
      public void toFront(double index, double size) {
        command = new ToFrontCommand(selectShape, index, size);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());

    }
      
     //il command toBack è creato passando come argomento la posizione della forma selezionata nella lista;
     //infine è invocato il command
       public void toBack(double index) {
        command = new ToBackCommand(selectShape, index);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());

    }
       
    public void performUndo() {
        if (selectShape.getMemory().canUndo()) {
            shapes.clear();
            shapes.addAll(selectShape.getMemory().restoreLastState());
            redraw(baseCanvas.getGc());
        } else {
            showInfo("Undo", "Nessuno stato precedente disponibile.");
        }
    }
        
    // Save/Load logic implementation
    public void saveShapes(List<ShapeBase> shapes, File file){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(file, shapes);
            System.out.println("Saved " + shapes.size() + " shapes to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<ShapeBase> loadShapes(File file) {
        ObjectMapper mapper = new ObjectMapper();
        List<ShapeBase> shapes = null;

        try {
            shapes = mapper.readValue(file, 
                    mapper.getTypeFactory().constructCollectionType(List.class, ShapeBase.class));
            System.out.println("Loaded " + shapes.size() + " shapes from " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shapes;
    }

    // Dropdown menù handlers
    public void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shapes");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        Window window = baseCanvas.getCanvas().getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            saveShapes(shapes, file);
            showInfo("Save successful", "Saved " + shapes.size() + " shapes.");
        }
    }

    public void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Shapes");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        Window window = baseCanvas.getCanvas().getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            List<ShapeBase> loadedShapes = loadShapes(file);
            if (loadedShapes != null) {
                selectShape.getMemory().saveState(new ArrayList<>(shapes));
                shapes.clear();
                selectShape.setSelectedShape(null);
                shapes.addAll(loadedShapes);
                GraphicsContext gc = baseCanvas.getCanvas().getGraphicsContext2D();
                redraw(gc);
                showInfo("Load successful", "Loaded " + shapes.size() + " shapes.");
            }
        }
    }
    
private void updateScrollPaneViewport() {
    double canvasWidth = baseCanvas.getCanvas().getWidth();
    double canvasHeight = baseCanvas.getCanvas().getHeight();

    // Calcola le dimensioni scalate per il canvas
    double scaledWidth = canvasWidth * zoomFactor;
    double scaledHeight = canvasHeight * zoomFactor;

    // Imposta le dimensioni effettive del canvasPlaceholder (e quindi dello ScrollPane)
    canvasPlaceholder.setPrefWidth(scaledWidth);
    canvasPlaceholder.setPrefHeight(scaledHeight);
    canvasPlaceholder.setMinWidth(scaledWidth);
    canvasPlaceholder.setMinHeight(scaledHeight);
    canvasPlaceholder.setMaxWidth(scaledWidth);
    canvasPlaceholder.setMaxHeight(scaledHeight);

    scrollPane.setPannable(true);
    scrollPane.layout(); // forza aggiornamento layout
}






    @FXML
    private void handleNew() {
        selectShape.getMemory().saveState(new ArrayList<>(shapes));
        baseCanvas.clear();
        shapes.clear();
        selectShape.setSelectedShape(null);
        this.redraw(baseCanvas.getGc());
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit clicked");
        System.exit(0);
    }
    
     @FXML
    private void undoCommand(ActionEvent e) {
        performUndo();
    }
    
   /* @FXML
    private void setupPanTool() {
    btnPan.setOnAction(event -> {
        isPanMode = true;
        setActiveToolButton(btnPan);});
    
    
    

    baseCanvas.getCanvas().setOnMouseDragged(event -> {
        
            double deltaX = lastMouseX - event.getSceneX();
            double deltaY = lastMouseY - event.getSceneY();

            double newHValue = scrollPane.getHvalue() + deltaX / baseCanvas.getCanvas().getWidth();
            double newVValue = scrollPane.getVvalue() + deltaY / baseCanvas.getCanvas().getHeight();

            scrollPane.setHvalue(clamp(newHValue, 0, 1));
            scrollPane.setVvalue(clamp(newVValue, 0, 1));

            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
            event.consume();
        
        });   
    } */
    
   /* private void setActiveToolButton(Button activeButton) {
    // Rimuovi stile da tutti i bottoni degli strumenti
    btnPan.setStyle("");
    btnSelect.setStyle("");

    // Evidenzia il bottone attivo
    if (activeButton != null) {
        activeButton.setStyle("-fx-background-color: lightblue;");
    }
    
    }*/
    
}
