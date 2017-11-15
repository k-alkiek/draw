package controllers;

import controllers.commands.ICommand;
import controllers.commands.commandsClasses.*;
import controllers.interfaces.DrawingEngine;
import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;
import models.shapes.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by khaledabdelfattah on 10/27/17.
 */
public class Painter implements DrawingEngine {

    private ICommand add, remove, update;
    private ArrayList<Shape> shapes;
    private Stack<ICommand> commands, undoCommands;
    private Saver save;
    private Loader load;
    private static List<Class<? extends Shape>> supportedShapes;

    public Painter () {
        shapes = new ArrayList<>();
        commands = new Stack<>();
        undoCommands = new Stack<>();
    }

    @Override
    public void refresh(GraphicsContext canvas) {
        for (int i = 0; i < shapes.size(); i ++) {
            shapes.get(i).draw(canvas);
        }
    }

    @Override
    public void addShape(Shape shape) {
        try {
            add = new AddShapeCommand(shape, shapes);
            add.execute();
            commands.push(add);
            undoCommands.clear();
        } catch (EmptyStackException exception) {
            throw exception;
        }
    }

    public void addShapePreview(Shape shape) {
        try {
            add = new AddShapeCommand(shape, shapes);
            add.execute();
        } catch (EmptyStackException exception) {
            throw exception;
        }
    }

    @Override
    public void removeShape(Shape shape) {
        try {
            remove = new RemoveShapeCommand(shape, shapes);
            remove.execute();
            commands.add(remove);
            undoCommands.clear();
        } catch (EmptyStackException exception) {
            throw exception;
        }
    }

    public void removeShapePreview(Shape shape) {
        try {
            remove = new RemoveShapeCommand(shape, shapes);
            remove.execute();
        } catch (EmptyStackException exception) {
            throw exception;
        }
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        try {
            update = new UpdateShapeCommand(oldShape, newShape, shapes);
            update.execute();
            commands.push(update);
            undoCommands.clear();
        } catch (EmptyStackException exception) {
            throw exception;
        }
    }

    @Override
    public Shape[] getShapes() {
        Shape[] currentShapes = new Shape[shapes.size()];
        int i = 0;
        for (Shape shape : shapes) {
            currentShapes[i ++] = shape;
        }
        return currentShapes;
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        supportedShapes = new LinkedList<>();
        supportedShapes.add(Circle.class);
        supportedShapes.add(Rectangle.class);
        supportedShapes.add(Ellipse.class);
        supportedShapes.add(Line.class);
        supportedShapes.add(RoundRectangle.class);
        supportedShapes.add(Triangle.class);
        return supportedShapes;
    }

    @Override
    public void undo() {
        try {
            ICommand curCommand = commands.pop();
            curCommand.unexecute();
            undoCommands.push(curCommand);
        } catch (Exception exception) {
            throw new EmptyStackException();
        }
    }

    @Override
    public void redo() {
        try {
            ICommand curCommand = undoCommands.pop();
            curCommand.execute();
            commands.push(curCommand);
        } catch (Exception exception) {
            throw new EmptyStackException();
        }
    }

    @Override
    public void save(String path) {
        try {
            save = new Saver(path, shapes);
            save.save();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String path) {
        try {
            load = new Loader(path);
            shapes = load.load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Class<? extends Shape>> getShapeClasses () {
        return supportedShapes;
    }
}
