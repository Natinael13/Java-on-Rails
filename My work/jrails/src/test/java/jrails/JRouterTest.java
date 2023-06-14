package jrails;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JRouterTest {

    private JRouter jRouter;

    @Before
    public void setUp() throws Exception {
        jRouter = new JRouter();
    }

    @Test
    public void addRoute() {
        jRouter.addRoute("GET", "/", Controller.class, "testcontroller");
        assertThat(jRouter.getRoute("GET", "/"), is("jrails.Controller#testcontroller"));
        Map<String, String> testparams = new HashMap<>();
        testparams.put("hello", "world");
        Html test = jRouter.route("GET", "/", testparams);
    }
}