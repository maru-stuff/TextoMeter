package marustuff.textometer.controller;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Website;
import marustuff.textometer.repository.WebsiteRepository;
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
    private final WebsiteRepository repository;
    private final String listWebsitesReturn="list";
    private final String listWebsitesModelAttributeName= "currentWebsites";
    private final String addWebsiteReturn="add";
    private final String addWebsiteModelAttributeName="website";
    private final String saveWebsiteReturn="redirect:/website/list";

    @GetMapping("/list")
    public String listWebsites(Model model){
        model.addAttribute(listWebsitesModelAttributeName,repository.findAll());
        return listWebsitesReturn;
    }

    @GetMapping("/add")
    public String addWebsite(Model model){
        Website website = new Website();
        model.addAttribute(addWebsiteModelAttributeName, website);
        return addWebsiteReturn;
    }

    @PostMapping("/submit")
    public String saveWebsite(@ModelAttribute Website website){
        // należałoby najpierw zweryfikować czy website posiada odpowiednie wartości, dodatkowo mamy tutaj repository w użyciu, które aż się prosi o przeniesienie do osobnego serwisu WebsiteService i tam dopiero obsłużeniu tego repozytorium
        repository.save(website);
        return saveWebsiteReturn;
    }


}
