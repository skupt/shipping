package rozaryonov.shipping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rozaryonov.shipping.service.impl.AuthUserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth_user/")
public class AuthUserController {
	private final AuthUserServiceImpl authUserService;
	
	@GetMapping("/cabinet")
	public String cabinet() {
		return "/auth_user/cabinet";
	}
	
	@GetMapping("/settlements/spending_form")
	public String showInvoices(HttpSession session, HttpServletRequest request) {
		authUserService.getSpendingForm(session, request);
		return "/auth_user/settlements/spending_form";
	}
	
	@PostMapping("/settlements")
	public String payInvoice(HttpServletRequest request, HttpSession session) {
		authUserService.payInvoice(request, session);
		return "redirect:/auth_user/settlements/spending_form";
	}
}
