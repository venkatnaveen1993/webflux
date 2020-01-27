package com.nisum.webclient.controller;

import com.nisum.webclient.domain.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@RestController
public class ItemClientController {
    WebClient webClient =WebClient.create("http://localhost:8080");
    @GetMapping("/client/retrieve")
    public Flux<Item> getItemsUsingRetireve(){
       return webClient.get().uri("/v1/items").retrieve().bodyToFlux(Item.class).log();
    }
    @GetMapping("/client/excahange")
    public Flux<Item> getItemsUsingExchange(){
        return webClient.get().uri("/v1/items").exchange().flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class)).log();
    }
    @GetMapping("/client/retrieve/singleitem")
    public Mono<Item> oneretrieve(){
        String id ="ABC";
        return webClient.get().uri("v1/items/{id}",id).retrieve().bodyToMono(Item.class).log();

    }
    @GetMapping("/client/exchange/singleitem")
    public Mono<Item> oneexcahnge(){
        String id ="ABC";
        return webClient.get().uri("v1/items/{id}",id).exchange().flatMap(clientResponse -> clientResponse.bodyToMono(Item.class)).log();

    }
    @PutMapping("client/updateitem/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item){
        Mono<Item> mono = Mono.just(item);
        return webClient.put().uri("v1/items/{id}").body(mono,Item.class).retrieve().bodyToMono(Item.class).log();
    }
    @DeleteMapping("client/deleteitem/{id}")
    public Mono<Void> deleteitem(@PathVariable String id){
        return webClient.delete().uri("v1/items/{id}").retrieve().bodyToMono(Void.class).log();
    }
    @PostMapping("clent/createitem")
    public Mono<Item> createitem(@RequestBody Item item){
        Mono<Item> mono = Mono.just(item);
        return webClient.post().uri("v1/items").contentType(MediaType.APPLICATION_JSON)
                .body(mono,Item.class).retrieve().bodyToMono(Item.class).log();

    }
    @GetMapping("client/retrieve/error")
    public Flux<Item> errorRetrieve(){
        return  webClient.get().uri("v1/items/runtimeexception").retrieve()
                .onStatus(HttpStatus::is5xxServerError,clientResponse -> {
                  Mono<String>monoerror=  clientResponse.bodyToMono(String.class);
                  return monoerror.flatMap((errormessage)->{
                      throw new RuntimeException(errormessage);
                  });
                }).bodyToFlux(Item.class);

    }}

//    @GetMapping("client/retrieve/error")
//    public Flux<Item> errorExchane(){
//        return  webClient.get().uri("v1/items/runtimeexception").exchange().flatMap((clientResponse -> {
//            if (clientResponse.statusCode().is5xxServerError()){
//              return clientResponse.bodyToMono(String.class).flatMap(errormessage->{
//              throw  new RuntimeException(errormessage);
//            });
//            }else{
//
//            return clientResponse.bodyToFlux(Item.class);
//        }));
//
//
//}