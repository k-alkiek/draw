package models.shapes;

import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 10/26/17.
 */
public class Line extends AbstractShape {

    public Line () {
        super();
        shapeProperties = new HashMap<>();
        shapeProperties.put("x1", 0.0);
        shapeProperties.put("y2", 0.0);
        shapeProperties.put("x2", 0.0);
        shapeProperties.put("y2", 0.0);
        shapeProperties.put("lineWidth", 0.0);
    }
    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("lineWidth"));
        canvas.setStroke(fillColor);
        canvas.strokeLine(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                shapeProperties.get("x2"),
                shapeProperties.get("y2"));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
