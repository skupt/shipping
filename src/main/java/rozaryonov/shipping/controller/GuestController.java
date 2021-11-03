package rozaryonov.shipping.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rozaryonov.shipping.exception.RoleNotFoundException;
import rozaryonov.shipping.service.GuestServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@Controller //todo diff with @RestController
@RequiredArgsConstructor
public class GuestController {

	private final GuestServiceImpl guestService;

	@GetMapping("/")
	public String indexPage () {
		return "index";
	}
	
	@GetMapping("/tariffs")//todo correct naming
	public String tariffs(HttpServletRequest request, HttpSession session) {
		return guestService.tariffs(request, session);
	}
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		return "loginPage";
	}

	@GetMapping("/authorized_zone_redirection")
	public String enterCabinet(Model model, Principal principal, HttpSession session) {
		return guestService.enterCabinet(model, principal, session);
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return guestService.logout(session);
	}
	
}
