package rozaryonov.shipping.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.ShippingStatus;
import rozaryonov.shipping.repository.InvoiceRepository;
import rozaryonov.shipping.repository.InvoiceStatusRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.ShippingStatusRepository;
import rozaryonov.shipping.service.ManagerService;
import rozaryonov.shipping.service.SettlementsService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/")
public class ManagerController {
	private static Logger logger = LogManager.getLogger();
	
	private final ManagerService managerService;
	private final ShippingRepository shippingRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;
	private final InvoiceRepository invoiceRepository;
	private final ShippingStatusRepository shippingStatusRepository;

	@GetMapping("/cabinet")
	public String cabinet() {
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

	@SuppressWarnings("unchecked")
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
	
	
}
