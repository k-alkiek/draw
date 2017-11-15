package controllers.commands.commandsClasses;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import models.interfaces.IShapesFactory;
import models.interfaces.Shape;
import models.shapes.ShapesFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by khaledabdelfattah on 10/29/17.
 */
public class Loader {
    private FileInputStream savedFile;
    public Loader (String path) throws FileNotFoundException {
        try {
            savedFile = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Shape> load () throws IOException {
        ArrayList<Shape> shapes = new ArrayList<>();
        try {
            XMLDecoder decoder = new XMLDecoder(savedFile);
            ArrayList<Map<String, Double>> properties = (ArrayList<Map<String, Double>>) decoder.readObject();
            ArrayList<String> jsonArr = (ArrayList) decoder.readObject();
            IShapesFactory factory = new ShapesFactory();
            for (int i = 0; i < properties.size(); i ++) {
                JSONObject obj = XML.toJSONObject(jsonArr.get(i));
                Shape shape = factory.createShape(obj.get("shapeType").toString());
                shape.setProperties(properties.get(i));
                shape.setFillColor(new Color((Double) obj.get("fillColorR"),
                        (Double) obj.get("fillColorG"),
                        (Double) obj.get("fillColorB"),
                        (Double) obj.get("fillColorOpacity")));
                shape.setColor(new Color((Double) obj.get("perColorR"),
                        (Double) obj.get("perColorG"),
                        (Double) obj.get("perColorB"),
                        (Double) obj.get("perColorOpacity")));
                shapes.add(shape);
            }
            decoder.close();
            savedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return shapes;
    }
}
