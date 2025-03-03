package edu.byui.apj.storefront.tutorial91.model;

public class Animal {
    private int id;
    private String name;
    private int age;
    private String diet;
    private String species;

    // Constructors, getters, setters, and toString()
    public Animal() {
    }

    public Animal(int id, String name, int age, String diet, String species) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.diet = diet;
        this.species = species;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "Animal{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", age=" + age +
            ", diet='" + diet + '\'' +
            ", species='" + species + '\'' +
            '}';
    }
}
