package rozaryonov.shipping.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.ShippingStatus;
import rozaryonov.shipping.repository.InvoiceRepository;
import rozaryonov.shipping.repository.InvoiceStatusRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.SettlementsTypeRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.ShippingStatusRepository;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.InvoiceService;
import rozaryonov.shipping.service.PersonService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth_user/")
public class AuthUserController {
	private static Logger logger = LogManager.getLogger();

	
	private final ShippingStatusRepository shippingStatusRepository;
	private final ShippingRepository shippingRepository;
	private final PersonService personService;
	private final PageableFactory pageableFactory;
	private final InvoiceRepository invoiceRepository;
	private final SettlementsTypeRepository settlementsTypeRepository;
	private final SettlementsRepository settlementsRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;

	
	@GetMapping("/cabinet")
	public String cabinet() {
		return "/auth_user/cabinet"; 
	}
	
	@GetMapping("/shippings_new")
	public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
		Person person = (Person) session.getAttribute("person");
		if (person == null) return "redirect:/new";
		return "/auth_user/shippings_new";
	}
	
	@PostMapping("/shippings")
	public String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto, 
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) return "/auth_user/shippings_new";

		//Check timestamp downloadDateTime field
		Timestamp downloadTs0=null;
		try {
			String tsStr = orderDataDto.getDownloadDatetime() + " 00:00:00";
			downloadTs0 = Timestamp.valueOf(tsStr);
		} catch (Exception e) {
			//do nothing
		}
		if (downloadTs0 == null) {
			bindingResult.addError(new FieldError("orderDataDto", "downloadDatetime", "Wrong date."));
			return "/auth_user/shippings_new";
		}

		// Everything is Ok with order, save it
		String shipper = orderDataDto.getShipper();
		String downloadAddress = orderDataDto.getDownloadAddress();
		String consignee = orderDataDto.getConsignee();
		String unloadAddress = orderDataDto.getUnloadAddress();
		double distance = (Double) session.getAttribute("distanceD");
		double weight = (Double) session.getAttribute("weightD");
		double volume = (Double) session.getAttribute("volumeD");
		BigDecimal fare = BigDecimal.valueOf((Double) session.getAttribute("totalD"));
		Long shippingStatusId = 1L;

		session.setAttribute("shipper", shipper);
		session.setAttribute("downloadAddress", downloadAddress);
		session.setAttribute("consignee", consignee);
		session.setAttribute("unloadAddress", unloadAddress);

		Timestamp creationTs = Timestamp.valueOf(LocalDateTime.now());
		Timestamp downloadTs = downloadTs0;
		
		Shipping shipping = Shipping.builder()
				.person((Person) session.getAttribute("person"))
				.creationTimestamp(creationTs)
				.loadLocality((Locality) session.getAttribute("loadLocality"))
				.shipper(shipper)
				.downloadDatetime(downloadTs)
				.downloadAddress(downloadAddress)
				.unloadLocality((Locality) session.getAttribute("unloadLocality"))
				.consignee(consignee)
				.unloadAddress(unloadAddress)
				.distance(distance)
				.weight(weight)
				.volume(volume)
				.fare(fare)
				.shippingStatus(shippingStatusRepository.findById(shippingStatusId)
						.orElseThrow(()-> new DaoException("No sippingStatus while ResumeOrder/shippings method of AuthUserController.")))
				.build();
		
		shippingRepository.save(shipping);	
		return "redirect:/auth_user/cabinet";
	}
	
	@GetMapping("/invoices_of_user")
	public String showInvoices(HttpSession session, HttpServletRequest request) {
		Person curPerson = (Person) session.getAttribute("person");
		session.setAttribute("balance", personService.calcAndReplaceBalance(curPerson.getId()));
		Page<Invoice, InvoiceService> pageInvoiceToPay = null;
		List<Invoice> invoices = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageInvoiceToPay = (Page<Invoice, InvoiceService>) session
						.getAttribute("pageInvoiceToPay");
				invoices = pageInvoiceToPay.prevPage();
				session.setAttribute("pageNum", pageInvoiceToPay.getCurPageNum());
				session.setAttribute("invoices", invoices);
				break;
			case "nextPage":
				pageInvoiceToPay = (Page<Invoice, InvoiceService>) session
				.getAttribute("pageInvoiceToPay");
				invoices = pageInvoiceToPay.nextPage();
				session.setAttribute("pageNum", pageInvoiceToPay.getCurPageNum());
				session.setAttribute("invoices", invoices);
				break;
			}
		} else {
				System.out.println("inside }else{");
			pageInvoiceToPay = pageableFactory.getPageableForUserSpendingPage(3, curPerson);
			session.setAttribute("pageInvoiceToPay", pageInvoiceToPay);
				System.out.println("pageInvoiceToPay " + pageInvoiceToPay);
			invoices = pageInvoiceToPay.nextPage();
			session.setAttribute("pageNum", pageInvoiceToPay.getCurPageNum());
			session.setAttribute("invoices", invoices);
			System.out.println("invoices " + invoices);

		}

		return "/auth_user/invoices_of_user";
	}
	
	@PostMapping("/invoices_of_user")
	public String payInvoice(HttpServletRequest request, HttpSession session) {
		String redirection = null;
		//NumberFormat nf  = NumberFormat.getNumberInstance(request.getLocale());
		//Long l = (Long) nf.parse(request.getParameter("invoiceId"));
		Long invoiceId = Long.parseLong(request.getParameter("invoiceId"));
		Invoice i = invoiceRepository.findById(invoiceId).orElseThrow(() -> new DaoException("No Invoice while PayInvoice cmd."));
		// dangerous operation

		Person curPerson = (Person) session.getAttribute("person");
		BigDecimal balance = personService.calcAndReplaceBalance(curPerson.getId());
		BigDecimal sum = i.getSum();
		// prepare money spending (settlement)
		Settlements paym = new Settlements();
		paym.setPerson(curPerson);
		paym.setCreationDatetime(LocalDateTime.now());
		paym.setAmount(sum);
		paym.setSettlementType(
				settlementsTypeRepository.findById(2L).orElseThrow(() -> new DaoException("No SettlementType found for id=2")));
		// check balance
		if (balance.compareTo(sum) >= 0) {
			System.out.println("Pay invoice: balance >");
			// start transaction
			try {
//			cn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//			cn.setAutoCommit(false);
			settlementsRepository.save(paym);
			
			System.out.println("personService.calcAndReplaceBalance(curPerson.getId()) - BEFORE");
			long personId = curPerson.getId();
			personService.calcAndReplaceBalance(personId);
			System.out.println("settlementsRepository.save(paym) - AFTER");
			i.setInvoiceStatus(
					invoiceStatusRepository.findById(2L).orElseThrow(() -> new DaoException("No InvpoceStatus found for id=2")));
			ShippingStatus delivering  = shippingStatusRepository.findById(4L).orElseThrow(()-> new DaoException("No ShippingStatus found"));
			for (Shipping shp : i.getShippings()) {
				shp.setShippingStatus(delivering);
			}
			//System.out.println("pay invoice: invoice before save: " + i);
			invoiceRepository.save(i);
			//System.out.println("Pay invoice: invoice statuse chenged to paid");
//			cn.commit();
//			cn.setAutoCommit(true);
			// end transaction
			session.setAttribute("balance", personService.calcAndReplaceBalance(curPerson.getId()));

			} catch (Exception e) {
				logger.warn(e.getMessage());
//				try {
//					cn.rollback();
//				} catch (SQLException e1) {
//					logger.warn(e1.getMessage());
//				}
			}

			Page<Invoice, InvoiceService> pageInvoiceToPay = pageableFactory.getPageableForUserSpendingPage(3, curPerson);
			session.setAttribute("pageInvoiceToPay", pageInvoiceToPay);
			List<Invoice> invoices = pageInvoiceToPay.nextPage();
			session.setAttribute("invoices", invoices);
		}
		return "redirect:/auth_user/invoices_of_user";
	}
}
