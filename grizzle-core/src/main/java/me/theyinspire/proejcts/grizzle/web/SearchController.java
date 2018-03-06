package me.theyinspire.proejcts.grizzle.web;

import me.theyinspire.proejcts.grizzle.service.SearchService;
import me.theyinspire.projects.grizzle.model.Lyrics;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.POST, value = {"", "/"})
    public List<Lyrics> findAndRank(@RequestBody String query, @RequestParam(value = "tokens", defaultValue = "false")
            Boolean includeTokens) {
        return searchService.rank(query, includeTokens);
    }

}
