package rozaryonov.shipping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.exception.*;
import rozaryonov.shipping.model.*;
import rozaryonov.shipping.repository.*;
import rozaryonov.shipping.repository.page.DayReportRepo;
import rozaryonov.shipping.repository.page.DirectionReportRepo;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.repository.reportable.DayReport;
import rozaryonov.shipping.repository.reportable.DirectionReport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
//todo @Slf4j
//todo refactor all calsses to clean code (url naming< var naming, method naming, code readibility)
//todo refactor all calsses to Solid (essentially Single responsibility)
//todo use ultimate idea and learn its hotkeys (do not use eclipse!)
/* TODO Refactor this class to use only Springframwork's Page. */


public class ManagerServiceImpl {

	private final PersonRepository personRepository;
	private final SettlementsTypeRepository settlementsTypeRepository;
	private final PageableFactory pageableFactory;
	private final SettlementsRepository settlementsRepository;
	private final ShippingRepository shippingRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;
	private final ShippingStatusRepository shippingStatusRepository;
	private final InvoiceRepository invoiceRepository;

	@SuppressWarnings("unchecked")
	public String paymentsShow(@ModelAttribute("settlements") @Valid SettlementsDto settlements, HttpSession session,
			HttpServletRequest request) {
		rozaryonov.shipping.repository.page.Page<Settlements, SettlementsRepository> pageSettlementsAddPayment = null;
		List<Settlements> settlementsList;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {// todo use more gracious way like Optional;
			switch (cmd) {// todo make code readibility; extrac to small methods
			case "prevPage":
				pageSettlementsAddPayment = (Page<Settlements, SettlementsRepository>) session// todo
						.getAttribute("pageSettlementsAddPayment");// todo correct all yellow Idea's code
				settlementsList = pageSettlementsAddPayment.prevPage();
				session.setAttribute("pageNum", pageSettlementsAddPayment.getCurPageNum());
				session.setAttribute("pageTotal", pageSettlementsAddPayment.getTotalPages());
				session.setAttribute("settlementsList", settlementsList);
				break;
			case "nextPage":
				pageSettlementsAddPayment = (rozaryonov.shipping.repository.page.Page<Settlements, SettlementsRepository>) session
						.getAttribute("pageSettlementsAddPayment");
				settlementsList = pageSettlementsAddPayment.nextPage();
				session.setAttribute("pageNum", pageSettlementsAddPayment.getCurPageNum());
				session.setAttribute("pageTotal", pageSettlementsAddPayment.getTotalPages());
				session.setAttribute("settlementsList", settlementsList);
				break;
			}
		} else {
			pageSettlementsAddPayment = pageableFactory.getPageableForManagerPaymentsPage(3);
			session.setAttribute("pageSettlementsAddPayment", pageSettlementsAddPayment);

			settlementsList = pageSettlementsAddPayment.nextPage();
			session.setAttribute("pageNum", pageSettlementsAddPayment.getCurPageNum());
			session.setAttribute("pageTotal", pageSettlementsAddPayment.getTotalPages());
			session.setAttribute("settlementsList", settlementsList);
			Role user = Role.findById(2L);
			session.setAttribute("persons", personRepository.findByRole(user));

		}

		return "/manager/settlements";
	}

	@Transactional // todo learn how it works (learn about inside Proxy)
	public String paymentsCreate(@ModelAttribute("settlements") SettlementsDto settlements, BindingResult bindingResult,
			HttpServletRequest request, HttpSession session) throws NumberFormatException {

		if (bindingResult.hasErrors())
			return "/manager/payments";

		LocalDateTime paymentDate = null;
		Person person = null;
		BigDecimal amount = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy K:mm a");
			paymentDate = LocalDateTime.parse(request.getParameter("creationDatetime"), formatter);

			person = personRepository.findById(Long.parseLong(request.getParameter("person")))
					.orElseThrow(() -> new PersonNotFoundException("No Person with id while PaymentInsert"));
			amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
		} catch (IllegalArgumentException | DateTimeParseException e) {
			log.warn(e.getMessage());
			throw new ManagerSerivceException(e.getMessage());
		}
		boolean hasErrors = false;
		if (paymentDate == null) {
			bindingResult.addError(new FieldError("settlements", "creationDatetime", "Wrong date."));
			hasErrors = true;
		}
		if (person == null) {
			bindingResult.addError(new FieldError("settlements", "person", "Wrong person."));
			hasErrors = true;
		}
		if (hasErrors)
			return "/manager/payments";

		SettlementType settlementType = settlementsTypeRepository.findById(1L)
				.orElseThrow(() -> new SettlementsTypeNotFoundException("No SettlementType with while PaymentInsert"));
		Settlements settlement = new Settlements();
		settlement.setCreationDatetime(paymentDate);
		settlement.setPerson(person);
		settlement.setSettlementType(settlementType);
		settlement.setAmount(amount);
		settlementsRepository.save(settlement);
		session.setAttribute("goTo", "/manager/payments");
		session.setAttribute("message", "prg.paymentOk");

		return "redirect:/manager/settlements/payment_form";
	}

	@SuppressWarnings("unchecked")
	public String showCreateInvoicesForm(HttpSession session, HttpServletRequest request) {

		ShippingStatus justCreated = new ShippingStatus();
		justCreated.setId(1L);

		org.springframework.data.domain.Page<Shipping> pageShipping = null;

		String cmd = request.getParameter("cmd");// todo reanme
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageShipping = (org.springframework.data.domain.Page<Shipping>) session.getAttribute("pageShipping");
				if (pageShipping.hasPrevious()) {
					Pageable pageable = pageShipping.previousPageable();
					pageShipping = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated,
							pageable);
				}
				session.setAttribute("pageShipping", pageShipping);
				session.setAttribute("shippingList", pageShipping.getContent());
				break;
			case "nextPage":
				pageShipping = (org.springframework.data.domain.Page<Shipping>) session.getAttribute("pageShipping");
				Pageable pageable = pageShipping.nextOrLastPageable();
				pageShipping = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated,
						pageable);
				session.setAttribute("pageShipping", pageShipping);
				session.setAttribute("shippingList", pageShipping.getContent());
				break;
			}
		} else {
			pageShipping = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated,
					PageRequest.of(0, 2));
			session.setAttribute("pageShipping", pageShipping);
			session.setAttribute("shippingList", pageShipping.getContent());
		}

		return "/manager/invoices/form";
	}

	@Transactional
	public String createInvoices(HttpServletRequest request) {

		String[] shippingIds = request.getParameterValues("shippingId");
		Set<Shipping> shippingSet = new HashSet<>();
		for (String shippingId : shippingIds) {// todo naming; learn diff of for loop and foreach; LEARN
												// EMERGENT!!!!!!!!!! //todo renaming hotkey shift+f6
			Shipping shipping;
			try {
				shipping = shippingRepository.findById(Long.parseLong(shippingId))// todo hotkeyuy ctrl+q for see method
																					// info
						.orElseThrow(() -> new ShippingNotFoundException(
								"No Shipping found in managerServiceImpl.createInvoices()."));
			} catch (ShippingNotFoundException e) {
				log.warn(e.getMessage());
				throw e;
			}
			shippingSet.add(shipping);
		}
		Map<Person, List<Shipping>> personShippingsMap = shippingSet.stream()
				.collect(Collectors.groupingBy(se -> se.getPerson()));
		for (Map.Entry<Person, List<Shipping>> shippingsOfPerson : personShippingsMap.entrySet()) {
			Invoice inv = new Invoice();
			inv.setPerson(shippingsOfPerson.getKey());
			inv.setCreationDateTime(Timestamp.valueOf(LocalDateTime.now()));
			List<Shipping> shippingList = shippingsOfPerson.getValue().stream().collect(Collectors.toList());
			BigDecimal sum = shippingList.stream().map(x -> x.getFare()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
			inv.setSum(sum);
			inv.setInvoiceStatus(invoiceStatusRepository.findById(1L).orElseThrow(() -> new InvoiceStatusNotFound(
					"No InvoiceStatus found while createInvoices cmd in ManagerService")));
			for (Shipping sh : shippingList) {
				sh.setShippingStatus(shippingStatusRepository.findById(2L).orElseThrow(
						() -> new ShippingStatusNotFoundException("No ShippingStatus while CreateInvoices cmd")));
			}
			inv.setShippings(shippingList);
			for (Shipping shp : shippingList) {// todo learn diff of @Transactional and .commit() and . rollback()
				shippingRepository.save(shp);
			}
			invoiceRepository.save(inv);
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("goTo", "/manager/create_invoices");
		session.setAttribute("message", "prg.invoiceOk");

		return "redirect:/manager/prg";// todo extract preffix and suffix in app.properties
	}

	// @Override
	@SuppressWarnings("unchecked")
	public String showFinishShippingsForm(@ModelAttribute("shippingDto") ShippingToFinishDto shippingDto,
			HttpSession session, HttpServletRequest request) {

		ShippingStatus deliveringStatus = new ShippingStatus();
		deliveringStatus.setId(4L);// todo don't use unanderstandable variabels; make constansp; hotkey ctrl+alt+C

		org.springframework.data.domain.Page<Shipping> pageShippingFinish = null;

		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageShippingFinish = (org.springframework.data.domain.Page<Shipping>) session
						.getAttribute("pageShippingFinish");// todo import
				if (pageShippingFinish.hasPrevious()) {
					Pageable pageable = pageShippingFinish.previousPageable();
					pageShippingFinish = shippingRepository
							.findAllByShippingStatusOrderByCreationTimestamp(deliveringStatus, pageable);
				}
				session.setAttribute("pageShippingFinish", pageShippingFinish);
				session.setAttribute("shippingListFinish", pageShippingFinish.getContent());
				break;
			case "nextPage":
				pageShippingFinish = (org.springframework.data.domain.Page<Shipping>) session
						.getAttribute("pageShippingFinish");
				Pageable pageable = pageShippingFinish.nextOrLastPageable();
				pageShippingFinish = shippingRepository
						.findAllByShippingStatusOrderByCreationTimestamp(deliveringStatus, pageable);
				session.setAttribute("pageShippingFinish", pageShippingFinish);
				session.setAttribute("shippingListFinish", pageShippingFinish.getContent());
				break;
			}
		} else {
			pageShippingFinish = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(deliveringStatus,
					PageRequest.of(0, 2));
			session.setAttribute("pageShippingFinish", pageShippingFinish);
			session.setAttribute("shippingListFinish", pageShippingFinish.getContent());
		}

		return "/manager/finish_shippings";
	}

	@Transactional
	public String finishShippings(@ModelAttribute("shippingDto") @Valid ShippingToFinishDto shippingDto,
			BindingResult bindingResult, HttpServletRequest request, HttpSession session) {

		if (bindingResult.hasErrors())
			return "/manager/finish_shippings";
		boolean dataError = false;
		LocalDateTime unloadDate = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			LocalDate ld = LocalDate.parse(shippingDto.getUnloadDate(), formatter);
			unloadDate = LocalDateTime.of(ld, LocalTime.of(0, 0));
		} catch (DateTimeParseException e) {
			dataError = true;
		}
		if (dataError) {
			bindingResult.addError(new FieldError("shippingDto", "unloadDate", "Wrong date."));
			return "/manager/finish_shippings";
		}

		ShippingStatus statusDelivered = shippingStatusRepository.findById(5L).orElseThrow(
				() -> new ShippingStatusNotFoundException("No ShippingStatus i ManagerService.finishShippings()"));
		String[] shipIdsStr = request.getParameterValues("shippingId");

		for (int i = 0; i < shipIdsStr.length; i++) {
			Long shId = Long.parseLong(shipIdsStr[i]);
			if (shId != null) {
				Shipping shipping = shippingRepository.findById(shId).orElseThrow(
						() -> new ShippingNotFoundException("No Shipping found in ManagerService.finishShippings()"));
				shipping.setUnloadingDatetime(Timestamp.valueOf(unloadDate));
				shipping.setShippingStatus(statusDelivered);
				shippingRepository.save(shipping);
			}
		}

		session.setAttribute("goTo", "/manager/finish_shippings");
		session.setAttribute("message", "prg.shippingsFinishedOk");

		return "redirect:/manager/prg";
	}

	public boolean isManager(HttpSession session) {
		Person person = (Person) session.getAttribute("person");
		if (person != null && person.getRole().getName().equals("manager"))
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public String reportDay(HttpSession session, HttpServletRequest request) {
		rozaryonov.shipping.repository.page.Page<DayReport, DayReportRepo> pageDayReport = null;
		List<DayReport> reportDayList = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageDayReport = (rozaryonov.shipping.repository.page.Page<DayReport, DayReportRepo>) session
						.getAttribute("pageDayReport");
				reportDayList = pageDayReport.prevPage();
				session.setAttribute("pageDayReport", pageDayReport);
				session.setAttribute("reportDayList", reportDayList);
				break;
			case "nextPage":
				pageDayReport = (rozaryonov.shipping.repository.page.Page<DayReport, DayReportRepo>) session
						.getAttribute("pageDayReport");
				reportDayList = pageDayReport.nextPage();
				session.setAttribute("pageDayReport", pageDayReport);
				session.setAttribute("reportDayList", reportDayList);
				break;
			}
		} else {
			pageDayReport = pageableFactory.getPageableForManagerDayReport(3);
			reportDayList = pageDayReport.nextPage();
			session.setAttribute("pageDayReport", pageDayReport);
			session.setAttribute("reportDayList", reportDayList);
		}

		return "/manager/report_day";
	}

	@SuppressWarnings("unchecked")
	public String reportDirection(HttpSession session, HttpServletRequest request) {
		rozaryonov.shipping.repository.page.Page<DirectionReport, DirectionReportRepo> pageDirectionReport = null;
		List<DirectionReport> reportDirectionList = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageDirectionReport = (rozaryonov.shipping.repository.page.Page<DirectionReport, DirectionReportRepo>) session
						.getAttribute("pageDirectionReport");
				reportDirectionList = pageDirectionReport.prevPage();
				session.setAttribute("pageDirectionReport", pageDirectionReport);
				session.setAttribute("reportDirectionList", reportDirectionList);
				break;
			case "nextPage":
				pageDirectionReport = (rozaryonov.shipping.repository.page.Page<DirectionReport, DirectionReportRepo>) session
						.getAttribute("pageDirectionReport");
				reportDirectionList = pageDirectionReport.nextPage();
				session.setAttribute("pageDirectionReport", pageDirectionReport);
				session.setAttribute("reportDirectionList", reportDirectionList);
				break;
			}
		} else {
			pageDirectionReport = pageableFactory.getPageableForManagerDirectionReport(3);
			reportDirectionList = pageDirectionReport.nextPage();
			session.setAttribute("pageDirectionReport", pageDirectionReport);
			session.setAttribute("reportDirectionList", reportDirectionList);
		}

		return "/manager/report_direction";
	}

}
