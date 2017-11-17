package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 11/12/17.
 */
public class Triangle extends AbstractShape {
    private double[] xPoints, yPoints;
    public Triangle () {
        super();
        shapeProperties = new HashMap<>();
        shapeProperties.put("x1", 0.0);
        shapeProperties.put("y1", 0.0);
        shapeProperties.put("x2", 0.0);
        shapeProperties.put("y2", 0.0);
        shapeProperties.put("x3", 0.0);
        shapeProperties.put("y3", 0.0);
        shapeProperties.put("borderWidth", 0.0);
        shapeProperties.put("upperPointX", 0.0);
        shapeProperties.put("upperPointY", 0.0);
        shapeProperties.put("bottomPointX", 0.0);
        shapeProperties.put("bottomPointY", 0.0);
    }
    @Override
    public void draw(GraphicsContext canvas) {
        xPoints = new double[] {shapeProperties.get("x1"),
                shapeProperties.get("x2"),
                shapeProperties.get("x3")};
        yPoints = new double[] {shapeProperties.get("y1"),
                shapeProperties.get("y2"),
                shapeProperties.get("y3")};
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillPolygon(xPoints, yPoints, 3);
        canvas.setStroke(perimeterColor);
        canvas.strokePolygon(xPoints, yPoints, 3);
        setBoundaries();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newTriangle = new Triangle();
        cloning(newTriangle, this);
        return newTriangle;
    }
}
