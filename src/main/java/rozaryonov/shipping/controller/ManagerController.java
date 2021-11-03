package rozaryonov.shipping.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.service.ManagerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/")

public class ManagerController {
	private final ManagerService managerService;

	@GetMapping("/cabinet")
	public String cabinet (HttpSession session) {
		return "/manager/cabinet"; 
	}
	
	@GetMapping("/message")
	public String getMessage() {
		return "/manager/message";
	}

	
	@GetMapping("/settlements/payment_form")
	public String paymentsShow (@ModelAttribute("settlements") SettlementsDto settlements, HttpSession session, HttpServletRequest request) {
		managerService.paymentsShow(settlements, session, request);
		return "/manager/settlements";
	}
	
	@PostMapping("/settlements")
	public String paymentsCreate (@ModelAttribute ("settlements") @Valid SettlementsDto settlements, 
			BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
		managerService.paymentsCreate(settlements, bindingResult, request, session);
		return "redirect:/manager/settlements/payment_form";
	}

	@GetMapping("/invoices/form")
	public String showCreateInvoicesForm (HttpSession session, HttpServletRequest request) {
		managerService.showCreateInvoicesForm (session, request);
		return "/manager/invoices/form";
	}
	
	@PostMapping("/invoices")
	public String createInvoices(HttpServletRequest request) {
		managerService.createInvoices(request);
		return "redirect:/manager/invoices/form";
	}
	
	@GetMapping("/shippings/finish_form")
	public String showFinishShippingsForm (@ModelAttribute("shippingDto") ShippingToFinishDto shippingDto, HttpSession session, HttpServletRequest request) {
		managerService.showFinishShippingsForm (shippingDto, session, request);
		return "/manager/shippings/finish_form";
	}
	
	@PostMapping("/shippings")
	public String finishShippings(@ModelAttribute("shippingDto") @Valid ShippingToFinishDto shippingDto, BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
		managerService.finishShippings(shippingDto, bindingResult, request, session);
		return "redirect:/manager/shippings/finish_form";
	}
	
	@GetMapping("/report_day")
	public String reportDay(HttpSession session, HttpServletRequest request) {
		return managerService.reportDay(session, request);
	}
	
	@GetMapping("/report_direction")
	public String reportDirection(HttpSession session, HttpServletRequest request) {
		return managerService.reportDirection(session, request);
	}
	
	
}
