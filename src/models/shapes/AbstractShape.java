package models.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khaledabdelfattah on 10/25/17.
 */
public abstract class AbstractShape implements Shape, Serializable {
    protected Point2D centrePoint;
    protected Map<String, Double> shapeProperties;
    protected Color perimeterColor, fillColor;
    protected double width, height;

    public AbstractShape() {
        shapeProperties = new HashMap<>();
        shapeProperties.put("x1", 0.0);
        shapeProperties.put("y1", 0.0);
        shapeProperties.put("x2", 0.0);
        shapeProperties.put("y2", 0.0);
        shapeProperties.put("borderWidth", 0.0);
    }

    @Override
    public void setPosition(Point2D position) {
        centrePoint = position;
        shapeProperties.put("x1", position.getX());
        shapeProperties.put("y1", position.getX());
    }

    @Override
    public Point2D getPosition() {
        return centrePoint;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.shapeProperties = properties;
    }

    @Override
    public Map<String, Double> getProperties() {
        return shapeProperties;
    }

    @Override
    public void setColor(Color color) {
        perimeterColor = color;
    }

    @Override
    public Color getColor() {
        return perimeterColor;
    }

    @Override
    public void setFillColor(Color color) {
        fillColor = color;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public abstract void draw(GraphicsContext canvas);

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    protected void cloning (Shape newShape, Shape oldShape) {
        for(String key : shapeProperties.keySet()) {
            if (key.charAt(0) == 'x' || key.charAt(0) == 'y')
                shapeProperties.put(key, shapeProperties.get(key) + 5);
        }
        newShape.setProperties(shapeProperties);
        newShape.setColor(perimeterColor);
        newShape.setFillColor(fillColor);
    }

    protected void setAtrributes() {
        height = Math.abs(shapeProperties.get("y1") - shapeProperties.get("y2"));
        width = Math.abs(shapeProperties.get("x1") - shapeProperties.get("x2"));
    }
}
