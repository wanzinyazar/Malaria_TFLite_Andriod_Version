package com.jpaw.projectm;

/***
 * The data specified by DataObject will be stored
 * in the database created by DatabaseHelper.
 */

public class DataObject {

    public  static abstract class UserInfo{

        public static final String name = "user_name";
        public static final String parasitized_percentage = "parasitized";
        public static final String uninfected_percentage =  "uninfected" ;
        public static final String TABLE_NAME = "user_info";

    }

    /*
    private String name;
    private double parasitized_percentage;
    private double uninfected_percentage;

    public DataObject(String name, double parasitized_percentage, double uninfected_percentage) {
        this.name = name;
        this.parasitized_percentage = parasitized_percentage;
        this.uninfected_percentage = uninfected_percentage;
    }

    public String getName() {
        return this.name;
    }

    public double getParasitizedPercentage() {
        return this.parasitized_percentage;
    }

    public double getUninfectedPercentage() {
        return this.uninfected_percentage;
    }*/
}
