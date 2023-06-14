package jrails;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JRouter {

    public Map<String, String> allroutes = new HashMap<>();

    public void addRoute(String verb, String path, Class clazz, String method) {
        //store verb and path in map concatenated together as key and clazz#method together concatenated like that
        allroutes.put(verb + path, clazz.getName() + "#" + method);
    }

    // Returns "clazz#method" corresponding to verb+URN
    // Null if no such route
    public String getRoute(String verb, String path) {
        //concat verb and path and use that key to search map and return corresponding clazz#method
        return allroutes.get(verb + path);
    }

    // Call the appropriate controller method and
    // return the result
    public Html route(String verb, String path, Map<String, String> params) {
        //get corresponding clazz#method for route
        String route = getRoute(verb, path);
        //check if null, if so return null
        if(route == null){
            throw new UnsupportedOperationException("no such route in map");
        }
        //separate clazz and method and store in an array
        String[] routesplit = route.split("#");
        //creat instance of clazz, so we can call method on it
        try{
            //get class
            Class<?> c = Class.forName(routesplit[0]);
            //get constructor
            Constructor<?> cons = null;
            try {
                cons = c.getConstructor();
            } catch (Exception e) {
                throw new UnsupportedOperationException("no constructer found");
            }
            //create instance of class using constructor
            Object o = null;
            try {
                o = cons.newInstance();
            } catch (Exception e) {
                throw new UnsupportedOperationException("problem with new instance");
            }

            try {
                //get method we want to invoke
                Method MethodToUse = c.getMethod(routesplit[1], Map.class);
                //run method and return the results
                return (Html) MethodToUse.invoke(o, params);

            }catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
