package jrails;

import java.util.Map;

public class Controller {

    public static Html testcontroller(Map<String, String> params) {
        System.out.println(params);
        return new Html();
    }

}