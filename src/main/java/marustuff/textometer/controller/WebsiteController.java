package marustuff.textometer.controller;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Website;
import marustuff.textometer.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/website")
@RequiredArgsConstructor
public class WebsiteController {
    @Autowired
    private final WebsiteService websiteService;

    @GetMapping("/list")
    public String listWebsites(Model model) {
        return websiteService.serveListWebsites(model);

    }

    @GetMapping("/add")
    public String addWebsite(Model model) {
        return websiteService.serveAddWebsite(model);

    }

    @PostMapping("/submit")
    public String saveWebsite(@ModelAttribute Website website) {
        return websiteService.serveSaveWebsite(website);
    }
}
