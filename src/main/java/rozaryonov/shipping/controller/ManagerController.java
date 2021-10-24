package rozaryonov.shipping.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.service.ManagerService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/")

//todo in parallel learn Spring and its basics
public class ManagerController {
	private static Logger logger = LogManager.getLogger();
	
	private final ManagerService managerService;

	@GetMapping("/cabinet")
	public String cabinet (HttpSession session) {
		return "/manager/cabinet"; 
	}
	
	@GetMapping("/prg")
	public String getPrg() {
		return "/manager/prg"; 
	}

	
	@GetMapping("/payments")
	public String paymentsShow (@ModelAttribute("settlements") SettlementsDto settlements, HttpSession session, HttpServletRequest request) {
		return managerService.paymentsShow(settlements, session, request);
	}
	
	@PostMapping("/payments")
	public String paymentsCreate (@ModelAttribute ("settlements") @Valid SettlementsDto settlements, 
			BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
		return managerService.paymentsCreate(settlements, bindingResult, request, session);
	}

	@GetMapping("/create_invoices")
	public String showCreateInvoicesForm (HttpSession session, HttpServletRequest request) {
		return managerService.showCreateInvoicesForm (session, request);
	}
	
	@PostMapping("/create_invoices")
	public String createInvoices(HttpServletRequest request) {
		return managerService.createInvoices(request);
	}
	
	@GetMapping("/finish_shippings")
	public String showFinishShippingsForm (@ModelAttribute("shippingDto") ShippingToFinishDto shippingDto, HttpSession session, HttpServletRequest request) {
		return managerService.showFinishShippingsForm (shippingDto, session, request);
	}
	
	@PostMapping("/finish_shippings")
	public String finishShippings(@ModelAttribute("shippingDto") @Valid ShippingToFinishDto shippingDto, BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
		return managerService.finishShippings(shippingDto, bindingResult, request, session);
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
