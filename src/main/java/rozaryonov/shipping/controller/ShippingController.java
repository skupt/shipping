package rozaryonov.shipping.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.service.LocalityServiceImpl;
import rozaryonov.shipping.service.ShippingServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/shippings/")
public class ShippingController {
    @Autowired
    private ShippingServiceImpl shippingService;
    @Autowired
    private LocalityServiceImpl localityService;

    @GetMapping("/calculation_start_form")
    public String getShippingCalculationStartForm(Model model) {
        model.addAttribute("localities", localityService.findAll());
        return "/shippings/calculation_start_form";
    }

    @GetMapping("/calculation_result_form")
    public String getShippingCalculationResultForm(HttpServletRequest request, Model model, HttpSession session) {
        shippingService.shippingCostCalculationResult(request, model, session);
        return "/shippings/calculation_result_form";
    }

    @GetMapping("/form")
    public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
        shippingService.newShipping(session, orderDataDto);
        return "/shippings/form_new";
    }

    @PostMapping("/")
    public String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto,
                                 BindingResult bindingResult, HttpSession session) {
        return shippingService.createShipping(orderDataDto, bindingResult, session);
    }




}
