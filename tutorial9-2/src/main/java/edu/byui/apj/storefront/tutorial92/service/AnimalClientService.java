package edu.byui.apj.storefront.tutorial92.service;

import edu.byui.apj.storefront.tutorial92.model.Animal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AnimalClientService {

    private final String animalServiceUrl;
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public AnimalClientService(@Value("${animal.service.url}") String animalServiceUrl) {
        this.animalServiceUrl = animalServiceUrl;
        this.restTemplate = new RestTemplate();
        this.webClient = WebClient.builder().baseUrl(animalServiceUrl).build();
    }

    public List<Animal> getAllAnimalsRestTemplate() {
        ResponseEntity<List<Animal>> response = restTemplate.exchange(
            animalServiceUrl + "/animals",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });
        return response.getBody();
    }

    public List<Animal> getAllAnimalsWebClient() {
        return webClient.get().uri("/animals").retrieve().bodyToFlux(Animal.class).collectList().block();
    }

    public Animal getAnimalByIdRestTemplate(int id) {
        ResponseEntity<Animal> response = restTemplate.exchange(animalServiceUrl + "/animals/" + id, HttpMethod.GET, null, Animal.class);
        return response.getBody();
    }

    public Animal getAnimalByIdWebClient(int id) {
        return webClient.get().uri("/animals/" + id).retrieve().bodyToMono(Animal.class).block();
    }

    public void addAnimalRestTemplate(Animal animal) {
        restTemplate.postForEntity(animalServiceUrl + "/animals", animal, Void.class);
    }

    public void addAnimalWebClient(Animal animal) {
        webClient.post().uri("/animals").body(Mono.just(animal), Animal.class).retrieve().bodyToMono(Void.class).block();
    }

    public void removeAnimalRestTemplate(int id) {
        restTemplate.delete(animalServiceUrl + "/animals/" + id);
    }

    public void removeAnimalWebClient(int id) {
        webClient.delete().uri("/animals/" + id).retrieve().bodyToMono(Void.class).block();
    }
}
