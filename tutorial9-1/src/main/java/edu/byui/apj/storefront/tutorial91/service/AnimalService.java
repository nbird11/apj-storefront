package edu.byui.apj.storefront.tutorial91.service;

import edu.byui.apj.storefront.tutorial91.model.Animal;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final List<Animal> animals = new ArrayList<>();

    public AnimalService() {
        loadAnimalsFromCsv();
    }

    private void loadAnimalsFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("animals.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader()
                .build()
                .parse(reader);

            for (CSVRecord record : records) {
                int id = Integer.parseInt(record.get("id"));
                String name = record.get("name");
                int age = Integer.parseInt(record.get("age"));
                String diet = record.get("diet");
                String species = record.get("species");
                animals.add(new Animal(id, name, age, diet, species));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Animal> getAllAnimals() {
        return animals;
    }

    public Animal getAnimalById(int id) {
        return animals.stream().filter(animal -> animal.getId() == id).findFirst().orElse(null);
    }

    public List<Animal> getAnimalsByDiet(String diet) {
        return animals.stream().filter(animal -> animal.getDiet().equalsIgnoreCase(diet)).collect(Collectors.toList());
    }

    public List<Animal> searchAnimalsByName(String name) {
        return animals.stream().filter(animal -> animal.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(int id) {
        animals.removeIf(animal -> animal.getId() == id);
    }
}
