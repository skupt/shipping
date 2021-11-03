package rozaryonov.shipping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.service.AuthUserService;
import rozaryonov.shipping.service.ShippingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth_user/")
public class AuthUserController {
	private final AuthUserService authUserService;
	private final ShippingService shippingService;
	
	@GetMapping("/cabinet")
	public String cabinet() {
		return "/auth_user/cabinet";
	}
	
	@GetMapping("/settlements/spending_form")
	public String showInvoices(HttpSession session, HttpServletRequest request) {
		authUserService.getSpendingForm(session, request);
		return "/auth_user/spending_form";
	}
	
	@PostMapping("/settlements")
	public String payInvoice(HttpServletRequest request, HttpSession session) {
		authUserService.payInvoice(request, session);
		return "redirect:/auth_user/spending_form";
	}
	@GetMapping("/shippings/new_shipping_form")
	public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
		shippingService.newShipping(session, orderDataDto);
		return "/shippings/new_shipping_form";
	}

	@PostMapping("/shippings/")
	public String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto,
								 BindingResult bindingResult, HttpSession session) {
		String page;
		if (shippingService.checkShippingCreationForm(orderDataDto, bindingResult).hasErrors()) {
			page = "/shippings/new_shipping_form";
		} else {
			if (session.getAttribute("person") != null) {
				shippingService.createShipping(orderDataDto, session);
				page = "redirect:/auth_user/cabinet";
			} else {
				page = "redirect:/login";
			}
		}
		return page;
	}

}
