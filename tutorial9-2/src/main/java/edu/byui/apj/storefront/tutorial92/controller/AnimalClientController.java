package edu.byui.apj.storefront.tutorial92.controller;

import edu.byui.apj.storefront.tutorial92.model.Animal;
import edu.byui.apj.storefront.tutorial92.service.AnimalClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnimalClientController {

    private final AnimalClientService animalClientService;

    @Autowired
    public AnimalClientController(AnimalClientService animalClientService) {
        this.animalClientService = animalClientService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/getAllAnimals")
    public String getAllAnimalsPage(Model model, @RequestParam(defaultValue = "restTemplate") String method) {
        if ("restTemplate".equals(method)) {
            model.addAttribute("animals", animalClientService.getAllAnimalsRestTemplate());
        } else {
            model.addAttribute("animals", animalClientService.getAllAnimalsWebClient());
        }
        model.addAttribute("method", method);
        return "getAllAnimals";
    }

    @GetMapping("/getAnimalById")
    public String getAnimalByIdPage(Model model) {
        model.addAttribute("animal", new Animal());
        return "getAnimalById";
    }

    @PostMapping("/getAnimalById")
    public String getAnimalById(Model model, @RequestParam int id, @RequestParam(defaultValue = "restTemplate") String method) {
        if ("restTemplate".equals(method)) {
            model.addAttribute("animal", animalClientService.getAnimalByIdRestTemplate(id));
        } else {
            model.addAttribute("animal", animalClientService.getAnimalByIdWebClient(id));
        }
        model.addAttribute("method", method);
        return "getAnimalById";
    }

    @GetMapping("/addAnimal")
    public String addAnimalPage(Model model) {
        model.addAttribute("animal", new Animal());
        return "addAnimal";
    }

    @PostMapping("/addAnimal")
    public String addAnimal(Model model, @ModelAttribute Animal animal, @RequestParam(defaultValue = "restTemplate") String method) {
        if ("restTemplate".equals(method)) {
            animalClientService.addAnimalRestTemplate(animal);
        } else {
            animalClientService.addAnimalWebClient(animal);
        }
        return "redirect:/getAllAnimals?method=" + method;
    }

    @GetMapping("/removeAnimal")
    public String removeAnimalPage(Model model) {
        model.addAttribute("animal", new Animal());
        return "removeAnimal";
    }

    @PostMapping("/removeAnimal")
    public String removeAnimal(Model model, @RequestParam int id, @RequestParam(defaultValue = "restTemplate") String method) {
        if ("restTemplate".equals(method)) {
            animalClientService.removeAnimalRestTemplate(id);
        } else {
            animalClientService.removeAnimalWebClient(id);
        }
        return "redirect:/getAllAnimals?method=" + method;
    }
}
