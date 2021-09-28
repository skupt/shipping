package rozaryonov.shipping.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.LogisticNetElementService;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.PropertyService;
import rozaryonov.shipping.service.TariffService;
import rozaryonov.shipping.service.impl.PathFinder;
import rozaryonov.shipping.service.impl.PersonServiceImpl;
import rozaryonov.shipping.service.impl.UserDetailsServiceImpl;
import rozaryonov.shipping.utils.WebUtils;

@Controller
@RequiredArgsConstructor
public class MainController {
	private static Logger logger = LogManager.getLogger();
	
	private final PersonService personService;
	private final LocalityService localityService;
	private final PropertyService propertyService;
	private final LogisticNetElementService logisticNetElementService;
	private final TariffService tariffService;

	
	@GetMapping({"/", "/index"})
	public String indexPage () {
		return "index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
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
		long departureId = Long.parseLong(request.getParameter("departure"));
		long arrivalId = Long.parseLong(request.getParameter("arrival"));
		int length = Integer.parseInt(request.getParameter("length"));
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		double weight = Integer.parseInt(request.getParameter("weight"));
		//String locStrStr = (String) session.getAttribute("locale");
		String locStrStr = request.getLocale().toString();
		System.out.println(locStrStr);
		String[] locPart = locStrStr.split("_");
		Locale locale = new Locale(locPart[0], locPart[1]);
		NumberFormat doubleFormat = NumberFormat.getInstance(locale);
		doubleFormat.setMaximumFractionDigits(2);
		NumberFormat intFormat = NumberFormat.getInstance();
		intFormat.setMaximumFractionDigits(0);
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("uk", "UA"));
		
		long logisticConfigId = Long.parseLong(propertyService.findById("currentLogisticConfigId").getValue());
		PathFinder pf = (PathFinder) session.getAttribute("pathfinder");
		if (pf==null) {
			session.removeAttribute("pathfinder");
			pf = new PathFinder(logisticNetElementService, logisticConfigId);
			session.setAttribute("pathfinder", pf);
		}
		long tariffId = Long.parseLong(propertyService.findById("currentTariffId").getValue());		
		try {
			String route = pf.showShortestPath(departureId, arrivalId);
			double distance = pf.calcMinDistance(departureId, arrivalId);

			double truckVelocity = tariffService.findById(tariffId).getTruckVelocity();
			double dencity = tariffService.findById(tariffId).getDensity();
			double paperwork = tariffService.findById(tariffId).getPaperwork();
			double targetReceiptDist = tariffService.findById(tariffId).getTargetedReceipt();
			double targetDeliveryDist = tariffService.findById(tariffId).getTargetedDelivery();
			double shippingRate = tariffService.findById(tariffId).getShippingRate();
			double insuranceWorth = tariffService.findById(tariffId).getInsuranceWorth();
			double insuranceRate = tariffService.findById(tariffId).getInsuranceRate();
			
			Duration duration = Duration.ofHours((long) (distance / truckVelocity + 48));
			long dur = duration.toDays();
			double volumeWeight = length * width * height / 1000 * dencity;
			double volume = length * width * height / 1000;
			double usedWeight = Double.max(weight, volumeWeight);
			double targetReceiptCost = targetReceiptDist * usedWeight * shippingRate / 100;
			double interCityCost = distance * usedWeight * shippingRate /100;
			double targetDeliveryCost = targetDeliveryDist * usedWeight * shippingRate / 100;
			double insuranceCost = usedWeight * insuranceWorth * insuranceRate;
			double total = paperwork + targetReceiptCost + interCityCost + targetDeliveryCost + insuranceCost;
			
			Locality loadLocality = localityService.findById(departureId);
			Locality unloadLocality = localityService.findById(arrivalId);
			
			model.addAttribute("loadLocality", loadLocality);
			model.addAttribute("unloadLocality", unloadLocality);
			model.addAttribute("route", route);
			model.addAttribute("distanceD", distance);
			model.addAttribute("weightD", weight);
			model.addAttribute("volumeD", volume);
			model.addAttribute("totalD", total);
			model.addAttribute("duration", intFormat.format(dur));
			model.addAttribute("weight", doubleFormat.format(weight));
			model.addAttribute("paperwork", doubleFormat.format(paperwork));
			model.addAttribute("volumeWeight", doubleFormat.format(volumeWeight));
			model.addAttribute("volume", doubleFormat.format(volume));
			model.addAttribute("usedWeight", doubleFormat.format(usedWeight));
			model.addAttribute("targetReceipt", doubleFormat.format(targetReceiptCost));
			model.addAttribute("interCityCost", doubleFormat.format(interCityCost));
			model.addAttribute("targetDelivery", doubleFormat.format(targetDeliveryCost));
			model.addAttribute("insuranceWorth", doubleFormat.format(insuranceWorth));
			model.addAttribute("insuranceRate", doubleFormat.format(insuranceRate));
			model.addAttribute("insuranceCost", doubleFormat.format(insuranceCost));
			model.addAttribute("totalMoney", currencyFormat.format(total));
			model.addAttribute("totalMoney", currencyFormat.format(total));
			model.addAttribute("shippingRate", doubleFormat.format(shippingRate));
			model.addAttribute("targetReceiptDist", doubleFormat.format(targetReceiptDist));
			model.addAttribute("targetDeliveryDist", doubleFormat.format(targetDeliveryDist));
			model.addAttribute("date", LocalDateTime.now().plusDays(dur));
		} catch (ClassNotFoundException | IOException e) {
			logger.warn(e.getMessage());
		}
		return "/delivery_cost";
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
