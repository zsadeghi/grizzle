package me.theyinspire.projects.grizzle.web;

import me.theyinspire.projects.grizzle.service.ResultPage;
import me.theyinspire.projects.grizzle.service.SearchService;
import me.theyinspire.projects.grizzle.model.Lyrics;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.POST, value = {"", "/"})
    public ResultPage<Lyrics> findAndRank(@RequestBody String query,
                                          @RequestParam(value = "tokens", defaultValue = "false") Boolean includeTokens,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return searchService.rank(query, includeTokens, page);
    }

}
