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

import com.group21.model.Command.*;

// Import for save/load 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group21.model.Decorator.GridDecorator;

import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import com.group21.controller.Strategy.*;
import com.group21.controller.Visitor.*;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author claco
 */
public class FXMLController implements Initializable {
       
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
    @FXML private Button btnPan;
    @FXML private Button btnToggleGrid;
    @FXML private Button btnUndo;
    
    private Invoker invoker;
    private ShapeSelector selectShape;
    private List<ShapeBase> shapes = new ArrayList<>();
    ContextMenu contextMenu = new ContextMenu();
    private List<Point2D> polygonPoints = new ArrayList<>();
    private String currentMouseCommand = null;
    private boolean isDrawingLine = false, isDrawingRectangle = false, isDrawingEllipse = false, 
                    isDrawingPolygon = false, isSelected = false, isDrawingTextBox = false, isPanMode = false;
    private Command command = null;
    private final double[] zoomLevels = {0.25,0.5, 1.0, 1.5, 1.75};
    private int currentZoomIndex = 2; // corrisponde a 1.0
    private double zoomFactor = zoomLevels[currentZoomIndex]; 
    private Point2D lastMousePos = null;
    private GridDecorator gridDecorator;
    private boolean isGridVisible = false;
    private double currentMouseX;
    private double currentMouseY;
    private DrawingToolContext toolContext = new DrawingToolContext();
    private ShapeBase previewShape;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(2000,2000);
        baseCanvas.getCanvas().setCache(false);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        setupShapeIcons();//crica icone dei bottoni
        scrollPane.setPannable(false);//scrollPane inizialmente inattivo
        selectShape = new ShapeSelector(shapes, null,fillColorPicker, strokeColorPicker);
        strokeColorPicker.setValue(Color.web("#000000"));
        invoker = new Invoker();
        gridDecorator = new GridDecorator(baseCanvas);
        //Strategy Pattern
        btnSelect.setOnAction(e -> {
            disablePanMode();
            currentMouseCommand = "Select";
        });
        btnLine.setOnAction(e -> {
            disablePanMode();
            currentMouseCommand = "Line";
            final LineTool[] toolHolder = new LineTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new LineTool(shapes, strokeColorPicker.getValue(), selectShape, callback);
            toolContext.setStrategy(toolHolder[0]);
        });
        btnRectangle.setOnAction(e -> {
            disablePanMode();
            currentMouseCommand = "Rectangle";
            final RectangleTool[] toolHolder = new RectangleTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new RectangleTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(),selectShape, callback);
            toolContext.setStrategy(toolHolder[0]);
        });
        btnEllipse.setOnAction(e -> {
            disablePanMode();
            currentMouseCommand = "Ellipse";
            final EllipseTool[] toolHolder = new EllipseTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new EllipseTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), selectShape, callback);
            toolContext.setStrategy(toolHolder[0]);
        });
        btnTextBox.setOnAction(e -> {
            disablePanMode();
            currentMouseCommand = "TextBox";
            final TextBoxTool[] toolHolder = new TextBoxTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new TextBoxTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), selectShape, callback);
            toolContext.setStrategy(toolHolder[0]);
        });
        btnPolygon.setOnAction(e -> {
            disablePanMode();
            currentMouseCommand = "Polygon";
            final PolygonTool[] toolHolder = new PolygonTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new PolygonTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), selectShape, callback);
            toolContext.setStrategy(toolHolder[0]);
        }); 
         btnZoomIn.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon || isDrawingTextBox) return;
            if (currentZoomIndex < zoomLevels.length - 1) {
                currentZoomIndex++;
                zoomFactor = zoomLevels[currentZoomIndex];
                updateScrollPaneViewport();
                redraw(baseCanvas.getGc());
            }
        });
        //attiva il panning dello scrollpane
        btnPan.setOnAction(e -> {
            boolean newPanState = !scrollPane.isPannable();
            scrollPane.setPannable(newPanState);
            if (newPanState) {
                currentMouseCommand = "Pan";
                toolContext.setStrategy(null); // disattiva qualsiasi tool
                selectShape.setSelectedShape(null); // ❗ rimuove selezione figura
                redraw(baseCanvas.getGc());         // ❗ aggiorna canvas senza selezione
            } else {
                currentMouseCommand = null;
            }
            setActiveToolButton(newPanState ? btnPan : null);
        });
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
        
        baseCanvas.getCanvas().setOnMousePressed(e -> {
           // Nascondi il context menu se visibile
            if (contextMenu.isShowing() && e.isPrimaryButtonDown()) {
                contextMenu.hide();
            }
           // Se tasto destro (secondary)
            if (e.isSecondaryButtonDown()) {
                // Se siamo in modalità disegno poligono, chiudi il poligono e consuma evento
                if ("Polygon".equals(currentMouseCommand)) {
                    // Logica per chiudere il poligono (ad esempio, toolContext.handleMousePressed(e))
                    toolContext.handleMousePressed(e);
                    e.consume(); // Consuma evento per non mostrare il context menu
                    return;      // Esci qui per evitare che si apra il menu
                }
               // Altrimenti (non in modalità poligono), mostra il context menu se non è già visibile
                if (!contextMenu.isShowing()) {
                    select(e);
                    initContextMenu();
                }
                // Offset per lo scroll e zoom
                double scrollXOffset = scrollPane.getHvalue() *
                    (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                double scrollYOffset = scrollPane.getVvalue() *
                    (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
                if (toolContext.getStrategy() != null) {
                    toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
                }
                toolContext.handleMousePressed(e);
            }
            // Se selezione attiva
            if ("Select".equals(currentMouseCommand)) {
               isSelected = true;
               select(e);  // <-- essenziale per selezionare figura cliccata
               ShapeBase selected = selectShape.getSelectedShape();
                if (selected != null) {
                   selectShape.getMemory().saveState(new ArrayList<>(shapes));
                }
            }
            // Altrimenti: modalità disegno (linea, rettangolo, ecc.)
            else {
               // Offset per lo scroll e zoom
                double scrollXOffset = scrollPane.getHvalue() *
                    (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                double scrollYOffset = scrollPane.getVvalue() *
                    (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
                if (toolContext.getStrategy() != null) {
                    toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
                }
                toolContext.handleMousePressed(e);
            }
        });

        baseCanvas.getCanvas().setOnMouseReleased(e -> {
            // Se non è tasto sinistro, ignora
            if (e.getButton() != MouseButton.PRIMARY) return;
            // Tool di disegno
            if (!"Select".equals(currentMouseCommand)) {
                // Offset per lo scroll e zoom
                double scrollXOffset = scrollPane.getHvalue() * 
                    (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                double scrollYOffset = scrollPane.getVvalue() * 
                    (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
                if (toolContext.getStrategy() != null) {
                    toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
                }
                toolContext.handleMouseReleased(e);
            }
            // Modalità selezione
            else if (isSelected) {
                select(e);
                isSelected = false;
            }
            lastMousePos = null;
        });
    
        baseCanvas.getCanvas().setOnMouseMoved(e -> {
            if (!"Select".equals(currentMouseCommand)) {
                // Offset per lo scroll e zoom
                    double scrollXOffset = scrollPane.getHvalue() * 
                        (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                    double scrollYOffset = scrollPane.getVvalue() * 
                        (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
                    if (toolContext.getStrategy() != null) {
                        toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
                    }
                toolContext.handleMouseMoved(e);
            }
        });
     
        baseCanvas.getCanvas().setOnMouseDragged(e -> {
            // Disegno anteprima solo con tasto sinistro premuto
            if (!"Select".equals(currentMouseCommand) && e.isPrimaryButtonDown()) {
                // Offset per lo scroll e zoom
                    double scrollXOffset = scrollPane.getHvalue() * 
                        (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                    double scrollYOffset = scrollPane.getVvalue() * 
                        (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
                    if (toolContext.getStrategy() != null) {
                        toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
                    }
                toolContext.handleMouseDragged(e);
            }
            // Gestione spostamento/ridimensionamento selezione
            else if ("Select".equals(currentMouseCommand)) {
                ShapeBase selected = selectShape.getSelectedShape();
                if (selected == null) return;
                double scrollXOffset = scrollPane.getHvalue() * (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
                double scrollYOffset = scrollPane.getVvalue() * (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
                double mouseX = (e.getX() / zoomFactor) + scrollXOffset;
                double mouseY = (e.getY() / zoomFactor) + scrollYOffset;
                if (lastMousePos == null) {
                    lastMousePos = new Point2D(mouseX, mouseY);
                    return;
                }
                double dx = mouseX - lastMousePos.getX();
                double dy = mouseY - lastMousePos.getY();
                if (e.isControlDown()) {
                    translateShape(selected, dx, dy);
                } else {
                    resizeShape(selected, mouseX, mouseY);
                }
                lastMousePos = new Point2D(mouseX, mouseY);
                redraw(baseCanvas.getGc());
            }
        });
        
        //forzano il ridisegno del canvas ad ogni scroll orizzontale o verticale
        scrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> redraw(baseCanvas.getGc()));
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> redraw(baseCanvas.getGc()));
    }
       
    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);  
        // Applica il CSS personalizzato al DialogPane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
        dialogPane.getStyleClass().add("root");  // o altra classe definita nel tuo CSS
        alert.showAndWait();
    }
 
    private void redraw(GraphicsContext gc) {
        gc.clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        gc.save();
        double scrollXOffset = scrollPane.getHvalue() *
            (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
        double scrollYOffset = scrollPane.getVvalue() *
            (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
        gc.translate(-scrollXOffset * zoomFactor, -scrollYOffset * zoomFactor);
        gc.scale(zoomFactor, zoomFactor);
        if (isGridVisible) {
            gridDecorator.drawGrid(gc, scrollXOffset, scrollYOffset, zoomFactor);
        }
        for (ShapeBase shape : shapes) {
            shape.draw(gc);
        }
        ShapeBase selected = selectShape.getSelectedShape();
        if (selected != null) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(3.0);
            ShapeVisitor visitor = new SelectionOutlineVisitor(gc);
            selected.accept(visitor);
        }
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
        if (toolContext.getStrategy() != null && toolContext.getStrategy().getPreviewShape() != null) {
            toolContext.getStrategy().getPreviewShape().draw(gc);
        }
        // Anteprima in tempo reale del poligono (aggiunta dinamica)
        ShapeBase preview = toolContext.getStrategy() != null ? toolContext.getStrategy().getPreviewShape() : null;
        if (preview != null) {
            preview.draw(gc);
        }
        gc.restore();
        canvasPlaceholder.requestLayout();
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
                lastMousePos = null;
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
        ShapeBase selected = selectShape.getSelectedShape();
        if (selected == null) {
            // Nessuna figura selezionata: mostra solo le azioni generali, per esempio Paste
            MenuItem paste = new MenuItem("Paste");
            paste.setOnAction(e -> menuPasteHandler());
            contextMenu.getItems().add(paste);
            return;
        }
        List<String> actions = selected.getSupportedActions(); // Questo metodo deve essere definito in ShapeBase e nelle sottoclassi
        if (actions.contains("delete")) {
            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(e -> menuDeleteHandler());
            contextMenu.getItems().add(delete);
        }
        if (actions.contains("cut")) {
            MenuItem cut = new MenuItem("Cut");
            cut.setOnAction(e -> menuCutHandler());
            contextMenu.getItems().add(cut);
        } 
        if (actions.contains("copy")) {
            MenuItem copy = new MenuItem("Copy");
            copy.setOnAction(e -> menuCopyHandler());
            contextMenu.getItems().add(copy);
        }
        if (actions.contains("paste")) {
            MenuItem paste = new MenuItem("Paste");
            paste.setOnAction(e -> menuPasteHandler());
            contextMenu.getItems().add(paste);
        }
        if (actions.contains("strokeColor")) {
            MenuItem strokeColor = new MenuItem("Set border color");
            strokeColor.setOnAction(e -> menuModifyColorStroke());
            contextMenu.getItems().add(strokeColor);
        }
        if (actions.contains("strokeWidth")) {
            MenuItem strokeWidth = new MenuItem("Set border thickness");
            strokeWidth.setOnAction(e -> menuModifyWidthStroke());
            contextMenu.getItems().add(strokeWidth);
        }
        if (actions.contains("fillColor")) {
            MenuItem fillColor = new MenuItem("Set fill color");
            fillColor.setOnAction(e -> menuModifyColorFill());
            contextMenu.getItems().add(fillColor);
        }
        if (actions.contains("toFront")) {
            MenuItem front = new MenuItem("ToFront");
            front.setOnAction(e -> toFront(shapes.indexOf(selected), shapes.size()));
            contextMenu.getItems().add(front);
        }
        if (actions.contains("toBack")) {
            MenuItem back = new MenuItem("ToBack");
            back.setOnAction(e -> toBack(shapes.indexOf(selected)));
            contextMenu.getItems().add(back);
        }
        if (actions.contains("modifyText")) {
            MenuItem modifyText = new MenuItem("Modify text");
            modifyText.setOnAction(e -> menuModifyText());
            contextMenu.getItems().add(modifyText);
        }
        if (actions.contains("HMirror")) { 
            MenuItem mirrorHorizontally = new MenuItem("Mirror Horizontally");
            mirrorHorizontally.setOnAction(e -> menuHMirror(selected));
            contextMenu.getItems().add(mirrorHorizontally);
        }
        if (actions.contains("VMirror")) { 
            MenuItem mirrorVertically = new MenuItem("Mirror Vertically");
            mirrorVertically.setOnAction(e -> menuVMirror(selected));
            contextMenu.getItems().add(mirrorVertically);
        }
        baseCanvas.getCanvas().setOnContextMenuRequested(e -> {
            if (isDrawingPolygon) {
                e.consume(); // Impedisce il menu mentre disegni un poligono
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
    
    public void menuCutHandler (){   
        command = new CutCommand(selectShape);
        command.execute();  
        redraw(baseCanvas.getGc());
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
    
    public void menuHMirror(ShapeBase selected) {
        double centerX = baseCanvas.getCanvas().getWidth() / 2;
        double centerY = baseCanvas.getCanvas().getHeight() / 2;
        command = new MirrorCommand(selected, true, false, centerX, centerY);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
    }
    
    public void menuVMirror(ShapeBase selected) {
        double centerX = baseCanvas.getCanvas().getWidth() / 2;
        double centerY = baseCanvas.getCanvas().getHeight() / 2;
        command = new MirrorCommand(selected, false, true, centerX, centerY);  // vertical only
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
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
        canvasPlaceholder.setPrefSize(scaledWidth, scaledHeight);
        canvasPlaceholder.setMinSize(scaledWidth, scaledHeight);
        canvasPlaceholder.setMaxSize(scaledWidth, scaledHeight);
        // forza aggiornamento layout
        canvasPlaceholder.requestLayout();
        scrollPane.layout();
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
        if (selectShape.getMemory().canUndo()) {
            ShapeBase oldSelected = selectShape.getSelectedShape();
            shapes.clear();
            List<ShapeBase> restored = selectShape.getMemory().restoreLastState();
            shapes.addAll(restored);
            // Deselect by default
            selectShape.setSelectedShape(null);
            // Facoltativo: ripristina selezione se esiste equivalente
            if (oldSelected != null) {
                for (ShapeBase shape : restored) {
                    if (shape.equals(oldSelected)) {  // usa equals definito correttamente
                        selectShape.setSelectedShape(shape);
                        break;
                    }
                }
            }
            redraw(baseCanvas.getGc());
        } else {
            showInfo("Undo", "Nessuno stato precedente disponibile.");
        }
    }
    
    private void setActiveToolButton(Button activeButton) {
         btnPan.setStyle("");
         btnSelect.setStyle("");
         btnLine.setStyle("");
         btnRectangle.setStyle("");
         btnEllipse.setStyle("");
         btnPolygon.setStyle("");
         btnTextBox.setStyle("");
         if (activeButton != null) {
             activeButton.setStyle("-fx-background-color: lightblue;");
         }
    }
    
    private void translateShape(ShapeBase selected, double mouseX, double mouseY){
        disablePanMode();
        if (selected != null) {
            ShapeVisitor translateVisitor = new TranslateVisitor(mouseX, mouseY);
            selected.accept(translateVisitor);
        }
    }
    
    private void resizeShape(ShapeBase selected, double mouseX, double mouseY) {
        disablePanMode();
        if (selected != null) {
            ShapeVisitor editVisitor = new ResizeVisitor(mouseX, mouseY);
            selected.accept(editVisitor);
        }
    }

    //gestisce l'anteprima con lo Strategy
    public void setPreviewShape(ShapeBase shape) {
        this.previewShape = shape;
    }

    public void menuModifyText() {
        ShapeBase shape = selectShape.getSelectedShape();
        shape.accept(new TextEditVisitor(baseCanvas.getCanvas()));
        redraw(baseCanvas.getGc());
    }
    
    private void setupShapeIcons() {
        setButtonIcon(btnRectangle, "/icons/rectangle.png", "Rectangle",48);
        setButtonIcon(btnEllipse, "/icons/ellipse.png", "Ellipse", 48);
        setButtonIcon(btnLine, "/icons/line.png", "Line", 48);
        setButtonIcon(btnPolygon, "/icons/polygon.png", "Polygon", 48);
        setButtonIcon(btnTextBox, "/icons/text.png", "Text", 48);
        setButtonIcon(btnSelect, "/icons/select.png", "Select", 20);
        setButtonIcon(btnUndo, "/icons/undo.png", "Undo", 20);
        setButtonIcon(btnPan, "/icons/pan.png", "Pan", 20);
        setButtonIcon(btnToggleGrid, "/icons/grid.png", "Grid", 20);
        setButtonIcon(btnZoomIn, "/icons/zoom_in.png", "Zoom In", 20);
        setButtonIcon(btnZoomOut, "/icons/zoom_out.png", "Zoom Out", 20);
    }

    private void setButtonIcon(Button button, String iconPath, String label, double size) {
        try {
            Image image = new Image(getClass().getResourceAsStream(iconPath));
            ImageView icon = new ImageView(image);
            icon.setFitWidth(size);
            icon.setFitHeight(size);
            button.setGraphic(icon);
            button.setText(label);
            button.setContentDisplay(ContentDisplay.TOP);  // icona sopra, testo sotto
        } catch (Exception e) {
            System.err.println("Errore caricando " + iconPath);
        }
    }
    
    //Funzione da chiamare quando si vuole disabilitare la modalità Pan
    private void disablePanMode() {
        if (scrollPane.isPannable()) {
            scrollPane.setPannable(false);
            isPanMode = false;  // se usi questa variabile
            setActiveToolButton(null); // resetto evidenziazione bottoni
        }
    }
   
}