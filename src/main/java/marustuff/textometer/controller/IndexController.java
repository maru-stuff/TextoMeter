package marustuff.textometer.controller;

import marustuff.textometer.model.Metering;
import marustuff.textometer.model.Vs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private final String modelMetering="metering";
    private final String modelVs="vs";
    private final String indexControlerReturn="index";
    @RequestMapping("/")
    public String fetchIndex(Model model){
        Metering metering = new Metering();
        Vs vs = new Vs();
        model.addAttribute(modelMetering,metering);
        model.addAttribute(modelVs,vs);
        return indexControlerReturn;
    }
}