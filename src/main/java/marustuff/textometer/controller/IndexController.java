package marustuff.textometer.controller;

import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private static final String modelMetering = "metering";
    private static final String modelVs = "vs";
    private static final String indexControlerReturn = "index";

    @RequestMapping("/")
    public String fetchIndex(Model model) {
        Metering metering = new Metering();
        MeteringComparison vs = new MeteringComparison();
        model.addAttribute(modelMetering, metering);
        model.addAttribute(modelVs, vs);
        return indexControlerReturn;
    }
}