package marustuff.textometer.controller;

import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private static final String MODEL_METERING = "metering";
    private static final String MODEL_METERING_COMPARISON = "vs";
    private static final String INDEX_CONTROLLER_RETURN = "index";


    @RequestMapping("/")
    public String fetchIndex(Model model) {
        Metering metering = new Metering();
        MeteringComparison meteringComparison = new MeteringComparison();
        model.addAttribute(MODEL_METERING, metering);
        model.addAttribute(MODEL_METERING_COMPARISON, meteringComparison);
        return INDEX_CONTROLLER_RETURN;
    }
}