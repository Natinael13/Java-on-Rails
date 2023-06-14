package jrails;

public class Person extends Model {
    @Column
    public String name;

    @Column
    public int age;

    @Column
    public boolean iscool;

}
