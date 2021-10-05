package rozaryonov.shipping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import rozaryonov.shipping.dto.OrderDataDto;

public interface AuthUserService {

	boolean isAuthUser(HttpSession session);
	String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto, 
			BindingResult bindingResult, HttpSession session);
	String showInvoices(HttpSession session, HttpServletRequest request);
	String payInvoice(HttpServletRequest request, HttpSession session);
}
