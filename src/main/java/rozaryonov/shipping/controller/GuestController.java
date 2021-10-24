package rozaryonov.shipping.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.exception.RoleNotFoundException;
import rozaryonov.shipping.service.GuestService;

@Controller //todo diff with @RestController
@RequiredArgsConstructor
public class GuestController {
	private static Logger logger = LogManager.getLogger();//todo @slf4j
	
	private final GuestService guestService;
	
	@GetMapping({"/", "/index", "/users"})//todo extract to config
	public String indexPage () {
		return "index";
	}
	
	@GetMapping("/tariffs")//todo correct naming
	public String tariffs(HttpServletRequest request, HttpSession session) {
		return guestService.tariffs(request, session);
	}
	
	@GetMapping("/new")//todo correct naming; READ REST best practices and Richardson Maturity Model! Cannot be 'new' for GetMapping;
	public String newUser (@ModelAttribute("personDto") PersonDto personDto) {
		return "/new";
	}//todo why we need parameter? what?????
	
	@PostMapping({"/"})
	public String createUser(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult) {
		return guestService.createUser(personDto, bindingResult);
	}
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		return "loginPage";
	}

	@GetMapping("/dispatch")//todo what meaning of it??? rename
	public String enterCabinet(Model model, Principal principal, HttpSession session) {
		return guestService.enterCabinet(model, principal, session);
	}
	
	@GetMapping("/costs")
	public String costForm (Model model) {
		return guestService.costForm(model);
	}
	
	@GetMapping("/delivery_cost")
	public String costResult (HttpServletRequest request, Model model, HttpSession session) {
		return guestService.costResult(request, model, session);
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return guestService.logout(session);
	}
	

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {
		return guestService.accessDenied(model, principal);
	}
	
	@GetMapping("/exception")
	public String exception() {
		throw new RoleNotFoundException("RoleNotFoundException in GuestController");
	}

}
