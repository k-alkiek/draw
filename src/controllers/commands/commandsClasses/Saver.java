package controllers.commands.commandsClasses;

import models.interfaces.Shape;
import org.json.JSONObject;
import org.json.XML;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by khaledabdelfattah on 10/27/17.
 */
public class Saver {
    private FileOutputStream savedFile;
    private ArrayList<Shape> shapes;
    public Saver (String path, ArrayList<Shape> shapes) throws FileNotFoundException {
        try {
            savedFile = new FileOutputStream(path);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.shapes = shapes;
    }

    public void save () throws IOException {
        try {
            XMLEncoder encoder = new XMLEncoder(savedFile);
            ArrayList<Map<String, Double>> properties = new ArrayList<>();
            ArrayList<String> jsonArr = new ArrayList<>();
            for (Shape shape : shapes) {
                properties.add(shape.getProperties());
                JSONObject obj = new JSONObject();
                obj.put("shapeType", shape.getClass().getName());
                obj.put("centerX", shape.getPosition().getX());
                obj.put("centerY", shape.getPosition().getY());
                obj.put("fillColorR", shape.getFillColor().getRed());
                obj.put("fillColorG", shape.getFillColor().getGreen());
                obj.put("fillColorB", shape.getFillColor().getBlue());
                obj.put("fillColorOpacity", shape.getFillColor().getOpacity());
                obj.put("perColorR", shape.getColor().getRed());
                obj.put("perColorG", shape.getColor().getGreen());
                obj.put("perColorB", shape.getColor().getBlue());
                obj.put("perColorOpacity", shape.getColor().getOpacity());
                String xml = XML.toString(obj);
                jsonArr.add(xml);
            }
            encoder.writeObject(properties);
            encoder.writeObject(jsonArr);
            encoder.close();
            savedFile.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
