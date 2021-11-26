package marustuff.textometer;

import lombok.RequiredArgsConstructor;
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
        model.addAttribute("currentWebsites",repository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addWebsite(Model model){
        Website website = new Website();
        model.addAttribute("website", website);
        return "add";
    }

    @PostMapping("/submit")
    public String saveWebsite(@ModelAttribute Website website){
        repository.save(website);
        return "redirect:/website/list";
    }


}
