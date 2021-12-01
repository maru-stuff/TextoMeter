package marustuff.textometer.controller;

import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private static final String modelMetering = "metering";
    private static final String modelMeteringComparision = "vs";
    private static final String indexControlerReturn = "index";


    @RequestMapping("/")
    public String fetchIndex(Model model) {
        Metering metering = new Metering();
        MeteringComparison meteringComparison = new MeteringComparison();
        model.addAttribute(modelMetering, metering);
        model.addAttribute(modelMeteringComparision, meteringComparison);
        return indexControlerReturn;
    }
}