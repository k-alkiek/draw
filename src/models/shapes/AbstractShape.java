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
        shapeProperties.put("upperPointX", 0.0);
        shapeProperties.put("upperPointY", 0.0);
        shapeProperties.put("bottomPointX", 0.0);
        shapeProperties.put("bottomPointY", 0.0);
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
        Map<String, Double> newShapeProps = new HashMap<>();
        for(String key : shapeProperties.keySet()) {
            if (key.charAt(0) == 'x' || key.charAt(0) == 'y')
                newShapeProps.put(key, shapeProperties.get(key) + 0);
            else
                newShapeProps.put(key, shapeProperties.get(key));
        }
        newShape.setProperties(newShapeProps);
        newShape.setColor(perimeterColor);
        newShape.setFillColor(fillColor);
    }

    protected void setAttributes() {
        height = Math.abs(shapeProperties.get("y1") - shapeProperties.get("y2"));
        width = Math.abs(shapeProperties.get("x1") - shapeProperties.get("x2"));
        setBoundaries();
    }

    protected void setBoundaries() {
        Double x1, y1, x2, y2;
        x1 = shapeProperties.get("x1");
        x2 = shapeProperties.get("x2");
        y1 = shapeProperties.get("y1");
        y2 = shapeProperties.get("y2");
        for (String key : shapeProperties.keySet()) {
            if (key.charAt(0) == 'x') {
                x1 = Math.min(x1, shapeProperties.get(key));
                x2 = Math.max(x2, shapeProperties.get(key));
            } else if (key.charAt(0) == 'y') {
                y1 = Math.min(y1, shapeProperties.get(key));
                y2 = Math.max(y2, shapeProperties.get(key));
            }
        }
        shapeProperties.put("upperPointX", x1);
        shapeProperties.put("upperPointY", y1);
        shapeProperties.put("bottomPointX", x2);
        shapeProperties.put("bottomPointY", y2);
    }
}
