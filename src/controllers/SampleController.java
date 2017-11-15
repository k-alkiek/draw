package controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.interfaces.Shape;
import models.shapes.ShapesFactory;
import models.shapes.Triangle;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.net.URL;
import java.util.*;
import java.util.List;

public class SampleController implements Initializable{

    @FXML Canvas canvas;

    @FXML JFXComboBox<Label> toolsComboBox;
    @FXML JFXColorPicker fillColorPicker;
    @FXML JFXColorPicker strokeColorPicker;
    @FXML JFXSlider strokeSlider;
    @FXML Canvas strokePreviewCanvas;

    @FXML Group selectedShapeGroup;
    @FXML JFXColorPicker selectedShapeFillColorPicker;
    @FXML JFXColorPicker selectedShapeStrokeColorPicker;
    @FXML JFXComboBox<CheckBox> shapesComboBox;

    @FXML JFXBadge undoBadge;
    @FXML JFXBadge redoBadge;
    @FXML JFXBadge saveBadge;

    @FXML JFXListView<Label> shapesListView;
    @FXML VBox selectedShapeLayout;
    @FXML Label selectedShapeLabel;

    Painter painter;
    ShapesFactory factory = new ShapesFactory();
    BiMap<String, Shape> shapesMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        painter = new Painter();
        shapesMap = HashBiMap.create();
        initializeStrokePreview();
        initializeTools();
        initializeBadges();

        shapesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        clear();
    }

    @FXML
    void onShapeSelect() {
        if (selectedShapes().size() == 1) {
            selectedShapeLayout.setDisable(false);
            Shape shape = selectedShapes().get(0);
            selectedShapeLabel.setText(shapesMap.inverse().get(shape));

            selectedShapeFillColorPicker.setValue(shape.getFillColor());
            selectedShapeStrokeColorPicker.setValue(shape.getColor());
        }
        else if (selectedShapes().size() > 1) {
            selectedShapeLayout.setDisable(false);
            selectedShapeLabel.setText("Shapes");
            selectedShapeFillColorPicker.setValue(Color.BLACK);
            selectedShapeStrokeColorPicker.setValue(Color.WHITE);
        }
        else {
            deselect();
        }
    }

    @FXML
    void deselect() {
        selectedShapeLayout.setDisable(true);
        shapesListView.getSelectionModel().select(-1);
        selectedShapeLabel.setText("Selected Shape");
    }

    @FXML
    void updateSelected() {
        for (Shape shape: selectedShapes()) {
            shape.setFillColor(selectedShapeFillColorPicker.getValue());
            shape.setColor(selectedShapeStrokeColorPicker.getValue());
            refresh();
        }
    }

    @FXML
    void redo() {
        painter.redo();
        refresh();
        refreshShapeList();
    }

    @FXML
    void save() {
        painter.save("asd.xml");
    }

    @FXML
    void load() {
        painter.load("asd.xml");
        refresh();
        refreshShapeList();
    }

    @FXML
    void undo() {
        painter.undo();
        refresh();
        refreshShapeList();
    }

    @FXML
    void comboBoxSelected() {
        clickHistory.clear();
    }

    List<Point2D> clickHistory = new ArrayList<Point2D>();

    @FXML
    void onCanvasClick(MouseEvent click) {
        Shape shape = factory.createShape(toolsComboBox.getValue().getText());

        if (shape instanceof Triangle) {
            if (clickHistory.size() < 2) {
                clickHistory.add(new Point2D(click.getX(), click.getY()));
            } else {
                clickHistory.add(new Point2D(click.getX(), click.getY()));
                Map<String, Double> properties = new HashMap<String, Double>();
                properties.put("x1", clickHistory.get(0).getX());
                properties.put("y1", clickHistory.get(0).getY());
                properties.put("x2", clickHistory.get(1).getX());
                properties.put("y2", clickHistory.get(1).getY());
                properties.put("x3", clickHistory.get(2).getX());
                properties.put("y3", clickHistory.get(2).getY());
                properties.put("borderWidth", strokeSlider.getValue());

                shape.setProperties(properties);
                shape.setFillColor(fillColorPicker.getValue());
                shape.setColor(strokeColorPicker.getValue());
                addShape(shape);
                painter.refresh(canvas.getGraphicsContext2D());
                clickHistory.clear();
            }
        }
        else {
            clickHistory.clear();
            canvasDrag(shape, click);
        }
    }
    private void canvasDrag(Shape shape, MouseEvent click) {
        Map<String, Double> properties = new HashMap<String, Double>();
        Point2D originPoint = new Point2D(click.getX(), click.getY());

        properties.put("x1", click.getX());
        properties.put("y1", click.getY());
        properties.put("borderWidth", strokeSlider.getValue());

        setShapeColors(shape);

        canvas.setOnMouseDragged(mouseEvent->{
            properties.put("x2", mouseEvent.getX());
            properties.put("y2", mouseEvent.getY());
            shape.setProperties(properties);
            painter.addShapePreview(shape);
            refresh();
            painter.removeShapePreview(shape);
            System.out.println(shape);
            System.out.println("X coord: " + mouseEvent.getX());
            System.out.println("Y coord: " + mouseEvent.getY());
        });
        addShape(shape);
    }

    private void initializeBadges() {
        FontAwesomeIconView undoIcon = new FontAwesomeIconView();
        undoIcon.setGlyphName("UNDO");
        StackPane s1 = new StackPane();
        s1.getChildren().add(undoIcon);
        undoBadge.getChildren().add(s1);

        FontAwesomeIconView redoIcon = new FontAwesomeIconView();
        redoIcon.setGlyphName("REPEAT");
        StackPane s2 = new StackPane();
        s2.getChildren().add(redoIcon);
        redoBadge.getChildren().add(s2);

        FontAwesomeIconView saveIcon = new FontAwesomeIconView();
        saveIcon.setGlyphName("SAVE");
        StackPane s3 = new StackPane();
        s3.getChildren().add(saveIcon);
        saveBadge.getChildren().add(s3);
    }

    private void initializeTools() {
        List<Class<? extends Shape>> shapeClasses = painter.getSupportedShapes();

        for (Class<? extends Shape> shapeClass : shapeClasses) {
            toolsComboBox.getItems().add(new Label(shapeClass.getSimpleName()));
        }
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
        shape.setFillColor(fillColorPicker.getValue());
        shape.setColor(strokeColorPicker.getValue());
    }

    private void addShape(Shape shape) {
        painter.addShape(shape);
        String shapeName = generateUniqueName(shape);
        shapesMap.put(shapeName, shape);
        refreshShapeList();
    }

    private void refresh() {
        clear();
        painter.refresh(canvas.getGraphicsContext2D());
    }

    private void refreshShapeList() {
        shapesListView.getItems().clear();
        for (Shape current_shape : painter.getShapes()) {
            shapesListView.getItems().add(0, new Label(shapesMap.inverse().get(current_shape )));
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
