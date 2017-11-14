package controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import models.interfaces.Shape;
import models.shapes.Polygon;
import models.shapes.ShapesFactory;
import models.shapes.Triangle;

import java.net.URL;
import java.util.*;

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
    @FXML JFXComboBox<?> shapesComboBox;

    @FXML JFXBadge undoBadge;
    @FXML JFXBadge redoBadge;
    @FXML JFXBadge saveBadge;

    Painter painter;
    ShapesFactory factory = new ShapesFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        painter = new Painter();
        initializeStrokePreview();
        initializeTools();
        initializeBadges();

        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        System.out.println(canvas.getBoundsInLocal().getMinX());
        System.out.println(canvas.getBoundsInLocal().getMinY());


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

    @FXML
    void redo() {
        painter.redo();
        refresh();
    }

    @FXML
    void save() {
        painter.save("asd.xml");
    }

    @FXML
    void load() {
        painter.load("asd.xml");
        refresh();
    }

    @FXML
    void undo() {
        painter.undo();
        refresh();
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
                painter.addShape(shape);
                painter.refresh(canvas.getGraphicsContext2D());
                clickHistory.clear();
            }
        }
        else {
            canvas.setOnDragDetected(event -> {
                Point2D startPoint = new Point2D(event.getX(), event.getY());
                canvas.setOnMouseDragged(m->{
                    double x = m.getX();
                    double y = m.getY();
                    System.out.println("X coord: " + x);
                    System.out.println("Y coord: " + y);
                    canvas.getGraphicsContext2D().setFill(strokeColorPicker.getValue());
                    canvas.getGraphicsContext2D().fillRect(x, y, strokeSlider.getValue(), strokeSlider.getValue());
                    System.out.println(toolsComboBox.getValue().getText());

                    shape.setPosition(startPoint);
                    shape.setPosition(startPoint);
                });
            });
        }
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

    private void refresh() {
        clear();
        painter.refresh(canvas.getGraphicsContext2D());
    }
}
