package me.theyinspire.projects.grizzle.importer.web.controllers;

import me.theyinspire.projects.grizzle.importer.input.ConverterStatus;
import me.theyinspire.projects.grizzle.importer.input.InputConverter;
import me.theyinspire.projects.grizzle.importer.web.dto.RunRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest/v1/converter")
public class ConverterController {

    private final InputConverter converter;

    public ConverterController(final InputConverter converter) {
        this.converter = converter;
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/status", "/status/"})
    public ConverterStatus getStatus() {
        return converter.getStatus();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/last", "/last/"})
    public Object getLast() {
        return converter.getLast();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/status/total", "/status/total/"})
    public Long getTotal() {
        return converter.total();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/status/converted", "/status/converted/"})
    public Long getConverted() {
        return converter.converted();
    }

    @RequestMapping(method = RequestMethod.POST, value = {"/start", "/start/"})
    public void start(@RequestBody RunRequest request) {
        new Thread(() -> {
            converter.start(request.getRerun());
        }).start();
    }

    @RequestMapping(method = RequestMethod.POST, value = {"/stop", "/stop/"})
    public void stop() {
        converter.stop();
    }

}
