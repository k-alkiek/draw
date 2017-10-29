package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 10/25/17.
 */
public class Rectangle extends AbstractShape {
    public Rectangle () {
        shapeProperties = new HashMap<>();
        shapeProperties.put("height", 0.0);
        shapeProperties.put("width", 0.0);
        shapeProperties.put("borderWidth", 0.0);
    }
    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillRect(centrePoint.getX(),
                centrePoint.getY(),
                shapeProperties.get("height"),
                shapeProperties.get("width"));
        canvas.setStroke(perimeterColor);
        canvas.strokeRect(centrePoint.getX(),
                centrePoint.getY(),
                shapeProperties.get("height"),
                shapeProperties.get("width"));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newRectangle = new Rectangle();
        cloning(newRectangle, this);
        return newRectangle;
    }
}
