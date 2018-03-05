package me.theyinspire.projects.grizzle.explorer.web.controller;

import me.theyinspire.projects.grizzle.explorer.data.DatabaseService;
import me.theyinspire.projects.grizzle.explorer.dto.Header;
import me.theyinspire.projects.grizzle.explorer.dto.ResultPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rest/v1/database")
public class DatabaseController {

    private final DatabaseService service;

    public DatabaseController(final DatabaseService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/tables", "/tables/"})
    public List<String> getTableNames() {
        return service.tableNames();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/tables/{name}/pages/{page}", "/tables/{name}/pages/{page}/"})
    public ResultPage fetchData(@PathVariable("name") String name, @PathVariable("page") Integer page) {
        return service.fetch(name, page);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/tables/{name}/{id}", "/tables/{name}/{id}/"})
    public ResultPage fetchRowById(@PathVariable("name") String name, @PathVariable("id") Object id) {
        return service.fetchById(name, id);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/tables/{name}/headers", "/tables/{name}/headers/"})
    public List<Header> fetchHeaders(@PathVariable("name") String name) {
        return service.headers(name);
    }

}
