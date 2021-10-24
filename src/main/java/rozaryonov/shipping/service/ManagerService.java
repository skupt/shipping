package rozaryonov.shipping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;

import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;

public interface ManagerService {
	
	String paymentsShow (SettlementsDto settlements, HttpSession session, HttpServletRequest request);	
	String paymentsCreate(@Valid SettlementsDto settlements, BindingResult bindingResult, HttpServletRequest request,
			HttpSession session);
	String showCreateInvoicesForm  (HttpSession session, HttpServletRequest request);
	String createInvoices(HttpServletRequest request);
	String showFinishShippingsForm (ShippingToFinishDto shippingDto, HttpSession session, HttpServletRequest request);
	String finishShippings(ShippingToFinishDto shippingDto, BindingResult bindingResult, HttpServletRequest request, HttpSession session);
	String reportDay(HttpSession session, HttpServletRequest request);
	String reportDirection(HttpSession session, HttpServletRequest request);
}
