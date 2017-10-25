package models.interfaces;

/**
 * shape interface that uses JavaFX Objects.
 * shapes factory
 */
public interface Shape {


    public void setPosition(javafx.geometry.Point2D position);
    public javafx.geometry.Point2D getPosition();

    /* update shape specific properties (e.g., radius) */
    public void setProperties(java.util.Map<String, Double> properties);
    public java.util.Map<String, Double> getProperties();

    // javafx.scene.paint.Color
    public void setColor(javafx.scene.paint.Color color);
    public javafx.scene.paint.Color getColor();

    public void setFillColor(javafx.scene.paint.Color color);
    public javafx.scene.paint.Color getFillColor();

    /* redraw the shape on the canvas */
    public void draw(javafx.scene.canvas.GraphicsContext canvas);

    /* create a deep clone of the shape */
    public Object clone() throws CloneNotSupportedException;
}
