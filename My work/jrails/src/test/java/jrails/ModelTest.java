package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ModelTest {

    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new Model(){};
    }

    @Test
    public void id() {
//        assertThat(model.id(), notNullValue());
        Person person1 = new Person();
        person1.name = "hello";
        person1.age = 36;
        person1.iscool = true;
        person1.save();

        Person person2 = new Person();
        person2.name = "Lil Jeff";
        person2.age = 22;
        person2.iscool = true;
        person2.save();

        Person result = Person.find(Person.class, 1);
        System.out.println(result.id());

    }

    @After
    public void tearDown() throws Exception {

    }
}