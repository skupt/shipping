package rozaryonov.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.exception.InvoiceNotFoundException;
import rozaryonov.shipping.exception.InvoiceStatusNotFound;
import rozaryonov.shipping.exception.SettlementsTypeNotFoundException;
import rozaryonov.shipping.exception.ShippingStatusNotFoundException;
import rozaryonov.shipping.model.*;
import rozaryonov.shipping.repository.*;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.InvoiceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl {
	private static Logger logger = LogManager.getLogger();

	private final ShippingRepository shippingRepository;
	private final ShippingStatusRepository shippingStatusRepository;
	private final PersonServiceImpl personService;
	private final PageableFactory pageableFactory;
	private final InvoiceRepository invoiceRepository;
	private final SettlementsTypeRepository settlementsTypeRepository;
	private final SettlementsRepository settlementsRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;


	@SuppressWarnings("unchecked")
	public String getSpendingForm(HttpSession session, HttpServletRequest request) {
		Person curPerson = (Person) session.getAttribute("person");
		session.setAttribute("balance", personService.calcAndReplaceBalance(curPerson.getId()));
		Page<Invoice, InvoiceService> pageInvoiceToPay = null;
		List<Invoice> invoices = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageInvoiceToPay = (Page<Invoice, InvoiceService>) session.getAttribute("pageInvoiceToPay");
				invoices = pageInvoiceToPay.prevPage();
				session.setAttribute("pageNum", pageInvoiceToPay.getCurPageNum());
				session.setAttribute("totalPages", pageInvoiceToPay.getTotalPages());
				session.setAttribute("invoices", invoices);
				break;
			case "nextPage":
				pageInvoiceToPay = (Page<Invoice, InvoiceService>) session.getAttribute("pageInvoiceToPay");
				invoices = pageInvoiceToPay.nextPage();
				session.setAttribute("pageNum", pageInvoiceToPay.getCurPageNum());
				session.setAttribute("totalPages", pageInvoiceToPay.getTotalPages());
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
			session.setAttribute("totalPages", pageInvoiceToPay.getTotalPages());
			session.setAttribute("invoices", invoices);

		}

		return "/auth_user/settlements/spending_form";
	}

	@Transactional
	public String payInvoice(HttpServletRequest request, HttpSession session) {
		Long invoiceId = Long.parseLong(request.getParameter("invoiceId"));
		Invoice i = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new InvoiceNotFoundException("No Invoice found for id=" + invoiceId));

		Person curPerson = (Person) session.getAttribute("person");
		BigDecimal balance = personService.calcAndReplaceBalance(curPerson.getId());
		BigDecimal sum = i.getSum();
		Settlements paym = new Settlements();
		paym.setPerson(curPerson);
		paym.setCreationDatetime(LocalDateTime.now());
		paym.setAmount(sum);
		paym.setSettlementType(settlementsTypeRepository.findById(2L)
				.orElseThrow(() -> new SettlementsTypeNotFoundException("No SettlementType found for id=2")));
		if (balance.compareTo(sum) >= 0) {
			try {
				settlementsRepository.save(paym);
				long personId = curPerson.getId();
				personService.calcAndReplaceBalance(personId);
				i.setInvoiceStatus(invoiceStatusRepository.findById(2L)
						.orElseThrow(() -> new InvoiceStatusNotFound("No InvpoceStatus found for id=2")));
				ShippingStatus delivering = shippingStatusRepository.findById(4L)
						.orElseThrow(() -> new ShippingStatusNotFoundException("No ShippingStatus found for id=4"));
				for (Shipping shp : i.getShippings()) {
					shp.setShippingStatus(delivering);
				}
				invoiceRepository.save(i);
				session.setAttribute("balance", personService.calcAndReplaceBalance(curPerson.getId()));

			} catch (IllegalArgumentException e) {
				logger.warn(e.getMessage());
				throw e;
			}

			Page<Invoice, InvoiceService> pageInvoiceToPay = pageableFactory.getPageableForUserSpendingPage(3,
					curPerson);
			session.setAttribute("pageInvoiceToPay", pageInvoiceToPay);
			List<Invoice> invoices = pageInvoiceToPay.nextPage();
			session.setAttribute("invoices", invoices);
		}
		return "redirect:/auth_user/settlements/spending_form";
	}

}
