package rozaryonov.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import rozaryonov.shipping.exception.GuestSerivceException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.*;
import rozaryonov.shipping.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

	private final PropertyService propertyService;
	private final LogisticNetElementService logisticNetElementService;
	private final TariffService tariffService;
	private final LocalityService localityService;
	private final PageableFactory pageableFactory;
	private final PersonService personService;

	@Override
	public String costResult(HttpServletRequest request, Model model, HttpSession session) {
		long departureId = Long.parseLong(request.getParameter("departure"));
		long arrivalId = Long.parseLong(request.getParameter("arrival"));
		int length = Integer.parseInt(request.getParameter("length"));
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		double weight = Integer.parseInt(request.getParameter("weight"));
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
		if (pf == null) {
			session.removeAttribute("pathfinder");
			pf = new PathFinder(logisticNetElementService, logisticConfigId);
			session.setAttribute("pathfinder", pf);
		}
		
		String route;
		double distance;
		long tariffId = Long.parseLong(propertyService.findById("currentTariffId").getValue());
		try {
			route = pf.showShortestPath(departureId, arrivalId);
			distance = pf.calcMinDistance(departureId, arrivalId);
		} catch (ClassNotFoundException | IOException e) {
			log.warn(e.getMessage());
			throw new GuestSerivceException(e.getMessage());
		}

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
		double interCityCost = distance * usedWeight * shippingRate / 100;
		double targetDeliveryCost = targetDeliveryDist * usedWeight * shippingRate / 100;
		double insuranceCost = usedWeight * insuranceWorth * insuranceRate;
		double total = paperwork + targetReceiptCost + interCityCost + targetDeliveryCost + insuranceCost;

		Locality loadLocality = localityService.findById(departureId);
		Locality unloadLocality = localityService.findById(arrivalId);

		session.setAttribute("loadLocality", loadLocality);
		session.setAttribute("unloadLocality", unloadLocality);
		session.setAttribute("route", route);
		session.setAttribute("distanceD", distance);
		session.setAttribute("weightD", weight);
		session.setAttribute("volumeD", volume);
		session.setAttribute("totalD", total);
		session.setAttribute("duration", intFormat.format(dur));
		session.setAttribute("weight", doubleFormat.format(weight));
		session.setAttribute("paperwork", doubleFormat.format(paperwork));
		session.setAttribute("volumeWeight", doubleFormat.format(volumeWeight));
		session.setAttribute("volume", doubleFormat.format(volume));
		session.setAttribute("usedWeight", doubleFormat.format(usedWeight));
		session.setAttribute("targetReceipt", doubleFormat.format(targetReceiptCost));
		session.setAttribute("interCityCost", doubleFormat.format(interCityCost));
		session.setAttribute("targetDelivery", doubleFormat.format(targetDeliveryCost));
		session.setAttribute("insuranceWorth", doubleFormat.format(insuranceWorth));
		session.setAttribute("insuranceRate", doubleFormat.format(insuranceRate));
		session.setAttribute("insuranceCost", doubleFormat.format(insuranceCost));
		session.setAttribute("totalMoney", currencyFormat.format(total));
		session.setAttribute("totalMoney", currencyFormat.format(total));
		session.setAttribute("shippingRate", doubleFormat.format(shippingRate));
		session.setAttribute("targetReceiptDist", doubleFormat.format(targetReceiptDist));
		session.setAttribute("targetDeliveryDist", doubleFormat.format(targetDeliveryDist));
		session.setAttribute("date", LocalDateTime.now().plusDays(dur));
		return "/delivery_cost";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tariffs(HttpServletRequest request, HttpSession session) {
		Page<Tariff, TariffService> pageTariffArchive;
		List<Tariff> tariffArchiveList;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "TariffArchivePrev":
				pageTariffArchive = (Page<Tariff, TariffService>) session.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.prevPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveNext":
				pageTariffArchive = (Page<Tariff, TariffService>) session.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.nextPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveApply":
				String sort = request.getParameter("sorting");
				int filter = Integer.parseInt(request.getParameter("logConf"));
				// comparator creation
				Comparator<Tariff> c;
				switch (sort) {
				case "incr":
					c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp());
					break;
				case "decr":
					c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp()).reversed();
					break;
				default:
					c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp());
					break;
				}
				// Predicate creation
				Predicate<Tariff> p = (Tariff t) -> t.getLogisticConfig().getId() == filter;
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

	@Override
	public String enterCabinet(Model model, Principal principal, HttpSession session) {
		Person person = personService.findByLogin((principal.getName()));// todo here we get Optional
		String page;// todo don't left gray code
		if (person != null) {// todo use Optional
			switch (person.getRole().getName()) {
			case "ROLE_USER":
				model.addAttribute("balance", personService.calcAndReplaceBalance(person.getId()));
				model.addAttribute("person", person);
				session.setAttribute("person", person);
				page = "redirect:/auth_user/cabinet";
				break;
			case "ROLE_MANAGER":
				model.addAttribute("person", person);
				session.setAttribute("person", person);
				page = "redirect:/manager/cabinet";
				break;
			default:
				page = "/";
				break;
			}
		} else {
			page = "/";
		}
		return page;
	}

	@Override
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

	@Override
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@Override
	public String costForm(Model model) {
		model.addAttribute("localities", localityService.findAll());

		return "costs";
	}

}
