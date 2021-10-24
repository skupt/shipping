package rozaryonov.shipping.controller;

import javax.el.PropertyNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.GuestSerivceException;
import rozaryonov.shipping.exception.InvoiceNotFoundException;
import rozaryonov.shipping.exception.InvoiceStatusNotFound;
import rozaryonov.shipping.exception.LocalityNotFoundException;
import rozaryonov.shipping.exception.LogisticConfigNotFoundException;
import rozaryonov.shipping.exception.LogisticNetNotFoundException;
import rozaryonov.shipping.exception.ManagerSerivceException;
import rozaryonov.shipping.exception.PageableListFindingException;
import rozaryonov.shipping.exception.PersonBalanceCalculationException;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.exception.RoleNotFoundException;
import rozaryonov.shipping.exception.SettlementsTypeNotFoundException;
import rozaryonov.shipping.exception.ShippingNotFoundException;

@ControllerAdvice
public class ShippingAdviceController {
	
	@ExceptionHandler( {
		ConnectionGettingException.class,
		GuestSerivceException.class, 
		InvoiceNotFoundException.class, 
		InvoiceStatusNotFound.class, 
		LocalityNotFoundException.class, 
		LogisticConfigNotFoundException.class, 
		LogisticNetNotFoundException.class, 
		ManagerSerivceException.class, 
		PageableListFindingException.class, 
		PersonBalanceCalculationException.class, 
		PersonNotFoundException.class, 
		PropertyNotFoundException.class, 
		RoleNotFoundException.class, 
		SettlementsTypeNotFoundException.class, 
		ShippingNotFoundException.class 
		})
	public String handleShippingExceptions (Exception e, Model model, HttpServletResponse response) {
		model.addAttribute("erororDescription", e.getMessage());
		response.setStatus(500);
		return "/error/5xx";
	}

}
