package hello;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Value("${name}")
    private String name;
    @Value("${age}")
    private String age;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/info")
    public String userInfo(){
     	String info=String.format("name: %s age: %s",name,age);
     	return info;
    	//return "Hello, Spirng Boot!"+name;
    }

    @Autowired
    private MyBean mybean;

    @RequestMapping("/User")
    public List<User> getUser(){
        return mybean.getList();
    }

}
