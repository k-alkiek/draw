package controllers;

import com.jfoenix.controls.*;
import controllers.commands.commandsClasses.LoadExtension;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.interfaces.Shape;
import models.shapes.Line;
import models.shapes.Polygon;
import models.shapes.ShapesFactory;
import models.shapes.Triangle;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class SampleController implements Initializable{
    private String filePath;
    private Painter painter;
    private ShapesFactory factory = new ShapesFactory();
    private BiMap<String, Shape> shapesMap;
    List<Point2D> clickHistory = new ArrayList<Point2D>();
    private ArrayList<Class<? extends Shape>> extentions;

    @FXML private Canvas canvas;

    @FXML private Menu shapesMenu;

    @FXML private JFXComboBox<Label> toolsComboBox;
    @FXML private JFXColorPicker fillColorPicker;
    @FXML private JFXColorPicker strokeColorPicker;
    @FXML private JFXSlider strokeSlider;

    @FXML private Canvas strokePreviewCanvas;
    @FXML private JFXBadge undoBadge;
    @FXML private JFXBadge redoBadge;
    @FXML private JFXBadge saveBadge;
    @FXML private JFXBadge deselectBadge;
    @FXML private JFXBadge cloneBadge;
    @FXML private JFXBadge deleteBadge;

    @FXML private VBox selectedShapeLayout;
    @FXML private Label selectedShapeLabel;
    @FXML private JFXColorPicker selectedShapeFillColorPicker;
    @FXML private JFXColorPicker selectedShapeStrokeColorPicker;

    @FXML private JFXListView<Label> shapesListView;

    @FXML private Label xLabel;
    @FXML private Label yLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        painter = Painter.getInstanceOfPainter();
        shapesMap = HashBiMap.create();
        filePath = null;
        extentions = new ArrayList<>();
        initializeStrokePreview();
        initializeTools();
        initializeBadges();
        initializeShapesMenuItem();

        shapesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        clear();
    }

    @FXML
    void showCoordinates(MouseEvent event) {
        xLabel.setText("x: " + (int) event.getX());
        yLabel.setText("y: " + (int) event.getY());
    }

    @FXML
    void hideCoordinates() {
        xLabel.setText("");
        yLabel.setText("");
    }

    @FXML
    void onShapeSelect() {
        refresh();
        toolsComboBox.getSelectionModel().select(0);
        highlightShapes();
    }

    private void highlightShapes() {
        if (selectedShapes().size() == 1) {
            selectedShapeLayout.setDisable(false);
            Shape shape = selectedShapes().get(0);
            selectedShapeLabel.setText(shapesMap.inverse().get(shape));
            selectedShapeFillColorPicker.setValue(shape.getFillColor());
            selectedShapeStrokeColorPicker.setValue(shape.getColor());
            drawBoundingBox(selectedShapes());
        }
        else if (selectedShapes().size() > 1) {
            selectedShapeLayout.setDisable(false);
            selectedShapeLabel.setText("Shapes");
            selectedShapeFillColorPicker.setValue(Color.BLACK);
            selectedShapeStrokeColorPicker.setValue(Color.WHITE);
            drawBoundingBox(selectedShapes());
        }
        else {
            deselectShapes();
        }
    }
    @FXML
    void test() {
        System.out.println("asd");
    }

    @FXML
    void deselectShapes() {
        selectedShapeLayout.setDisable(true);
        shapesListView.getSelectionModel().select(-1);
        selectedShapeLabel.setText("Selected Shape");
        canvas.setOnMouseDragged(null);
        removeBoundingBox();
    }

    private void drawBoundingBox(List<Shape> shapes) {
        double minX = canvas.getWidth();
        double minY = canvas.getHeight();
        double maxX = 0;
        double maxY = 0;

        for (Shape shape : selectedShapes()) {
            double x1 = shape.getProperties().get("upperPointX");
            double y1 = shape.getProperties().get("upperPointY");
            if (x1 < minX) minX = x1;
            if (y1 < minY) minY = y1;

            double x2 = shape.getProperties().get("bottomPointX");
            double y2 = shape.getProperties().get("bottomPointY");
            if (x2 > maxX) maxX = x2;
            if (y2 > maxY) maxY = y2;
        }
        drawBoundingBoxLines(minX, minY, maxX, maxY);
        addEditHandlersToCanvas(minX, minY, maxX, maxY);
        resetGraphicsContext();
    }

    private void removeBoundingBox() {
        refresh();
    }

    private void drawBoundingBoxLines(double minX, double minY, double maxX, double maxY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.setLineDashes(7);
        gc.setLineWidth(1);
        gc.strokeRect(minX - 5, minY - 5, maxX - minX + 10,maxY - minY + 10);

        gc.setFill(Color.WHITE);
        gc.setLineDashes(0);

        gc.fillRect(minX - 10, minY - 10, 10, 10);
        gc.strokeRect(minX - 10, minY - 10, 10, 10);

        gc.fillRect(minX - 10, maxY, 10, 10);
        gc.strokeRect(minX - 10, maxY, 10, 10);

        gc.fillRect(maxX, minY - 10, 10, 10);
        gc.strokeRect(maxX, minY - 10, 10, 10);

        gc.fillRect(maxX, maxY, 10, 10);
        gc.strokeRect(maxX, maxY, 10, 10);

        gc.fillRect((maxX + minX)/2, minY - 10, 10, 10);
        gc.strokeRect((maxX + minX)/2, minY - 10, 10, 10);

        gc.fillRect((maxX + minX)/2, maxY, 10, 10);
        gc.strokeRect((maxX + minX)/2, maxY, 10, 10);

        gc.fillRect(minX - 10, (maxY + minY)/2, 10, 10);
        gc.strokeRect(minX - 10, (maxY + minY)/2, 10, 10);

        gc.fillRect(maxX, (maxY + minY)/2, 10, 10);
        gc.strokeRect(maxX, (maxY + minY)/2, 10, 10);
    }

    private void addEditHandlersToCanvas(double minX, double minY, double maxX, double maxY) {

        canvas.setOnMousePressed(click -> {
            List<Shape> newShapes = new ArrayList<>();
            List<Shape> oldShapes = selectedShapes();
            double originX = click.getX();
            double originY = click.getY();

            try {
                for (Shape shape : oldShapes) {
                    newShapes.add((Shape) shape.clone());
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (click.getX() > minX && click.getX() < maxX && click.getY() > minY && click.getY() < maxY) { // move if dragged inside
                canvas.setOnMouseDragged(event -> {
                    double deltaX = event.getX() - originX;
                    double deltaY = event.getY() - originY;
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                shape.getProperties().replace(key, oldShape.getProperties().get(key) + deltaX);
                            }
                            else if (key.charAt(0) == 'y') {
                                shape.getProperties().replace(key, oldShape.getProperties().get(key) + deltaY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX + deltaX, minY + deltaY, maxX + deltaX, maxY + deltaY);
                });
            }
            else if (click.getX() > minX - 10 && click.getX() < minX && click.getY() > minY - 10 && click.getY() < minY) { //scale from top-left
                canvas.setOnMouseDragged(event -> {
                    double scaleX = (event.getX() - maxX ) / (minX - maxX);
                    double scaleY = (event.getY() - maxY) / (minY - maxY);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for (String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                double oldX = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldX * scaleX - (maxX) * scaleX + maxX);
                            } else if (key.charAt(0) == 'y') {
                                double oldY = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldY * scaleY - maxY* scaleY + maxY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(event.getX(), event.getY(), maxX, maxY);
                });
            }
            else if (click.getX() > maxX && click.getX() < maxX + 10 && click.getY() > minY - 10 && click.getY() < minY) { //scale from top-right
                System.out.println("Top Right Corner");
                canvas.setOnMouseDragged(event -> {
                    double scaleX = (event.getX() - minX ) / (maxX - minX);
                    double scaleY = (event.getY() - maxY) / (minY - maxY);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                double oldX = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldX * scaleX - (minX) * scaleX + minX);
                            } else if (key.charAt(0) == 'y') {
                                double oldY = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldY * scaleY - maxY* scaleY + maxY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX, event.getY(), event.getX(), maxY);
                });
            }
            else if (click.getX() > minX - 10 && click.getX() < minX && click.getY() > maxY && click.getY() < maxY + 10) { //scale from bottom-left
                System.out.println("Top Right Corner");
                canvas.setOnMouseDragged(event -> {
                    double scaleX = (event.getX() - maxX ) / (minX - maxX);
                    double scaleY = (event.getY() - minY) / (maxY - minY);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                double oldX = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldX * scaleX - (maxX) * scaleX + maxX);
                            } else if (key.charAt(0) == 'y') {
                                double oldY = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldY * scaleY - minY* scaleY + minY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(event.getX(), minY, maxX, event.getY());
                });
            }
            else if (click.getX() > maxX && click.getX() < maxX + 10 && click.getY() > maxY && click.getY() < maxY + 10) { //scale from bottom right
                canvas.setOnMouseDragged(event -> {
                    double scaleX = (event.getX() - minX) / (maxX - minX);
                    double scaleY = (event.getY() - minY) / (maxY - minY);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                double oldX = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldX * scaleX - minX * scaleX + minX);
                            }
                            else if (key.charAt(0) == 'y') {
                                double oldY = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldY * scaleY - minY * scaleY + minY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX, minY, event.getX(), event.getY());
                });
            }
            else if (click.getX() > (maxX + minX)/2 && click.getX() < (maxX + minX)/2 + 10 && click.getY() > minY - 10 && click.getY() < minY ) { //scale from top
                canvas.setOnMouseDragged(event -> {
                    double scaleY = (event.getY() - maxY) / (minY - maxY);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'y') {
                                double oldY = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldY * scaleY - maxY * scaleY + maxY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX, event.getY(), maxX, maxY);
                });
            }
            else if (click.getX() > (maxX + minX)/2 && click.getX() < (maxX + minX)/2 + 10 && click.getY() > maxY && click.getY() < maxY + 10 ) { //scale from top
                canvas.setOnMouseDragged(event -> {
                    double scaleY = (event.getY() - minY) / (maxY - minY);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'y') {
                                double oldY = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldY * scaleY - minY * scaleY + minY);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX, minY, maxX, event.getY());
                });
            }
            else if (click.getX() > maxX && click.getX() < maxX + 10 && click.getY() > (maxY + minY)/2 && click.getY() < (maxY + minY)/2 + 10 ) { //scale from right
                canvas.setOnMouseDragged(event -> {
                    double scaleX = (event.getX() - minX) / (maxX - minX);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                double oldX = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldX * scaleX - minX * scaleX + minX);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX, minY, event.getX(), maxY);
                });
            }
            else if (click.getX() > minX - 10 && click.getX() < minX && click.getY() > (maxY + minY)/2 && click.getY() < (maxY + minY)/2 + 10 ) { //scale from right
                canvas.setOnMouseDragged(event -> {
                    double scaleX = (event.getX() - maxX) / (minX - maxX);
                    for (int i = 0; i < newShapes.size(); i++) {
                        Shape shape = newShapes.get(i);
                        Shape oldShape = oldShapes.get(i);
                        Map<String, Double> properties = new HashMap<>(shape.getProperties());
                        for(String key : properties.keySet()) {
                            if (key.charAt(0) == 'x') {
                                double oldX = oldShape.getProperties().get(key);
                                shape.getProperties().replace(key, oldX * scaleX - maxX * scaleX + maxX);
                            }
                        }
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(event.getX(), minY, maxX, maxY);
                });
            }
            canvas.setOnMouseReleased(event -> {
                for (int i = 0; i < oldShapes.size(); i++) {
                    Shape oldShape = oldShapes.get(i);
                    Shape newShape = newShapes.get(i);
                    painter.updateShape(oldShapes.get(i), newShapes.get(i));

                    String shapeName = generateUniqueName(newShape);
                    shapesMap.put(shapeName, newShape);
                }
                canvas.setOnMouseDragged(null);
                refreshShapeList();
                refresh();

                for (Shape clonedShape : newShapes) {
                    selectShapeByName(shapesMap.inverse().get(clonedShape));
                }
                drawBoundingBox(newShapes);
            });
        });
    }

    @FXML
    void updateSelectedShapes() {
        List<Shape> newShapes = new ArrayList<>();
        for (Shape shape: selectedShapes()) {
            Shape newShape;
            try {
                newShape = shape.getClass().newInstance();
                newShapes.add(newShape);
            } catch (Exception e) {
                return;
            }
            newShape.setProperties(shape.getProperties());
            newShape.setFillColor(selectedShapeFillColorPicker.getValue());
            newShape.setColor(selectedShapeStrokeColorPicker.getValue());
            painter.updateShape(shape, newShape);

            String shapeName = generateUniqueName(newShape);
            shapesMap.put(shapeName, newShape);

            refreshShapeList();
            refresh();

            for (Shape clonedShape : newShapes) {
                selectShapeByName(shapesMap.inverse().get(clonedShape));
            }
            drawBoundingBox(newShapes);
        }
    }

    @FXML
    void deleteSelectedShapes() {
        for (Shape shape: selectedShapes()) {
            painter.removeShape(shape);
        }
        deselectShapes();
        refresh();
        refreshShapeList();
    }

    @FXML
    void cloneSelectedShapes() {
        List<Shape> clonedShapes = new ArrayList<>();
        for (Shape shape: selectedShapes()) {
            Shape newShape;
            try {
                newShape = (Shape) shape.clone();
                clonedShapes.add(newShape);
                addShape(newShape);
            } catch (CloneNotSupportedException e) {
                continue;
            }

        }
        refresh();
        deselectShapes();
        refreshShapeList();
        for (Shape clonedShape : clonedShapes) {
            selectShapeByName(shapesMap.inverse().get(clonedShape));
        }
        drawBoundingBox(clonedShapes);
    }

    @FXML
    void undo() {
        painter.undo();
        refresh();
        refreshShapeList();
    }

    @FXML
    void redo() {
        painter.redo();
        refresh();
        refreshShapeList();
    }

    @FXML
    void save() {
        if (filePath == null) {
            saveAs();
        }
        else {
            painter.save(filePath);
        }
    }

    @FXML
    void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing XML");

        try {
            StringBuilder path = new StringBuilder(fileChooser.showSaveDialog(new Stage()).getPath());
            if (!path.substring(path.length() - 4).equals(".xml")) {
                path.append(".xml");
            }
            filePath = path.toString();
            painter.save(filePath);
        } catch (Exception e) {

        }
    }

    @FXML
    void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Drawing XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));
        try {
            filePath = fileChooser.showOpenDialog(new Stage()).getPath();
            painter.load(filePath);
        } catch (Exception e) {

        }
        shapesMap.clear();
        for (Shape shape : painter.getShapes()) {
            String shapeName = generateUniqueName(shape);
            shapesMap.put(shapeName, shape);
        }
        refreshShapeList();
        refresh();
    }

    @FXML
    void importPlugin() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Plugin JAR");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR File", "*.jar"));
        try {
            filePath = fileChooser.showOpenDialog(new Stage()).getPath();
            painter.load(filePath);
        } catch (Exception e) {

        }
        LoadExtension loadExtension = new LoadExtension(filePath);
        try {
            System.out.println(loadExtension.addExtension().size());
            extentions.addAll(loadExtension.addExtension());
            for (Class<? extends Shape> shapeClass : extentions) {
                String shapeName = shapeClass.getSimpleName();
                MenuItem shapeMenuItem = setupShapeMenuItem(shapeClass);


                shapesMenu.getItems().add(shapeMenuItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        refresh();
    }

    @FXML
    void exit() {
        Platform.exit();
    }



    @FXML
    void listenForUserDrawing() {
        canvas.setOnMousePressed(click -> {


            Shape shape = null;
            String toolSelected = toolsComboBox.getValue().getText();

            try {
                shape = factory.createShape(toolSelected);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            if (shape instanceof Triangle) {
                if (clickHistory.size() < 2) {
                    clickHistory.add(new Point2D(click.getX(), click.getY()));
                } else {
                    clickHistory.add(new Point2D(click.getX(), click.getY()));
                    Map<String, Double> properties = new HashMap<>();
                    properties.put("x1", clickHistory.get(0).getX());
                    properties.put("y1", clickHistory.get(0).getY());
                    properties.put("x2", clickHistory.get(1).getX());
                    properties.put("y2", clickHistory.get(1).getY());
                    properties.put("x3", clickHistory.get(2).getX());
                    properties.put("y3", clickHistory.get(2).getY());
                    properties.put("borderWidth", strokeSlider.getValue());

                    shape.setProperties(properties);
                    setShapeColors(shape);
                    addShape(shape);
                    refreshShapeList();
                    refresh();
                    clickHistory.clear();
                }
            }
            else if (shape instanceof Polygon) {
//                Polygon polygon = new Polygon()
                int n = 5;
                if (clickHistory.size() < n - 1) {
                    clickHistory.add(new Point2D(click.getX(), click.getY()));
                } else {
                    clickHistory.add(new Point2D(click.getX(), click.getY()));
                    double[] x = new double[clickHistory.size()];
                    double[] y = new double[clickHistory.size()];
                    for (int i = 0; i < clickHistory.size(); i++) {
                        x[i] = clickHistory.get(i).getX();
                        y[i] = clickHistory.get(i).getY();
                    }
                    Polygon polygon = new Polygon(n, x, y);
                    polygon.getProperties().put("borderWidth", strokeSlider.getValue());
                    setShapeColors(polygon);
                    addShape(polygon);
                    refreshShapeList();
                    refresh();
                    clickHistory.clear();
                }
            }
            else {
                clickHistory.clear();
                Point2D originPoint = new Point2D(click.getX(), click.getY());
                canvasDrag(shape, originPoint);
            }
        });
    }

    private void canvasDrag(Shape shape, Point2D originPoint) {
        Map<String, Double> properties = new HashMap<String, Double>();


        properties.put("borderWidth", strokeSlider.getValue());

        setShapeColors(shape);
//        EventHandler<? super MouseEvent> drag =  canvas.getOnMouseDragged();
        canvas.setOnMouseDragged(mouseEvent->{
            xLabel.setText("" + mouseEvent.getX());
            yLabel.setText("" + mouseEvent.getY());
            if ( shape instanceof Line || (mouseEvent.getX() >= originPoint.getX() && mouseEvent.getY() >= originPoint.getY())) {
                properties.put("x1", originPoint.getX());
                properties.put("y1", originPoint.getY());
                properties.put("x2", mouseEvent.getX());
                properties.put("y2", mouseEvent.getY());
            }
            else if (mouseEvent.getX() >= originPoint.getX() && mouseEvent.getY() <= originPoint.getY()) {
                properties.put("x1", originPoint.getX());
                properties.put("y1", mouseEvent.getY());
                properties.put("x2", mouseEvent.getX());
                properties.put("y2", originPoint.getY());
            }
            else if (mouseEvent.getX() <= originPoint.getX() && mouseEvent.getY() <= originPoint.getY()) {
                properties.put("x1", mouseEvent.getX());
                properties.put("y1", mouseEvent.getY());
                properties.put("x2", originPoint.getX());
                properties.put("y2", originPoint.getY());
            }
            else if (mouseEvent.getX() <= originPoint.getX() && mouseEvent.getY() >= originPoint.getY()) {
                properties.put("x1", mouseEvent.getX());
                properties.put("y1", originPoint.getY());
                properties.put("x2", originPoint.getX());
                properties.put("y2", mouseEvent.getY());
            }

            shape.setProperties(properties);
            painter.addShapePreview(shape);
            refresh();
            painter.removeShapePreview(shape);
        });
        addShape(shape);
        canvas.setOnMouseReleased(event -> {
            canvas.setOnMouseDragged(null);
        });
    }

    private void initializeBadges() {
        Map<JFXBadge, String> map = new HashMap<JFXBadge, String>();
        map.put(undoBadge, "UNDO");
        map.put(redoBadge, "REPEAT");
        map.put(saveBadge, "SAVE");
        map.put(deselectBadge, "TIMES");
        map.put(cloneBadge, "CLONE");
        map.put(deleteBadge, "TRASH");

        for (JFXBadge badge : map.keySet()) {
            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setGlyphName(map.get(badge));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(icon);
            badge.getChildren().add(stackPane);
        }
    }

    private void initializeShapesMenuItem() {
        List<Class<? extends Shape>> shapeClasses = painter.getSupportedShapes();

        for (Class<? extends Shape> shapeClass : shapeClasses) {
            String shapeName = shapeClass.getSimpleName();
            MenuItem shapeMenuItem = setupShapeMenuItem(shapeClass);


            shapesMenu.getItems().add(shapeMenuItem);
        }
    }

    private MenuItem setupShapeMenuItem(Class<? extends Shape> shapeClass) {
        MenuItem shapeMenuItem = new MenuItem(shapeClass.getSimpleName());
        shapeMenuItem.setOnAction(event -> {
            try {
                boolean isSupported = !(extentions.contains(shapeClass)) &&
                        ! shapeClass.getSimpleName().equals("Line") &&
                        ! shapeClass.getSimpleName().equals("Triangle") &&
                        ! shapeClass.getSimpleName().equals("Polygon");

                Map<String, Double> map = newShapeController.setProps(shapeClass.newInstance(), isSupported);

                canvas.setOnMousePressed(click -> {
                    Shape shape = null;
                    try {
                        shape = shapeClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (isSupported) {
                        Map<String, Double> temp = new HashMap<>();
                        temp.put("x1", click.getX());
                        temp.put("y1", click.getY());
                        temp.put("x2", click.getX() + map.get("height"));
                        temp.put("y2", click.getY() + map.get("width"));
                        temp.put("borderWidth", strokeSlider.getValue());
                        shape.setProperties(temp);
                    } else {
                        shape.setProperties(map);
                    }
                    shape.setPosition(new Point2D(click.getX(), click.getY()));
                    shape.setColor(strokeColorPicker.getValue());
                    shape.setFillColor(fillColorPicker.getValue());
                    addShape(shape);
                    painter.refresh(canvas.getGraphicsContext2D());
                });
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        return shapeMenuItem;
    }

    private void initializeTools() {
        List<Class<? extends Shape>> shapeClasses = painter.getSupportedShapes();
        toolsComboBox.getItems().add(new Label("Edit"));
        toolsComboBox.getItems().add(new Label("Line"));
        toolsComboBox.getItems().add(new Label("Rectangle"));
        toolsComboBox.getItems().add(new Label("Ellipse"));
        toolsComboBox.getItems().add(new Label("RoundRectangle"));
        toolsComboBox.getItems().add(new Label("Triangle"));
        toolsComboBox.getItems().add(new Label("Polygon"));
        toolsComboBox.getSelectionModel().select(0);
    }

    @FXML
    void onToolSelected() {
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);

        String tool = toolsComboBox.getSelectionModel().getSelectedItem().getText();

        if (tool == "Edit" && selectedShapes().size() > 0) {
            highlightShapes();
        }
        else if (tool != "Edit") {
            listenForUserDrawing();
        }
        clickHistory.clear();
    }

    private void initializeStrokePreview() {
        drawStrokePreview(strokeColorPicker.getValue(), strokeSlider.getValue());

        strokeColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            drawStrokePreview(newValue, strokeSlider.getValue());
        });
        strokeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            drawStrokePreview(strokeColorPicker.getValue(), newValue.doubleValue());
        });
    }

    private void drawStrokePreview(Color color, double value) {
        GraphicsContext gc = strokePreviewCanvas.getGraphicsContext2D();

        double centerX = strokePreviewCanvas.getWidth() / 2;
        double centerY = strokePreviewCanvas.getHeight() / 2;
        double radius = strokeSlider.getValue() / 2;

        gc.clearRect(0, 0, strokePreviewCanvas.getWidth(), strokePreviewCanvas.getHeight());
        gc.setFill(color);
        gc.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    private void clear() {
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().setFill(fillColorPicker.getValue());
    }

    private void setShapeColors(Shape shape) {
        if (shape instanceof Line) {
            shape.setFillColor(strokeColorPicker.getValue());
            shape.setColor(Color.WHITE);
        }
        else {
            shape.setColor(strokeColorPicker.getValue());
            shape.setFillColor(fillColorPicker.getValue());
        }
    }

    private void addShape(Shape shape) {
        painter.addShape(shape);
        String shapeName = generateUniqueName(shape);
        shapesMap.put(shapeName, shape);
        refreshShapeList();
    }

    @FXML
    void refresh() {
        clear();
        painter.refresh(canvas.getGraphicsContext2D());
    }

    private String selectedTool() {
        return toolsComboBox.getSelectionModel().getSelectedItem().getText();
    }

    private void resetGraphicsContext() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineDashes(0);
        gc.setFill(fillColorPicker.getValue());
        gc.setStroke(strokeColorPicker.getValue());
    }

    private void refreshShapeList() {
        shapesListView.getItems().clear();
        for (Shape shape : painter.getShapes()) {
            shapesListView.getItems().add(0, new Label(shapesMap.inverse().get(shape)));
        }
    }

    private void selectShapeByName(String name) {
        for (Label item : shapesListView.getItems()) {
            if (item.getText() == name) shapesListView.getSelectionModel().select(item);
        }
    }

    private List<Shape> selectedShapes() {
        List<Shape> list = new ArrayList<Shape>();
        for (Label label : shapesListView.getSelectionModel().getSelectedItems()) {
            list.add(shapesMap.get(label.getText()));
        }
        return list;
    }

    private String generateUniqueName(Shape shape) {
        String name = shape.getClass().getSimpleName();
        String currentName = new String(name);

        int count = 1;
        while ( shapesMap.keySet().contains(currentName)) {
            currentName = name + " " + count;
            count ++;
        }
        return currentName;
    }
}
