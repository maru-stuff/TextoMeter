package marustuff.textometer.controller;

import marustuff.textometer.model.Metering;
import marustuff.textometer.model.Vs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String fetchIndex(Model model){
        Metering metering = new Metering();
        Vs vs = new Vs();
        model.addAttribute("metering",metering);
        model.addAttribute("vs",vs);
        return "index";
    }
}
