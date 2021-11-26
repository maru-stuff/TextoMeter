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

    @GetMapping("/list")
    public String listWebsites(Model model){
        //string do stałej
        model.addAttribute("currentWebsites",repository.findAll());
        //string do stałej
        return "list";
    }

    @GetMapping("/add")
    public String addWebsite(Model model){
        Website website = new Website();
        //string do stałej
        model.addAttribute("website", website);
        //string do stałej
        return "add";
    }

    @PostMapping("/submit")
    public String saveWebsite(@ModelAttribute Website website){
        // należałoby najpierw zweryfikować czy website posiada odpowiednie wartości, dodatkowo mamy tutaj repository w użyciu, które aż się prosi o przeniesienie do osobnego serwisu WebsiteService i tam dopiero obsłużeniu tego repozytorium
        repository.save(website);
        //string do stałej
        return "redirect:/website/list";
    }


}
