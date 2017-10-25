package models.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.interfaces.Shape;

import java.util.Map;

/**
 * Created by khaledabdelfattah on 10/25/17.
 */
public abstract class AbstractShape implements Shape {
    protected Point2D centrePoint;
    protected Map<String, Double> shapeProperties;
    protected Color perimeterColor, fillColor;

    public AbstractShape() {
    }

    @Override
    public void setPosition(Point2D position) {
        centrePoint = position;
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
        newShape.setPosition(new Point2D(centrePoint.getX() + 5, centrePoint.getY() + 5));
        newShape.setProperties(shapeProperties);
        newShape.setColor(perimeterColor);
        newShape.setFillColor(fillColor);
    }
}
