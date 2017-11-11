package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 11/7/17.
 */
public class RoundRectangle extends AbstractShape implements Serializable {

    public RoundRectangle () {
        super();
        shapeProperties = new HashMap<>();
        shapeProperties.put("width", 0.0);
        shapeProperties.put("height", 0.0);
        shapeProperties.put("arcWidth", 0.0);
        shapeProperties.put("arcLength", 0.0);
        shapeProperties.put("borderWidth", 0.0);
    }
    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillRoundRect(centrePoint.getX(),
                centrePoint.getY(),
                shapeProperties.get("width"),
                shapeProperties.get("height"),
                shapeProperties.get("arcWidth"),
                shapeProperties.get("arcHeight"));
        canvas.setStroke(perimeterColor);
        canvas.strokeRoundRect(centrePoint.getX(),
                centrePoint.getY(),
                shapeProperties.get("width"),
                shapeProperties.get("height"),
                shapeProperties.get("arcWidth"),
                shapeProperties.get("arcHeight"));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newRoundRectangle = new RoundRectangle();
        cloning(newRoundRectangle, this);
        return newRoundRectangle;
    }
}
