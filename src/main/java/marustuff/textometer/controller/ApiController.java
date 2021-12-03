package marustuff.textometer.controller;


import lombok.RequiredArgsConstructor;
import marustuff.textometer.EmptyWebsiteRepositoryException;
import marustuff.textometer.model.Metering;

import marustuff.textometer.model.Website;
import marustuff.textometer.service.RequestService;
import marustuff.textometer.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    @Autowired
    private final RequestService requestService;
    @Autowired
    private final WebsiteService websiteService;

    @GetMapping("/metering")
    public Iterable<Metering> findAllMeterings() {
        return requestService.findAll();
    }

    @GetMapping(value = "/metering/{word}")
    public Metering findById(@PathVariable("word") String word) {
        return requestService.findByWord(word);
    }

    @GetMapping("/website")
    public Iterable<Website> findAllWebsite() {
        try {
            return websiteService.findAll();
        } catch (EmptyWebsiteRepositoryException e) {
            return null;
        }
    }

    @PostMapping("/website/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Website addWebsite(@RequestBody Website website) {
        return websiteService.apiSaveWebsite(website);
    }

    @DeleteMapping("/website/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteWebsite(@PathVariable("id") Long id) {
        return websiteService.removeWebsite(id);
    }
}
