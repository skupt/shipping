package rozaryonov.shipping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.OrderDataDto;
import rozaryonov.shipping.dto.ShippingToCreateDto;
import rozaryonov.shipping.dto.mapper.ShippingToCreateDtoMapper;
import rozaryonov.shipping.exception.*;
import rozaryonov.shipping.model.*;
import rozaryonov.shipping.repository.*;
import rozaryonov.shipping.utils.PathFinder;

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

import rozaryonov.shipping.AppConst;

@Slf4j
@Service
public class ShippingService {
	@Autowired
	private ShippingRepository shippingRepository;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private LogisticNetElementRepository logisticNetElementRepository;
	@Autowired
	private TariffRepository tariffRepository;
	@Autowired
	private LocalityRepository localityRepository;
	@Autowired
	private ShippingStatusRepository shippingStatusRepository;

	public void shippingCostCalculationResult(HttpServletRequest request, HttpSession session) {
		// parameters parsing
		long departureId = Long.parseLong(request.getParameter("departure"));
		long arrivalId = Long.parseLong(request.getParameter("arrival"));
		int length = Integer.parseInt(request.getParameter("length"));
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		double weight = Integer.parseInt(request.getParameter("weight"));
		String locStrStr = request.getLocale().toString();

		// path attributes calculation
		long logisticConfigId = Long.parseLong(propertyRepository.findById("currentLogisticConfigId").orElseThrow(()-> new PropertyNotFoundException("currentLogisticConfigId")).getValue());
		Iterable<LogisticNetElement> logisticNetElements = logisticNetElementRepository.findByNetConfig(logisticConfigId);
		PathFinder pathfinder = new PathFinder(logisticNetElements);
		String route;
		double distance;
		try {
			route = pathfinder.showShortestPath(departureId, arrivalId);
			distance = pathfinder.calcMinDistance(departureId, arrivalId);
		} catch (ClassNotFoundException | IOException e) {
			log.warn(e.getMessage());
			throw new GuestSerivceException(e.getMessage());
		}

		// price atributes calculation
		long tariffId = Long.parseLong(propertyRepository.findById("currentTariffId").orElseThrow(()-> new PropertyNotFoundException("currentTariffId")).getValue());
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
		long durationDays = duration.toDays();
		double volumeWeight = length * width * height / 1000 * dencity;
		double volume = length * width * height / 1000;
		double usedWeight = Double.max(weight, volumeWeight);
		double targetReceiptCost = targetReceiptDist * usedWeight * shippingRate / 100;
		double interCityCost = distance * usedWeight * shippingRate / 100;
		double targetDeliveryCost = targetDeliveryDist * usedWeight * shippingRate / 100;
		double insuranceCost = usedWeight * insuranceWorth * insuranceRate;
		double total = paperwork + targetReceiptCost + interCityCost + targetDeliveryCost + insuranceCost;


		// session numeric attributes setting
		Locality loadLocality = localityRepository.findById(departureId).orElseThrow(()-> new LocalityNotFoundException("id=" + departureId));
		Locality unloadLocality = localityRepository.findById(arrivalId).orElseThrow(()-> new LocalityNotFoundException("id=" + departureId));
		session.setAttribute("loadLocality", loadLocality);
		session.setAttribute("unloadLocality", unloadLocality);
		session.setAttribute("distanceD", distance);
		session.setAttribute("weightD", weight);
		session.setAttribute("volumeD", volume);
		session.setAttribute("totalD", total);


		// model string attributes setting
		String[] locPart = locStrStr.split("_");
		Locale locale = new Locale(locPart[0], locPart[1]);
		NumberFormat doubleFormat = NumberFormat.getInstance(locale);
		doubleFormat.setMaximumFractionDigits(2);
		NumberFormat intFormat = NumberFormat.getInstance();
		intFormat.setMaximumFractionDigits(0);
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("uk", "UA"));

		session.setAttribute("route", route);
		session.setAttribute("duration", intFormat.format(durationDays));
		session.setAttribute("weight", doubleFormat.format(weight));
		session.setAttribute("paperwork", doubleFormat.format(paperwork));
		session.setAttribute("volumeWeight", doubleFormat.format(volumeWeight) );
		session.setAttribute("volume", doubleFormat.format(volume));
		session.setAttribute("usedWeight", doubleFormat.format(usedWeight));
		session.setAttribute("targetReceipt", doubleFormat.format(targetReceiptCost));
		session.setAttribute("interCityCost", doubleFormat.format(interCityCost));
		session.setAttribute("targetDelivery", doubleFormat.format(targetDeliveryCost));
		session.setAttribute("insuranceCost", doubleFormat.format(insuranceCost));
		session.setAttribute("totalMoney", currencyFormat.format(total));
	}

	@Transactional
	public void createShipping(@ModelAttribute("orderDataDto") @Valid OrderDataDto orderDataDto, HttpSession session) {
		ShippingToCreateDto shippingToCreateDto = new ShippingToCreateDto(orderDataDto, session);
		ShippingToCreateDtoMapper createDtoMapper = new ShippingToCreateDtoMapper(shippingStatusRepository);
		Shipping shipping = createDtoMapper.toShipping(shippingToCreateDto);
		shippingRepository.save(shipping);
		setAttributesForCreateShipping(session, shipping.getShipper(), shipping.getDownloadAddress(),
				shipping.getConsignee(), shipping.getUnloadAddress());
	}

	private void setAttributesForCreateShipping(HttpSession session, String shipper, String downloadAddress, String consignee,
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
