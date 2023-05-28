package knyazev.reflection;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplicationContext {

    private static final List<Object> hashComponents = new ArrayList<>();

    public static void injectDependencies() throws Exception {
        findComponents("knyazev");
        inject1();
        inject2("knyazev");
    }

    private static void inject1() throws IllegalAccessException {
        for(Object obj: hashComponents) {
            for(Field field: obj.getClass().getDeclaredFields()) {
                if(field.isAnnotationPresent(DependencyInjection.class)) {
                    for(Object hashObj: hashComponents) {
                        if(field.getType() == hashObj.getClass()) {
                            boolean isAccessible = field.isAccessible();
                            field.setAccessible(true);
                            field.set(obj, hashObj);
                            field.setAccessible(isAccessible);
                        }
                    }
                }
            }
        }
    }

    private static void inject2(String packageName) throws Exception {
        String pack = packageName.replaceAll("[.]", "/");
        String path = "./src/main/java/" + pack;
        File[] files = new File(path).listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    inject2(packageName + "." + file.getName());
                }
            }
        } else {
            throw new Exception("There is no files");
        }
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(pack);
        if(stream == null) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .forEach(line -> {
                    for(Field field : line.getDeclaredFields()) {
                        if(field.isAnnotationPresent(DependencyInjection.class)) {
                            for(Object hashObj : hashComponents) {
                                if(field.getType() == hashObj.getClass()) {
                                    boolean isAccessible = field.isAccessible();
                                    field.setAccessible(true);
                                    try {
                                        field.set(field.getDeclaringClass().newInstance(), hashObj);
                                    } catch (IllegalAccessException | InstantiationException e) {
                                        throw new RuntimeException(e);
                                    }
                                    field.setAccessible(isAccessible);
                                }
                            }
                        }
                    }
                });
    }

    private static void findComponents(String packageName) throws Exception {
        String pack = packageName.replaceAll("[.]", "/");
        String path = "./src/main/java/" + pack;
        File[] files = new File(path).listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findComponents(packageName + "." + file.getName());
                }
            }
        } else {
            throw new Exception("There is no files");
        }
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(pack);
        if(stream == null) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<Object> objects = reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .filter(Objects::nonNull)
                .filter(line -> line.isAnnotationPresent(Component.class))
                .map(line -> {
                    try {
                        return line.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        hashComponents.addAll(objects);
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

}