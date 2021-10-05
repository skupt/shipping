package rozaryonov.shipping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import rozaryonov.shipping.dto.PersonDto;

public interface GuestService {
	String costResult (HttpServletRequest request, Model model, HttpSession session);
	String tariffs(HttpServletRequest request, HttpSession session);
	String createUser(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult);
}
