package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.util.List;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class AddShapeCommand implements ICommand {
    private Shape shape;
    private GraphicsContext canvas;
    public AddShapeCommand(Shape shape, GraphicsContext canvas) {
        this.shape = shape;
        this.canvas = canvas;
    }
    @Override
    public void execute() {
        shape.draw(canvas);
    }

    @Override
    public void unexecute() {

    }
}
