package rozaryonov.shipping.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.AuthUserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth_user/")
public class AuthUserController {
	private final AuthUserService authUserService;
	
	@GetMapping("/cabinet")
	public String cabinet() {
		
		return "/auth_user/cabinet"; 
	}
	
	@GetMapping("/shippings_new")
	public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
		return authUserService.newShipping(session, orderDataDto);
	}
	
	@PostMapping("/shippings")
	public String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto, 
			BindingResult bindingResult, HttpSession session) {
		return authUserService.createShipping(orderDataDto, bindingResult, session);
	}
	
	@GetMapping("/invoices_of_user")
	public String showInvoices(HttpSession session, HttpServletRequest request) {
		return authUserService.showInvoices(session, request);
	}
	
	@PostMapping("/invoices_of_user")
	public String payInvoice(HttpServletRequest request, HttpSession session) {
		return authUserService.payInvoice(request, session);
	}
}
