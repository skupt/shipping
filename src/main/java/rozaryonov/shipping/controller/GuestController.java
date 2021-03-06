package rozaryonov.shipping.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.model.Mapper;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.GuestService;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.TariffService;
import rozaryonov.shipping.utils.WebUtils;

@Controller
@RequiredArgsConstructor
public class GuestController {
	private static Logger logger = LogManager.getLogger();
	
	private final PersonService personService;
	private final LocalityService localityService;
	private final GuestService guestService;
	private final PageableFactory pageableFactory;
	private final Mapper mapper;
	private final PersonRepository personRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	
	@GetMapping({"/", "/index", "/users"})
	public String indexPage () {
		return "index";
	}
	
	@GetMapping("/tariffs")
	public String tariffs(HttpServletRequest request, HttpSession session) {
		return guestService.tariffs(request, session);
	}
	
	@GetMapping("/new")
	public String newUser (@ModelAttribute("personDto") PersonDto personDto) {
		return "/new";
	}
	
	@PostMapping({"/"})
	public String createUser(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult) {
		return guestService.createUser(personDto, bindingResult);
	}
	
//	@GetMapping("/tariffs?cmd=TariffArchiveNext")
//	public String tariffsNextPage(HttpSession session) {
//		Page<Tariff, TariffService> pageTariffArchive  = (Page<Tariff, TariffService>) session.getAttribute("pageTariffArchive");
//		List<Tariff> tariffArchiveList = pageTariffArchive.nextPage();
//		session.setAttribute("tariffArchiveList", tariffArchiveList);
//
//		return "/tariffs";
//	}
	
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		return "loginPage";
	}

	@GetMapping("/dispatch")
	public String enterCabinet(Model model, Principal principal, HttpSession session) {
			Person person = personService.findByLogin((principal.getName()));
			String page = null;
			if (person != null) {
				switch (person.getRole().getName()) {
				case "user" : 
					model.addAttribute("balance", personService.calcAndReplaceBalance(person.getId()));
					model.addAttribute("person", person);
					session.setAttribute("person", person);
					page = "redirect:/auth_user/cabinet";
					break;
				case "manager" : 
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
		System.out.println("*inside logoutPre");
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
