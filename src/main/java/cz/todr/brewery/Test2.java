package cz.todr.brewery;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class Test2 {

    @Data
    @AllArgsConstructor
    static class ShoppingCart {
        int id;
        int userid;
        List<Long> products;
    }

    @Data
    @AllArgsConstructor
    static class Product {
        long id;
        String text;
        int price;
    }

    @Data
    @AllArgsConstructor
    static class User {
        long id;
        String name;
    }

    Mono<ShoppingCart> getCart(int id) {
        if (id == 123) {
            return Mono.just(new ShoppingCart(id, 7337, ImmutableList.of(444L, 555L)));
        } else {
            return Mono.empty();
        }
    }

    Mono<Product> getProduct(long id) {
        if (id == 444L) {
            return Mono.just(new Product(id, "444 mys", 450));
        } else if (id == 555L) {
            return Mono.just(new Product(id, "555 klavesnice", 1280));
        } else {
            return Mono.empty();
        }
    }

    Mono<User> getUser(int id) {
        if (id == 7337) {
            return Mono.just(new User(id, "Chuck Norris"));
        } else {
            return Mono.empty();
        }
    }

    Flux<Product> getProducts(List<Long> ids) {
        return ids.stream().map(id -> /* TODO <------------------ */)
    }

    private void run() {
        //vstup ID kosiku
        //1. vyhledani kosiku, ktere obsahuje ID uzivatele, list ID produktu
        //2. dohledani produktu
        //3. dohledani uzivatele
        //4. vratit celkovou castku za produkty a pokud je uzivatel == "karel" tak dat 50% slevu

        //val flux = Flux.range(1, 2);
        val mono = Mono.just(456);

        mono.map(id -> {
            ShoppingCart cart = getCart(id);

        })

    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void print(long value, String id) {
        System.out.printf("% 3d %10s on %s%n", value, id, Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        new Thread(() -> new Test2().run(), "MyConsumer").start();
        sleep(3000 );
    }
}
