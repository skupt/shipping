package rozaryonov.shipping.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.SettlementType;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.ShippingStatus;
import rozaryonov.shipping.repository.InvoiceRepository;
import rozaryonov.shipping.repository.InvoiceStatusRepository;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.SettlementsTypeRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.ShippingStatusRepository;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.ManagerService;
import rozaryonov.shipping.service.SettlementsService;

@Service
@RequiredArgsConstructor
//todo @Slf4j
//todo refactor all calsses to clean code (url naming< var naming, method naming, code readibility)
//todo refactor all calsses to Solid (essentially Single responsibility)
//todo use ultimate idea and learn its hotkeys (do not use eclipse!)

public class ManagerServiceImpl implements ManagerService {
	private static Logger logger = LogManager.getLogger();//todo use lombok
	
	private final PersonRepository personRepository;
	private final SettlementsTypeRepository settlementsTypeRepository;
	private final PageableFactory pageableFactory;
	private final SettlementsRepository settlementsRepository;
	private final ShippingRepository shippingRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;
	private final ShippingStatusRepository shippingStatusRepository;
	private final InvoiceRepository invoiceRepository;

	@Override
	public String paymentsShow (@ModelAttribute("settlements") @Valid SettlementsDto settlements, 
			HttpSession session, HttpServletRequest request) {
		rozaryonov.shipping.repository.page.Page<Settlements, SettlementsService> pageSettlementsAddPayment = null;
		List<Settlements> settlementsList = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {//todo use more gracious way like Optional;
			switch (cmd) {//todo make code readibility; extrac to small methods
			case "prevPage":
				pageSettlementsAddPayment = (Page<Settlements, SettlementsService>) session//todo
						.getAttribute("pageSettlementsAddPayment");//todo correct all yellow Idea's code
				settlementsList = pageSettlementsAddPayment.prevPage();
				session.setAttribute("pageNum", pageSettlementsAddPayment.getCurPageNum());
				session.setAttribute("pageTotal", pageSettlementsAddPayment.getTotalPages());
				session.setAttribute("settlementsList", settlementsList);
				break;
			case "nextPage":
				pageSettlementsAddPayment = (rozaryonov.shipping.repository.page.Page<Settlements, SettlementsService>) session
				.getAttribute("pageSettlementsAddPayment");
				settlementsList = pageSettlementsAddPayment.nextPage();
				session.setAttribute("pageNum", pageSettlementsAddPayment.getCurPageNum());
				session.setAttribute("pageTotal", pageSettlementsAddPayment.getTotalPages());
				session.setAttribute("settlementsList", settlementsList);
				break;
			}
		} else {
				pageSettlementsAddPayment = pageableFactory.getPageableForManagerPaymentsPage(6);
				session.setAttribute("pageSettlementsAddPayment", pageSettlementsAddPayment);
				
				settlementsList = pageSettlementsAddPayment.nextPage(); 
				session.setAttribute("pageNum", pageSettlementsAddPayment.getCurPageNum());
				session.setAttribute("pageTotal", pageSettlementsAddPayment.getTotalPages());
				session.setAttribute("settlementsList", settlementsList);
				Role user = Role.findById(2L);
				session.setAttribute("persons", personRepository.findByRole(user)); 

		}

		return "/manager/payments";
	}

	@Transactional//todo learn how it works (learn about inside Proxy)
	@Override
	public String paymentsCreate (@ModelAttribute ("settlements") SettlementsDto settlements, 
			BindingResult bindingResult, 
			HttpServletRequest request, 
			HttpSession session) {
		
		if (bindingResult.hasErrors()) return "/manager/payments";
		
		LocalDateTime paymentDate = null;
		Person person = null;
		BigDecimal amount = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy K:mm a");
			paymentDate = LocalDateTime.parse(request.getParameter("creationDatetime"), 
					formatter);
		
			person = personRepository.findById(Long.parseLong(request.getParameter("person"))).orElseThrow(()-> 
							new DaoException("No Person with id while PaymentInsert"));
			amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
		} catch (Exception e) {
			//nothing
		}
		boolean hasErrors = false;
		if (paymentDate==null) {
			bindingResult.addError(new FieldError("settlements", "creationDatetime", "Wrong date."));
			hasErrors=true;
		}
		if (person==null) {
			bindingResult.addError(new FieldError("settlements", "person", "Wrong person."));
			hasErrors=true;
		}
		if (hasErrors) return "/manager/payments";

		SettlementType settlementType = settlementsTypeRepository.findById(1L).orElseThrow(()->
			new DaoException("No SettlementType with while PaymentInsert"));
		Settlements settlement = new Settlements();
		settlement.setCreationDatetime(paymentDate);
		settlement.setPerson(person);
		settlement.setSettlementType(settlementType);
		settlement.setAmount(amount);
		settlementsRepository.save(settlement);
		session.setAttribute("goTo", "/manager/payments");
		session.setAttribute("message", "prg.paymentOk");
		
		
		return "redirect:/manager/payments";
	}
	
	@Override
	public String showCreateInvoicesForm  (HttpSession session, HttpServletRequest request) {

		
		ShippingStatus justCreated = new ShippingStatus();
		justCreated.setId(1L);

		org.springframework.data.domain.Page<Shipping> pageShipping = null;

		String cmd = request.getParameter("cmd");//todo reanme
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageShipping = (org.springframework.data.domain.Page<Shipping>) session.getAttribute("pageShipping");
				if (pageShipping.hasPrevious()) {
					Pageable pageable = pageShipping.previousPageable();
					pageShipping = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, pageable);
				}
				session.setAttribute("pageShipping", pageShipping);
				session.setAttribute("shippingList", pageShipping.getContent());
				break;
			case "nextPage":
				pageShipping = (org.springframework.data.domain.Page<Shipping>) session.getAttribute("pageShipping");
					Pageable pageable = pageShipping.nextOrLastPageable();
					pageShipping = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(justCreated, pageable);
				session.setAttribute("pageShipping", pageShipping);
				session.setAttribute("shippingList", pageShipping.getContent());
				break;
			}
		} else {
			pageShipping = shippingRepository
				.findAllByShippingStatusOrderByCreationTimestamp(justCreated, PageRequest.of(0, 2));
			session.setAttribute("pageShipping", pageShipping);
			session.setAttribute("shippingList", pageShipping.getContent());
		}
		
		return "/manager/create_invoices";
	}

	@Transactional
	@Override
	public String createInvoices(HttpServletRequest request) {
		
		String[] shippingIds = request.getParameterValues("shippingId");
		Set<Shipping> setShippings = new HashSet<>();
		for (String shippingId : shippingIds) {//todo naming; learn diff of for loop and foreach; LEARN EMERGENT!!!!!!!!!! //todo renaming hotkey shift+f6
			Shipping s = null;//todo naming
			try {
				s = shippingRepository.findById(Long.parseLong(shippingId))//todo hotkeyuy ctrl+q for see method info
						.orElseThrow(() -> new DaoException("No Shipping while CreateInvoices cmd."));
			} catch (DaoException e) {
				logger.warn(e.getMessage());//todo create
			}
			setShippings.add(s);
		}
		// group shippings by Persons
		Map<Person, List<Shipping>> personIdShippingsMap = setShippings.stream()
				.collect(Collectors.groupingBy((se) -> se.getPerson()));//todo learn how write simplified lamdas
		// create invoices
		for (Map.Entry<Person, List<Shipping>> me : personIdShippingsMap.entrySet()) {
			Invoice inv = new Invoice();
			inv.setPerson(me.getKey());
			inv.setCreationDateTime(Timestamp.valueOf(LocalDateTime.now()));
			List<Shipping> shippingSet = me.getValue().stream().collect(Collectors.toList());
			BigDecimal sum = shippingSet.stream().map(x -> x.getFare()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
			inv.setSum(sum);
			inv.setInvoiceStatus(invoiceStatusRepository.findById(1L)
					.orElseThrow(() -> new DaoException("No InvoiceStatus while CreateInvoices cmd"))); 
			for (Shipping sh : shippingSet) {
				sh.setShippingStatus(shippingStatusRepository.findById(2L)
						.orElseThrow(() -> new DaoException ("No ShippingStatus while CreateInvoices cmd"))); 
			}
			inv.setShippings(shippingSet);
			// TRANSACTION save each invoice
			try {
				// update statuses of shippings//todo we don't need extra comments; code should be understandable without it; better write docs if it needed;
				for (Shipping shp : shippingSet) {//todo learn diff of @Transactional and .commit() and . rollback()
					shippingRepository.save(shp);
				}
				// save invoice and it's shippings 
				invoiceRepository.save(inv);
				
			} catch (Exception e) {//todo is it right way to handle exception????
				logger.error(e.getMessage());//todo create custom Exceptions; see Spring @ControllerAdvice and use it here. IMPORTANT!
			}
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("goTo", "/manager/create_invoices");
		session.setAttribute("message", "prg.invoiceOk");

		return "redirect:/manager/prg";//todo extract preffix and suffix in app.properties
	}


	//@Override
	public String showFinishShippingsForm(@ModelAttribute("shippingDto") ShippingToFinishDto shippingDto, HttpSession session, HttpServletRequest request) {
		
		ShippingStatus deliveringStatus = new ShippingStatus();
		deliveringStatus.setId(4L);//todo don't use unanderstandable variabels; make constansp; hotkey ctrl+alt+C

		org.springframework.data.domain.Page<Shipping> pageShippingFinish = null;

		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageShippingFinish = (org.springframework.data.domain.Page<Shipping>) session.getAttribute("pageShippingFinish");//todo import
				if (pageShippingFinish.hasPrevious()) {
					Pageable pageable = pageShippingFinish.previousPageable();
					pageShippingFinish = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(deliveringStatus, pageable);
				}
				session.setAttribute("pageShippingFinish", pageShippingFinish);
				session.setAttribute("shippingListFinish", pageShippingFinish.getContent());
				break;
			case "nextPage":
				pageShippingFinish = (org.springframework.data.domain.Page<Shipping>) session.getAttribute("pageShippingFinish");
					Pageable pageable = pageShippingFinish.nextOrLastPageable();
					pageShippingFinish = shippingRepository.findAllByShippingStatusOrderByCreationTimestamp(deliveringStatus, pageable);
				session.setAttribute("pageShippingFinish", pageShippingFinish);
				session.setAttribute("shippingListFinish", pageShippingFinish.getContent());
				break;
			}
		} else {
			pageShippingFinish = shippingRepository
				.findAllByShippingStatusOrderByCreationTimestamp(deliveringStatus, PageRequest.of(0, 2));
			session.setAttribute("pageShippingFinish", pageShippingFinish);
			session.setAttribute("shippingListFinish", pageShippingFinish.getContent());
		}
		

		return "/manager/finish_shippings";
	}


	@Transactional
	@Override
	public String finishShippings(@ModelAttribute("shippingDto") @Valid ShippingToFinishDto shippingDto,
			BindingResult bindingResult,
			HttpServletRequest request, 
			HttpSession session) {
		
		if (bindingResult.hasErrors()) return "/manager/finish_shippings";
		boolean dataError = false; 
		LocalDateTime unloadDate = null;
		try {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		LocalDate ld = LocalDate.parse(shippingDto.getUnloadDate(), formatter);
		unloadDate = LocalDateTime.of(ld, LocalTime.of(0, 0));
		System.out.println(ld + " " + unloadDate);
		} catch (Exception e) {
			dataError = true;
		}
		if (dataError) {
			bindingResult.addError(new FieldError("shippingDto", "unloadDate", "Wrong date."));
			return "/manager/finish_shippings";
		}
		
		ShippingStatus statusDelivered = shippingStatusRepository.findById(5L)
				.orElseThrow(() -> new DaoException("No ShippingsStstus while FinishShippings"));
		String[] shipIdsStr = request.getParameterValues("shippingId");

		try {
			for (int i = 0; i < shipIdsStr.length; i++) {
				Long shId = Long.parseLong(shipIdsStr[i]);
				if (shId != null) {
					Shipping shipping = shippingRepository.findById(shId)
							.orElseThrow(() -> new DaoException("No Shippings for id while FinishShippings.execute()"));
					shipping.setUnloadingDatetime(Timestamp.valueOf(unloadDate));
					shipping.setShippingStatus(statusDelivered);
					shippingRepository.save(shipping);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		session.setAttribute("goTo", "/manager/finish_shippings");
		session.setAttribute("message", "prg.shippingsFinishedOk");

		
		return "redirect:/manager/prg";
	}
	
	public boolean isManager(HttpSession session) {
		Person person = (Person) session.getAttribute("person");
		if (person!=null && person.getRole().getName().equals("manager")) return true;
		return false;
	}
	
	
}
