package Turistguide.model;

public enum City {

    KØBENHAVN("København"),
    AALBORG("Aalborg"),
    AARHUS("Aarhus"),
    ODENSE("Odense");

    private String name;

    City(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
