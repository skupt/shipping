package rozaryonov.shipping.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.exception.InvoiceNotFoundException;
import rozaryonov.shipping.exception.InvoiceStatusNotFound;
import rozaryonov.shipping.exception.SettlementsTypeNotFoundException;
import rozaryonov.shipping.exception.ShippingStatusNotFoundException;
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
import rozaryonov.shipping.service.AuthUserService;
import rozaryonov.shipping.service.InvoiceService;
import rozaryonov.shipping.service.PersonService;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
	private static Logger logger = LogManager.getLogger();

	private final ShippingRepository shippingRepository;
	private final ShippingStatusRepository shippingStatusRepository;
	private final PersonService personService;
	private final PageableFactory pageableFactory;
	private final InvoiceRepository invoiceRepository;
	private final SettlementsTypeRepository settlementsTypeRepository;
	private final SettlementsRepository settlementsRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;

	@Override
	@Transactional
	public String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto,
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors())
			return "/auth_user/shippings_new";
		Timestamp downloadTs0 = null;
		try {
			String tsStr = orderDataDto.getDownloadDatetime() + " 00:00:00";
			downloadTs0 = Timestamp.valueOf(tsStr);
		} catch (IllegalArgumentException e) {
			// do nothing
		}
		if (downloadTs0 == null) {
			bindingResult.addError(new FieldError("orderDataDto", "downloadDatetime", "Wrong date."));
			return "/auth_user/shippings_new";
		}

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

		Shipping shipping = Shipping.builder().person((Person) session.getAttribute("person"))
				.creationTimestamp(creationTs).loadLocality((Locality) session.getAttribute("loadLocality"))
				.shipper(shipper).downloadDatetime(downloadTs).downloadAddress(downloadAddress)
				.unloadLocality((Locality) session.getAttribute("unloadLocality")).consignee(consignee)
				.unloadAddress(unloadAddress).distance(distance).weight(weight).volume(
						volume)
				.fare(fare)
				.shippingStatus(shippingStatusRepository.findById(shippingStatusId)
						.orElseThrow(() -> new ShippingStatusNotFoundException(
								"No SippingStatus found in AuthUserService.createShipping()")))
				.build();

		shippingRepository.save(shipping);
		return "redirect:/auth_user/cabinet";

	}

	@SuppressWarnings("unchecked")
	public String showInvoices(HttpSession session, HttpServletRequest request) {
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

		return "/auth_user/invoices_of_user";
	}

	@Override
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
		return "redirect:/auth_user/invoices_of_user";
	}

	@Override
	public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
		Person person = (Person) session.getAttribute("person");
		if (person == null)
			return "redirect:/new";
		return "/auth_user/shippings_new";
	}
}
