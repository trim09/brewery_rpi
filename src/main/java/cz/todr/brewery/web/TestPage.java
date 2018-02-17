package cz.todr.brewery.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestPage {
	
    @RequestMapping("/test")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        return "Hi, " + name + "!";
    }
    
}
