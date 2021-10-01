package rozaryonov.shipping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

public interface GuestService {
	public String costResult (HttpServletRequest request, Model model, HttpSession session);
	
}
