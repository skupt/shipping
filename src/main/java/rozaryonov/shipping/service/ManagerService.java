package rozaryonov.shipping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.model.Settlements;

public interface ManagerService {
	
	String paymentsShow (SettlementsDto settlements, HttpSession session, HttpServletRequest request);	
	String paymentsCreate(@Valid SettlementsDto settlements, BindingResult bindingResult, HttpServletRequest request,
			HttpSession session);
	
	String showCreateInvoicesForm  (HttpSession session, HttpServletRequest request);
	String createInvoices(HttpServletRequest request);

	String showFinishShippingsForm (ShippingToFinishDto shippingDto, HttpSession session, HttpServletRequest request);
	String finishShippings(ShippingToFinishDto shippingDto, BindingResult bindingResult, HttpServletRequest request, HttpSession session);
	
	boolean isManager(HttpSession session); 
}
