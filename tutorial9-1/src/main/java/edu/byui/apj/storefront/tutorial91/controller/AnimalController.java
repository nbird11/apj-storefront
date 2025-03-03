package edu.byui.apj.storefront.tutorial91.controller;

import edu.byui.apj.storefront.tutorial91.model.Animal;
import edu.byui.apj.storefront.tutorial91.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/{id}")
    public Animal getAnimalById(@PathVariable int id) {
        return animalService.getAnimalById(id);
    }

    @GetMapping("/diet/{diet}")
    public List<Animal> getAnimalsByDiet(@PathVariable String diet) {
        return animalService.getAnimalsByDiet(diet);
    }

    @GetMapping("/search")
    public List<Animal> searchAnimalsByName(@RequestParam String name) {
        return animalService.searchAnimalsByName(name);
    }

    @PostMapping
    public void addAnimal(@RequestBody Animal animal) {
        animalService.addAnimal(animal);
    }

    @DeleteMapping("/{id}")
    public void removeAnimal(@PathVariable int id) {
        animalService.removeAnimal(id);
    }
}
