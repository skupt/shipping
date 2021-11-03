package rozaryonov.shipping.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class ShippingAdviceController {
	
	@ExceptionHandler(ConnectionGettingException.class)
	public String handleShippingExceptions (ConnectionGettingException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(GuestSerivceException.class)
	public String handleShippingExceptions (GuestSerivceException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return "/error/5xx";
	}

	@ExceptionHandler(InvoiceNotFoundException.class)
	public String handleShippingExceptions (InvoiceNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(InvoiceStatusNotFound.class)
	public String handleShippingExceptions (InvoiceStatusNotFound e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(LocalityNotFoundException.class)
	public String handleShippingExceptions (LocalityNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(LogisticConfigNotFoundException.class)
	public String handleShippingExceptions (LogisticConfigNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(LogisticNetNotFoundException.class)
	public String handleShippingExceptions (LogisticNetNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(PaymentCreationException.class)
	public String handleShippingExceptions (PaymentCreationException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(PageableListFindingException.class)
	public String handleShippingExceptions (PageableListFindingException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(PersonBalanceCalculationException.class)
	public String handleShippingExceptions (PersonBalanceCalculationException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(PersonNotFoundException.class)
	public String handleShippingExceptions (PersonNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(PropertyNotFoundException.class)
	public String handleShippingExceptions (PropertyNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(RoleNotFoundException.class)
	public String handleShippingExceptions (RoleNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(SettlementsTypeNotFoundException.class)
	public String handleShippingExceptions (SettlementsTypeNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(ShippingNotFoundException.class)
	public String handleShippingExceptions (Exception e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(ShippingStatusNotFoundException.class )
	public String handleExceptions (ShippingStatusNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(TariffNotFoundException.class )
	public String handleExceptions (TariffNotFoundException e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

	@ExceptionHandler(Exception.class )
	public String handleExceptions (Exception e, Model model, HttpServletResponse response) {
		log.error(e.getMessage(), e); //+ stacktrace
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}



}
