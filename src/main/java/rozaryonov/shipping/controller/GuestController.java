package rozaryonov.shipping.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.dto.ShippingResultComposedDto;
import rozaryonov.shipping.repository.LocalityRepository;
import rozaryonov.shipping.service.GuestService;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.ShippingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GuestController {
	private final GuestService guestService;
	private final PersonService personService;
	private final ShippingService shippingService;
	private final LocalityRepository localityRepository;


	@GetMapping("/")
	public String indexPage () {
		return "index";
	}
	
	@GetMapping("/tariffs")
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

	@GetMapping("/persons/new_person_form")
	public String getNewPersonForm(@ModelAttribute("personDto") PersonDto personDto) {
		return "/new_person_form";
	}

	@PostMapping("/persons/")
	public String createPerson(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult) {
		String page;
		if (personService.checkUserCreationForm(personDto, bindingResult).hasErrors()) {
			page = "/person/new_person_form";
		} else {
			personService.createUser(personDto);
			page = "redirect:/";
		}
		return page;
	}

	@GetMapping("/shippings/calculation_start_form")
	public String getShippingCalculationStartForm(Model model) {
		model.addAttribute("localities", localityRepository.findAll());
		return "/shippings/calculation_start_form";
	}

	@GetMapping("/shippings/calculation_result_form")
	public String getShippingCalculationResultForm(HttpServletRequest request,  HttpSession session) {
		ShippingResultComposedDto composedDto = shippingService.calculatePriceResultComposedDto(request, session);
		shippingService.setNumericAttributes(session, composedDto.getShippingResultPriceAttributesDto());
		shippingService.setFormattedAttributes(session, composedDto.getShippingResultPriceAttributesDto(),
				composedDto.getShippingResultFormParametersDto());
		return "/shippings/calculation_result_form";
	}




}
