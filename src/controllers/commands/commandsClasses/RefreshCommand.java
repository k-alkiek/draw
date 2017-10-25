package controllers.commands.commandsClasses;

import javafx.scene.canvas.GraphicsContext;
import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.List;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class RefreshCommand implements ICommand {
    private List<Shape> shapes;
    private GraphicsContext canvas;

    public RefreshCommand (List<Shape> shapes, GraphicsContext canvas) {
        this.shapes = shapes;
        this.canvas = canvas;
    }
    @Override
    public void execute() {
        for (int i = 0; i < shapes.size(); i ++) {
            shapes.get(i).draw(canvas);
        }
    }
}
