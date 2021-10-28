package rozaryonov.shipping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.exception.GuestSerivceException;
import rozaryonov.shipping.exception.ShippingNotFoundException;
import rozaryonov.shipping.exception.ShippingStatusNotFoundException;
import rozaryonov.shipping.exception.TariffNotFoundException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.ShippingStatusRepository;
import rozaryonov.shipping.repository.TariffRepository;

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

import static rozaryonov.shipping.AppConst.SHIPPING_STATUS_JUST_CREATED;

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
	private TariffRepository tariffRepository;
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

	public void shippingCostCalculationResult(HttpServletRequest request, HttpSession session) {
		long departureId = Long.parseLong(request.getParameter("departure"));
		long arrivalId = Long.parseLong(request.getParameter("arrival"));
		int length = Integer.parseInt(request.getParameter("length"));
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		double weight = Integer.parseInt(request.getParameter("weight"));
		String locStrStr = request.getLocale().toString();
		String[] locPart = locStrStr.split("_");
		Locale locale = new Locale(locPart[0], locPart[1]);
		NumberFormat doubleFormat = NumberFormat.getInstance(locale);
		doubleFormat.setMaximumFractionDigits(2);
		NumberFormat intFormat = NumberFormat.getInstance();
		intFormat.setMaximumFractionDigits(0);
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("uk", "UA"));

		long logisticConfigId = Long.parseLong(propertyService.findById("currentLogisticConfigId").getValue());
		PathFinder pathfinder = new PathFinder(logisticNetElementService, logisticConfigId);

		String route;
		double distance;
		long tariffId = Long.parseLong(propertyService.findById("currentTariffId").getValue());
		try {
			route = pathfinder.showShortestPath(departureId, arrivalId);
			distance = pathfinder.calcMinDistance(departureId, arrivalId);
		} catch (ClassNotFoundException | IOException e) {
			log.warn(e.getMessage());
			throw new GuestSerivceException(e.getMessage());
		}

		Tariff tariff = tariffRepository.findById(tariffId).orElseThrow(()->
				new TariffNotFoundException("Tariff not found for id=" + tariffId));
		double truckVelocity = tariff.getTruckVelocity();
		double dencity = tariff.getDensity();
		double paperwork = tariff.getPaperwork();
		double targetReceiptDist = tariff.getTargetedReceipt();
		double targetDeliveryDist = tariff.getTargetedDelivery();
		double shippingRate = tariff.getShippingRate();
		double insuranceWorth = tariff.getInsuranceWorth();
		double insuranceRate = tariff.getInsuranceRate();

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
		session.setAttribute("pathfinder", pathfinder);
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
	}

	@Transactional
	public void createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto,
								 BindingResult bindingResult, HttpSession session) {
		Timestamp downloadTimestamp = null;
		try {
			String timestampsString = orderDataDto.getDownloadDatetime() + " 00:00:00";
			downloadTimestamp = Timestamp.valueOf(timestampsString);
		} catch (IllegalArgumentException e) {
			// do nothing we have checked it earlier in Controller
		}

		String shipper = orderDataDto.getShipper();
		String downloadAddress = orderDataDto.getDownloadAddress();
		String consignee = orderDataDto.getConsignee();
		String unloadAddress = orderDataDto.getUnloadAddress();
		double distance = (Double) session.getAttribute("distanceD");
		double weight = (Double) session.getAttribute("weightD");
		double volume = (Double) session.getAttribute("volumeD");
		BigDecimal fare = BigDecimal.valueOf((Double) session.getAttribute("totalD"));
		Timestamp creationTs = Timestamp.valueOf(LocalDateTime.now());
		Timestamp downloadTs = downloadTimestamp;

		Shipping shipping = Shipping.builder().person((Person) session.getAttribute("person"))
				.creationTimestamp(creationTs).loadLocality((Locality) session.getAttribute("loadLocality"))
				.shipper(shipper).downloadDatetime(downloadTs).downloadAddress(downloadAddress)
				.unloadLocality((Locality) session.getAttribute("unloadLocality")).consignee(consignee)
				.unloadAddress(unloadAddress).distance(distance).weight(weight).volume(
						volume)
				.fare(fare)
				.shippingStatus(shippingStatusRepository.findById(SHIPPING_STATUS_JUST_CREATED)
						.orElseThrow(() -> new ShippingStatusNotFoundException(
								"No SippingStatus found in AuthUserService.createShipping()")))
				.build();

		shippingRepository.save(shipping);
		setAttributes(session, shipper, downloadAddress, consignee, unloadAddress);
	}

	private void setAttributes(HttpSession session, String shipper, String downloadAddress, String consignee,
							   String unloadAddress) {
		session.setAttribute("shipper", shipper);
		session.setAttribute("downloadAddress", downloadAddress);
		session.setAttribute("consignee", consignee);
		session.setAttribute("unloadAddress", unloadAddress);

	}


	public BindingResult checkShippingCreationForm (OrderDataDto orderDataDto, BindingResult bindingResult) {
		Timestamp downloadTimestamp = null;
		try {
			String timestampsString = orderDataDto.getDownloadDatetime() + " 00:00:00";
			downloadTimestamp = Timestamp.valueOf(timestampsString);
		} catch (IllegalArgumentException e) {
			// do nothing
		}
		if (downloadTimestamp == null) {
			bindingResult.addError(new FieldError("orderDataDto", "downloadDatetime", "Wrong date."));

		}
		return bindingResult;
	}







	public String newShipping(HttpSession session, @ModelAttribute("orderDataDto") OrderDataDto orderDataDto) {
		Person person = (Person) session.getAttribute("person");
		if (person == null)
			return "redirect:/new";
		return "/auth_user/shippings_new";
	}


}
