package cz.todr.brewery.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TestPage {
	
    @RequestMapping("/test")
    public String greeting() {
        return "Hello world! " + LocalDateTime.now().toString();
    }
    
}
