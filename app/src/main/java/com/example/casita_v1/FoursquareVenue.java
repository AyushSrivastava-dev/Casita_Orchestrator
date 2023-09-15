package com.example.casita_v1;
public class FoursquareVenue {
    private String name;
    private String city;
    private String id;

    private String category;

    public FoursquareVenue() {
        this.name = "";
        this.city = "";
        this.id = "";
        this.setCategory("");
    }

    public String getCity() {
        if (city.length()>0) {
            return city;
        }
        return city;
    }

    public void setCity(String city) {
        if (city != null) {
            this.city = city.replaceAll("\\(", "").replaceAll("\\)", "");
            ;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
