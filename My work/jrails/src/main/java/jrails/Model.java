package jrails;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Model {

    public int id;
    public static int nextid;

    public void save() {

        //get all fields
        Field[] allfields = this.getClass().getDeclaredFields();
        //now get all @colfields....not needed but leaving here cuz im too lazy to take out
        ArrayList<Field> colfields = colannotationfinder(allfields);
        //now make sure all the @colfields are of appropriate type
        coltypechecker(colfields);


        try {
            //Create db if not already present
            File db = new File("./db.txt");


            //check if instance is a new entry or existing entry and send to appropriate function
            if(this.id == 0){
                NewRow(colfields, db);
            }
            else{
                UpdateRow(colfields, db);
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int id() {
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        //Create db.txt file
        File db = new File("./db.txt");

        //get constructor test complete
        Constructor<?> cons = null;
        try {
            cons = c.getConstructor();
        } catch (Exception e) {
            throw new UnsupportedOperationException("no constructer found");
        }
        Object o = null;
        //create instance of class using constructor
        try {
            o = cons.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("problem with new instance");
        }

        //we will use this to help split each line by comma
        String splitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(db))) {
            String line;
            while ((line = br.readLine()) != null) {
                //put in a split string
                String[] currline = line.split(splitBy);

                //check if that split string[0] aka the class matches the class c we are looking for
                String classString = currline[0];
                if(classString.equals(c.getName())){
                    //if so check if that split string[1] aka the id matches the id we are looking for
                    int idint = Integer.parseInt(currline[1]);
                    if(idint == id){
                        //if so materialize that

                        //get all fields in an array
                        Field[] DeclaredFields = c.getDeclaredFields();
                        //set id
                        Field[] idfield = (Model.class).getFields();
                        idfield[0].set(o, idint);

                        //loop through declared fields setting each field for our object as we go
                        for(int i = 0;i<DeclaredFields.length;i++){
                            Field fieldholder = DeclaredFields[i];
                            //case 1 field is a int
                            if(fieldholder.getType().equals(int.class)){
                                DeclaredFields[i].set(o, Integer.parseInt(currline[(i+2)]));
                            }
                            //case 2 field is a bool
                            if(fieldholder.getType().equals(boolean.class)){
                                DeclaredFields[i].set(o, Boolean.parseBoolean(currline[(i+2)]));
                            }
                            //case 3 field is a string
                            if(fieldholder.getType().equals(String.class)){
                                String currstringholder = currline[(i+2)];
                                //null string case
                                if(currstringholder.equals("null")){
                                    DeclaredFields[i].set(o, null);
                                }
                                //empty string case
                                else if(currstringholder.equals("empty string")){
                                    DeclaredFields[i].set(o, "");
                                }
                                //regular string case
                                else {
                                    String commasback = (currline[(i+2)]).replace('^', ',');
                                    DeclaredFields[i].set(o, commasback);
                                }
                            }
                        }
                        //return instance of class
                        return (T) o;
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //return null showing we did not find such a row
        return null;
    }

    public static <T> List<T> all(Class<T> c) {
        //create ArrayList to appropriate matching rows
        ArrayList<T> ResultList = new ArrayList<>();
        //Create db.txt file
        File db = new File("./db.txt");


        //we will use this to help split each line by comma
        String splitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(db))) {
            String line;
            while ((line = br.readLine()) != null) {
                //put in a split string
                String[] currline = line.split(splitBy);

                //check if that split string[0] aka the class matches the class c we are looking for
                String classString = currline[0];
                if(classString.equals(c.getName())){

                    //get constructor test complete
                    Constructor<?> cons = null;
                    try {
                        cons = c.getConstructor();
                    } catch (Exception e) {
                        throw new UnsupportedOperationException("no constructer found");
                    }
                    Object o = null;
                    //create instance of class using constructor
                    try {
                        o = cons.newInstance();
                    } catch (Exception e) {
                        throw new UnsupportedOperationException("problem with new instance");
                    }

                    //if so materialize that

                    //get all fields in an array
                    Field[] DeclaredFields = c.getDeclaredFields();
                    //set id
                    Field[] idfield = (Model.class).getFields();
                    int idint = Integer.parseInt(currline[1]);
                    idfield[0].set(o, idint);

                        //loop through declared fields setting each field for our object as we go
                        for(int i = 0;i<DeclaredFields.length;i++){

                            Field fieldholder = DeclaredFields[i];
                            //case 1 field is a int
                            if(fieldholder.getType().equals(int.class)){
                                DeclaredFields[i].set(o, Integer.parseInt(currline[(i+2)]));
                            }
                            //case 2 field is a bool
                            if(fieldholder.getType().equals(boolean.class)){
                                DeclaredFields[i].set(o, Boolean.parseBoolean(currline[(i+2)]));
                            }
                            //case 3 field is a string
                            if(fieldholder.getType().equals(String.class)){
                                String currstringholder = currline[(i+2)];
                                //null string case
                                if(currstringholder.equals("null")){
                                    DeclaredFields[i].set(o, null);
                                }
                                //empty string case
                                else if(currstringholder.equals("empty string")){
                                    DeclaredFields[i].set(o, "");
                                }
                                //regular string case
                                else {
                                    String commasback = (currline[(i+2)]).replace('^', ',');
                                    DeclaredFields[i].set(o, commasback);
                                }
                            }
                        }
                        //add instance to array
                        ResultList.add((T)o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //return null showing we did not find such a row
        return ResultList;
    }

    public void destroy() {
        //Create db.txt file
        File db = new File("./db.txt");

        //create new db2 where we will copy over data from first db
        File db2 = new File("./db2.txt");

        try {
            //open db so we can write to it
            FileWriter fstream = new FileWriter("./db2.txt", true);
            PrintWriter writer = new PrintWriter(fstream);

            //loop through db copying over data line by line
            try (BufferedReader br = new BufferedReader(new FileReader(db))) {
                String line;
                while ((line = br.readLine()) != null) {
                    writer.printf(line + "\n");
                }
                //close writer
                writer.close();
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        //clear db so we can copy everything back over
        reset();
        //we will use this to help split each line by comma
        String splitBy = ",";
        //we will use this to see if we ever had to remove the wanted line, if not it was never there thus error
        Boolean somthingremoved = false;
        //copy back data from db2 to db1, except for line with matching id
        try {
            //open db so we can write to it
            FileWriter fstream = new FileWriter("./db.txt", true);
            PrintWriter writer = new PrintWriter(fstream);

            //loop through db copying over data line by line, except for line with matching id
            try (BufferedReader br = new BufferedReader(new FileReader(db2))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] currline = line.split(splitBy);

                    //if current line has wanted id update line
                    int idint = Integer.parseInt(currline[1]);
                    if(idint == this.id){
                        somthingremoved = true;
                    }
                    //otherwise copy over same line
                    else{
                        writer.printf(line + "\n");
                    }
                }
                //close writer
                writer.close();
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        //delete db2, so we can use a new one in the future
        db2.delete();

        if(somthingremoved == false){
            throw new UnsupportedOperationException("tried to remove row that was not present");
        }
        return;

    }

    public static void reset() {
        //Create db file with current db.txt file
        File db = new File("./db.txt");
        try{
            FileWriter fw = new FileWriter(db, false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        }catch(Exception exception){
            System.out.println("Exception in reset()");
        }
    }




    //helper functions
    public static ArrayList<Field>colannotationfinder(Field[] fields) {
        //create ArrayList to store methods with colum annotation
        ArrayList<Field> result = new ArrayList<>();

        //loop through and add all colum annotations
            for (Field f : fields) {
                if (f.isAnnotationPresent(Column.class)) {
                    result.add(f);
                }
            }
        return result;
    }

    public static void coltypechecker(ArrayList<Field> fields){
        //loop through @colum fields and check if each has appropriate type
        for(Field f: fields){
            //check if int
            if((f.getType()) != (int.class)){
                //check if string
                if((f.getType()) != (String.class)){
                    //check if bool
                    if((f.getType()) != (boolean.class)){
                        //if none of these than throw exception
                        throw new UnsupportedOperationException();
                    }
                }
            }
        }
        return;
    }

    public void NewRow(ArrayList<Field> colfields, File db){
        //create logical id
        this.id = (nextid) + (nextid + 1);
        nextid = nextid + 1;

        //if db does not exist create one
        if(db.exists() == false) {
            try {
                //create PrintWriter, so we have a way of writing to file
                PrintWriter writer = new PrintWriter(db);

                //add model type first into the row
                writer.printf(this.getClass().getName() + ",");

                //add id second into row
                writer.printf(Integer.toString(this.id) + ",");

                //enter rest of data
                try {
                    for (int i = 0; i < colfields.size(); i++) {
                        if((colfields.get(i).get(this)) == (null)){
                            writer.printf("null" + ",");
                        }
                        else if((colfields.get(i).get(this).getClass()).equals(String.class)){
                            if(colfields.get(i).get(this).equals("")){
                                writer.printf("empty string" + ",");
                            }
                            else{
                                String holder = (String)(colfields.get(i).get(this));
                                String holder2 = holder.replace(',', '^');
                                writer.printf( holder2 + ",");
                            }
                        }
                        else {
                            writer.printf(colfields.get(i).get(this).toString() + ",");
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                //create new line so next row starts on new line
                writer.printf("\n");
                //close writer
                writer.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        else{
            try {
                //open db so we can write to it
                FileWriter fstream = new FileWriter("./db.txt", true);
                PrintWriter writer = new PrintWriter(fstream);

                //add model type first into the row
                writer.printf(this.getClass().getName() + ",");

                //add id second into row
                writer.printf(Integer.toString(this.id) + ",");

                //enter rest of data
                try {
                    for (int i = 0; i < colfields.size(); i++) {
                        if((colfields.get(i).get(this)) == (null)){
                            writer.printf("null" + ",");
                        }
                        else if((colfields.get(i).get(this).getClass()).equals(String.class)){
                            if(colfields.get(i).get(this).equals("")){
                                writer.printf("empty string" + ",");
                            }
                            else{
                                String holder = (String)(colfields.get(i).get(this));
                                String holder2 = holder.replace(',', '^');
                                writer.printf( holder2 + ",");
                            }
                        }
                        else {
                            writer.printf(colfields.get(i).get(this).toString() + ",");
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                //create new line so next row starts on new line
                writer.printf("\n");
                //close writer
                writer.close();
            }
            catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public void UpdateRow(ArrayList<Field> colfields, File db){
        //create new db2 where we will copy over data from first db
        File db2 = new File("./db2.txt");

        //check if id was actually in the db if not error
        Boolean idthere = false;

        try {
            //open db so we can write to it
            FileWriter fstream = new FileWriter("./db2.txt", true);
            PrintWriter writer = new PrintWriter(fstream);

            //loop through db copying over data line by line
            try (BufferedReader br = new BufferedReader(new FileReader(db))) {
                String line;
                while ((line = br.readLine()) != null) {
                    writer.printf(line + "\n");
                }
                //close writer
                writer.close();
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        //clear db so we can copy everything back over
        reset();
        //we will use this to help split each line by comma
        String splitBy = ",";

        //copy back data from db2 to db1, except replace line with matching id
        try {
            //open db so we can write to it
            FileWriter fstream = new FileWriter("./db.txt", true);
            PrintWriter writer = new PrintWriter(fstream);

            //loop through db copying over data line by line, except replace line with matching id
            try (BufferedReader br = new BufferedReader(new FileReader(db2))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] currline = line.split(splitBy);

                    //if current line has wanted id update line
                    int idint = Integer.parseInt(currline[1]);
                    if(idint == this.id){
                        //add model type first into the row
                        writer.printf(this.getClass().getName() + ",");

                        //add id second into row
                        writer.printf(Integer.toString(this.id) + ",");

                        //enter rest of data
                        try {
                            for (int i = 0; i < colfields.size(); i++) {
                                if ((colfields.get(i).get(this)).equals(null)) {
                                    writer.printf("null" + ",");
                                } else if ((colfields.get(i).get(this).getClass()).equals(String.class)) {
                                    if (colfields.get(i).get(this).equals("")) {
                                        writer.printf("empty string" + ",");
                                    } else {
                                        String holder = (String) (colfields.get(i).get(this));
                                        String holder2 = holder.replace(',', '^');
                                        writer.printf(holder2 + ",");
                                    }
                                } else {
                                    writer.printf(colfields.get(i).get(this).toString() + ",");
                                }
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        //create new line so next row starts on new line
                        writer.printf("\n");
                        //show that id was in fact in db
                        idthere = true;
                    }
                    //otherwise copy over same line
                    else{
                        writer.printf(line + "\n");
                    }
                }
                //close writer
                writer.close();
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        //delete db2, so we can use a new one in the future
        db2.delete();

        //if nid was not in db raise exception
        if(idthere == false){
            throw new UnsupportedOperationException("id not in db but given a nonzero id");
        }

        return;
    }


}
