package com.example.jorge.mytestacromax.model;

/**
 * Created by jorge on 01/11/2017.
 */


/*** Model of the Json  */
public class Player {

    /*** Fields */
    private int id;
    private String name;
    private String file;
    private String type;

    /*** Get and Set */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
