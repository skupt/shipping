package rozaryonov.shipping.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.GuestService;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.utils.WebUtils;

@Controller //todo diff with @RestController
@RequiredArgsConstructor //todo base endpoint name like '.../shipping...'
public class GuestController {
	private static Logger logger = LogManager.getLogger();//todo @slf4j
	
	private final PersonService personService;
	private final LocalityService localityService;
	private final GuestService guestService;
	//todo clean code! remove extra fields //use spring here
	
	
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
			Person person = personService.findByLogin((principal.getName()));//todo here we get Optional
			String page = null;//todo don't left gray code
			if (person != null) {//todo use Optional
				switch (person.getRole().getName()) {
				case "ROLE_USER" : 
					model.addAttribute("balance", personService.calcAndReplaceBalance(person.getId()));
					model.addAttribute("person", person);
					session.setAttribute("person", person);
					page = "redirect:/auth_user/cabinet";
					break;
				case "ROLE_MANAGER" : 
					model.addAttribute("person", person);
					session.setAttribute("person", person);
					page = "redirect:/manager/cabinet";
					break;
				default: 
					page = "/index";
					break;
				}
			} else {
				page = "/index";
			}
			return page;
		}
	
	@GetMapping("/costs")
	public String costForm (Model model) {
		model.addAttribute("localities", localityService.findAll());
		
		return "costs";
	}
	
	@GetMapping("/delivery_cost")
	public String costResult (HttpServletRequest request, Model model, HttpSession session) {
		return guestService.costResult(request, model, session);
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginedUser);
			model.addAttribute("userInfo", userInfo);
			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);
		}

		return "/error/403";
	}

}
