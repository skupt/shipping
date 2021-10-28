package rozaryonov.shipping.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.exception.GuestSerivceException;
import rozaryonov.shipping.exception.ShippingNotFoundException;
import rozaryonov.shipping.exception.ShippingStatusNotFoundException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.ShippingStatusRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Service
public class ShippingServiceImpl{
	@Autowired
	private ShippingRepository shippingRepository;
	@Autowired
	private PropertyServiceImpl propertyService;
	@Autowired
	private LogisticNetElementServiceImpl logisticNetElementService;
	@Autowired
	private TariffServiceImpl tariffService;
	@Autowired
	private LocalityServiceImpl localityService;
	@Autowired
	private ShippingStatusRepository shippingStatusRepository;



	public Shipping findById(Long id) {
		return shippingRepository.findById(id).orElseThrow(()-> new ShippingNotFoundException("No Shipping with id:" + id));
	}

	public Iterable<Shipping> findAll() {
		return shippingRepository.findAll();
	}//todo is it okay return Iterable??? Vitaly: "Every lector on summer course said if you could use super type or interface you should have used it".

	public String shippingCostCalculationResult(HttpServletRequest request, Model model, HttpSession session) {
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

	@Transactional
	public String createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto,
								 BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors())
			return "/auth_user/shippings_new";
		Timestamp downloadTs0 = null;
		try {
			String tsStr = orderDataDto.getDownloadDatetime() + " 00:00:00";
			downloadTs0 = Timestamp.valueOf(tsStr);
		} catch (IllegalArgumentException e) {
			// do nothing
		}
		if (downloadTs0 == null) {
			bindingResult.addError(new FieldError("orderDataDto", "downloadDatetime", "Wrong date."));
			return "/auth_user/shippings_new";
		}

		String shipper = orderDataDto.getShipper();
		String downloadAddress = orderDataDto.getDownloadAddress();
		String consignee = orderDataDto.getConsignee();
		String unloadAddress = orderDataDto.getUnloadAddress();
		double distance = (Double) session.getAttribute("distanceD");
		double weight = (Double) session.getAttribute("weightD");
		double volume = (Double) session.getAttribute("volumeD");
		BigDecimal fare = BigDecimal.valueOf((Double) session.getAttribute("totalD"));
		Long shippingStatusId = 1L;

		session.setAttribute("shipper", shipper);
		session.setAttribute("downloadAddress", downloadAddress);
		session.setAttribute("consignee", consignee);
		session.setAttribute("unloadAddress", unloadAddress);

		Timestamp creationTs = Timestamp.valueOf(LocalDateTime.now());
		Timestamp downloadTs = downloadTs0;

		Shipping shipping = Shipping.builder().person((Person) session.getAttribute("person"))
				.creationTimestamp(creationTs).loadLocality((Locality) session.getAttribute("loadLocality"))
				.shipper(shipper).downloadDatetime(downloadTs).downloadAddress(downloadAddress)
				.unloadLocality((Locality) session.getAttribute("unloadLocality")).consignee(consignee)
				.unloadAddress(unloadAddress).distance(distance).weight(weight).volume(
						volume)
				.fare(fare)
				.shippingStatus(shippingStatusRepository.findById(shippingStatusId)
						.orElseThrow(() -> new ShippingStatusNotFoundException(
								"No SippingStatus found in AuthUserService.createShipping()")))
				.build();

		shippingRepository.save(shipping);
		return "redirect:/auth_user/cabinet";

	}

	public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
		Person person = (Person) session.getAttribute("person");
		if (person == null)
			return "redirect:/new";
		return "/auth_user/shippings_new";
	}


}
