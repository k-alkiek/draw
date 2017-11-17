package controllers.commands.commandsClasses;

import models.interfaces.Shape;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by khaledabdelfattah on 11/14/17.
 */
public class LoadExtension {
    private String path;
    
    public LoadExtension(String path) {
        this.path = path;
    }

    public List<Class<? extends Shape>> addExtension() throws IOException, ClassNotFoundException {
        List<Class<? extends Shape>> extensions = new ArrayList<>();
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + path +"!/") };
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);

        while(e.hasMoreElements()) {
            JarEntry jarEntry = e.nextElement();
            if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")){
                continue;
            }
            String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
            className = className.replace('/', '.');
            Class newClass = classLoader.loadClass(className);
            if (newClass != Shape.class && Shape.class.isAssignableFrom(newClass))
                extensions.add(newClass);
        }
        return extensions;
    }
}
