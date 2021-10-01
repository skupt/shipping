package rozaryonov.shipping.controller;

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
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.GuestService;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.RoleService;
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
	private final RoleService roleService;
	private final Mapper mapper;
	private final PersonRepository personRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	
	@GetMapping({"/", "/index", "/users"})
	public String indexPage () {
		return "index";
	}
	
	@GetMapping("/tariffs")
	public String tariffs(HttpServletRequest request, HttpSession session) {
		Page<Tariff, TariffService> pageTariffArchive = null;
		List<Tariff> tariffArchiveList = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "TariffArchivePrev":
				pageTariffArchive = (Page<Tariff, TariffService>) session
						.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.prevPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveNext":
				pageTariffArchive = (Page<Tariff, TariffService>) session
						.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.nextPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveApply":
				String sort = request.getParameter("sorting");
				int filter = Integer.parseInt(request.getParameter("logConf"));
				// comparator creation
				Comparator<Tariff> c = null;
				switch (sort) {
				case "incr" : c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp()); break;
				case "decr" : c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp()).reversed(); break;
				default : c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp()); break;
				}
				//Predicetecreation
				Predicate<Tariff> p = (Tariff t)-> t.getLogisticConfig().getId()==filter;
				pageTariffArchive = pageableFactory.getPageableForTariffArchive(6, c, p);
				session.setAttribute("pageTariffArchive", pageTariffArchive);
				tariffArchiveList = pageTariffArchive.nextPage(); 
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			}
		} else {
			pageTariffArchive = pageableFactory.getPageableForTariffArchive(6, null, null);
			session.setAttribute("pageTariffArchive", pageTariffArchive);
			tariffArchiveList = pageTariffArchive.nextPage();
			session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
			session.setAttribute("tariffArchiveList", tariffArchiveList);
		}

		return "/tariffs";
	}
	
	@GetMapping("/new")
	public String newUser (@ModelAttribute("personDto") PersonDto personDto) {
//		person = new PersonDto();
//		model.addAttribute("person", person);
		return "/new";
	}
	
	@PostMapping({"/"})
	public String createUser(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult) {
			if (bindingResult.hasErrors()) return "/new";
			FieldError fe = new FieldError("personDto", "login", "Please, choose other login.");
			if (personService.findByLogin(personDto.getLogin()) != null) {
				bindingResult.addError(new FieldError("personDto", "login", "Please, choose other login."));
				return "/new";
			}
			// Everything is Ok with user, save him
			personDto.setRoleId(2l);
			String passEncoded = passwordEncoder.encode(personDto.getPassword());
			personDto.setPassword(passEncoded);
			Person person = mapper.toPerson(personDto);
			personService.save(person);
		return "redirect:/";
	}
	
	@GetMapping("/tariffs?cmd=TariffArchiveNext")
	public String tariffsNextPage(HttpSession session) {
		Page<Tariff, TariffService> pageTariffArchive  = (Page<Tariff, TariffService>) session.getAttribute("pageTariffArchive");
		List<Tariff> tariffArchiveList = pageTariffArchive.nextPage();
		session.setAttribute("tariffArchiveList", tariffArchiveList);

		return "/tariffs";
	}
	
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		return "loginPage";
	}

	@GetMapping("/cabinet")
	public String enterCabinet(Model model, Principal principal, HttpSession session) {
			Person person = personService.findByLogin((principal.getName()));
			String page = null;
			if (person != null) {
				switch (person.getRole().getName()) {
				case "user" : 
					model.addAttribute("balance", personService.calcAndReplaceBalance(person.getId()));
					model.addAttribute("person", person);
					session.setAttribute("person", person);
					page = "/auth_user/cabinet";
					break;
				case "manager" : 
					model.addAttribute("person", person);
					session.setAttribute("person", person);
					page = "/manager/cabinet";
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
	
	
	@RequestMapping(value = { "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "welcomePage";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}

	
	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {
		// After user login successfully.
		String userName = principal.getName();
		System.out.println("User Name: " + userName);
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "userInfoPage";
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

		return "403Page";
	}

}
