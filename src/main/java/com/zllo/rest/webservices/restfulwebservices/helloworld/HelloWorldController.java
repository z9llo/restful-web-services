package com.zllo.rest.webservices.restfulwebservices.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    public static final String GOOD_MORNING_MESSAGE = "good.morning.message";
    private MessageSource messageSource;

    @Autowired
    public HelloWorldController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World Bean");
    }

    @GetMapping(path = "/hello-world-path-var/{name}")
    public HelloWorldBean helloWorldPathVar(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World var, %s", name));
    }

    @GetMapping(path = "/hello-world-i18n")
    public String helloWorldI18n() {
        return messageSource.getMessage(GOOD_MORNING_MESSAGE, null, LocaleContextHolder.getLocale());
    }
}
