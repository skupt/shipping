package rozaryonov.shipping.service.impl;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.service.GuestService;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.LogisticNetElementService;
import rozaryonov.shipping.service.PropertyService;
import rozaryonov.shipping.service.TariffService;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {
	private static Logger logger = LogManager.getLogger();

	private final PropertyService propertyService;
	private final LogisticNetElementService logisticNetElementService;
	private final TariffService tariffService;
	private final LocalityService localityService;
	
	
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
		} catch (ClassNotFoundException | IOException e) {
			logger.warn(e.getMessage());
		}
		return "/delivery_cost";
	}

}
