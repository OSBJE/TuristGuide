package Turistguide.model;

public enum Tags {

    BØRNEVENLIG("Børnevelig"),
    GRATIS("Gratis"),
    KUNST("Kunst"),
    MUSEUM("Museum"),
    NATUR("Natur");

    private final String name;

    Tags(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

}
