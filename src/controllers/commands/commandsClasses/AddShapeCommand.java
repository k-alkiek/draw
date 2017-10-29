package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.ArrayList;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class AddShapeCommand implements ICommand {
    private Shape shape;
    private ArrayList<Shape> shapes;
    public AddShapeCommand(Shape shape, ArrayList<Shape> shapes) {
        this.shape = shape;
        this.shapes = shapes;
    }
    @Override
    public void execute() {
        shapes.add(shape);
    }

    @Override
    public void unexecute() {
        shapes.remove(shapes.indexOf(shape));
    }
}
