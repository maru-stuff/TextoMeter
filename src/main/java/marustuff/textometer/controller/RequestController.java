package marustuff.textometer.controller;

import lombok.RequiredArgsConstructor;
import marustuff.textometer.model.Metering;
import marustuff.textometer.model.MeteringComparison;
import marustuff.textometer.service.MeteringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    @Autowired
    private final MeteringService meteringService;

    @PostMapping("/post")
    public String submitWord(@ModelAttribute Metering metering) {
        return meteringService.submitMeteringView(metering);
    }

    @PostMapping("/postvs")
    public String submitComparison(@ModelAttribute MeteringComparison vs) {
        return meteringService.submitComparisonView(vs);
    }

    @GetMapping("/debug/metering")
    public String TestMetering(Model model) {
        return meteringService.debugMetering(model);
    }

    @GetMapping("get/{word}")
    public String serveMetering(@PathVariable String word, Model model) {
        return meteringService.serveMeteringView(word, model);
    }

    @GetMapping("getvs/{word}/{word2}")
    public String serveVsMetering(@PathVariable("word") String word, @PathVariable("word2") String word2, Model model) {
        return meteringService.serveComparisionMeteringView(word, word2, model);

    }
}
