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
        shapeProperties.put("arcLength", 0.0);
        shapeProperties.put("borderWidth", 0.0);
    }
    @Override
    public void draw(GraphicsContext canvas) {
        setAtrributes();
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillRoundRect(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                width,
                height,
                1,
                1);
        canvas.setStroke(perimeterColor);
        canvas.strokeRoundRect(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                width,
                height,
                1,
                1);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newRoundRectangle = new RoundRectangle();
        cloning(newRoundRectangle, this);
        return newRoundRectangle;
    }
}
