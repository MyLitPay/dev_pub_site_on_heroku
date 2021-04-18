package main.api.response.dto;

import main.model.Tag;

public class TagDTO {
    private String name;
    private double weight;

    public TagDTO() {
    }

    public TagDTO(Tag tag, double weight) {
        this.name = tag.getName();
        this.weight = weight;
    }

    public TagDTO(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
