package rozaryonov.shipping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.*;
import rozaryonov.shipping.dto.mapper.ShippingToCreateDtoMapper;
import rozaryonov.shipping.exception.*;
import rozaryonov.shipping.model.*;
import rozaryonov.shipping.repository.*;
import rozaryonov.shipping.utils.PathFinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;

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

    public ShippingResultComposedDto calculatePriceResultComposedDto(HttpServletRequest request) {
        ShippingResultFormParametersDto shippingResultFormParametersDto = new ShippingResultFormParametersDto(request);
        long logisticConfigId = Long.parseLong(propertyRepository.findById("currentLogisticConfigId").orElseThrow(() -> new PropertyNotFoundException("currentLogisticConfigId")).getValue());
        Iterable<LogisticNetElement> logisticNetElements = logisticNetElementRepository.findByNetConfig(logisticConfigId);
        PathFinder pathfinder = new PathFinder(logisticNetElements);
        String route;
        double distance;
        try {
            route = pathfinder.showShortestPath(shippingResultFormParametersDto.getDepartureId(), shippingResultFormParametersDto.getArrivalId());
            distance = pathfinder.calcMinDistance(shippingResultFormParametersDto.getDepartureId(), shippingResultFormParametersDto.getArrivalId());
        } catch (ClassNotFoundException | IOException e) {
            log.warn(e.getMessage());
            throw new PathCalculationException(e.getMessage());
        }
        long tariffId = Long.parseLong(propertyRepository.findById("currentTariffId").orElseThrow(() -> new PropertyNotFoundException("currentTariffId")).getValue());
        Tariff tariff = tariffRepository.findById(tariffId).orElseThrow(() ->
                new TariffNotFoundException("Tariff not found for id=" + tariffId));
        ShippingResultPriceAttributesDto shippingResultPriceAttributesDto = new ShippingResultPriceAttributesDto(tariff,
                shippingResultFormParametersDto, distance, localityRepository, route);

        return new ShippingResultComposedDto(shippingResultFormParametersDto, shippingResultPriceAttributesDto);
    }
    public void setNumericAttributes(HttpSession session, ShippingResultPriceAttributesDto priceDto) {
        session.setAttribute("loadLocality", priceDto.getLoadLocality());
        session.setAttribute("unloadLocality", priceDto.getUnloadLocality());
        session.setAttribute("distanceD", priceDto.getDistance());
        session.setAttribute("weightD", priceDto.getUsedWeight());
        session.setAttribute("volumeD", priceDto.getVolume());
        session.setAttribute("totalD", priceDto.getTotal());
    }
    public void setFormattedAttributes (HttpSession session, ShippingResultPriceAttributesDto priceDto,
                                        ShippingResultFormParametersDto formDto) {
        String[] locPart = formDto.getLocStrStr().split("_");
        Locale locale;
        if(locPart.length == 2) locale = new Locale(locPart[0], locPart[1]); else locale = new Locale(locPart[0]);
        NumberFormat doubleFormat = NumberFormat.getInstance(locale);
        doubleFormat.setMaximumFractionDigits(2);
        NumberFormat intFormat = NumberFormat.getInstance();
        intFormat.setMaximumFractionDigits(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("uk", "UA"));
        session.setAttribute("route", priceDto.getRoute());
        session.setAttribute("duration", intFormat.format(priceDto.getDurationDays()));
        session.setAttribute("weight", doubleFormat.format(priceDto.getUsedWeight()));
        session.setAttribute("paperwork", doubleFormat.format(priceDto.getPaperwork()));
        session.setAttribute("volumeWeight", doubleFormat.format(priceDto.getVolumeWeight()));
        session.setAttribute("volume", doubleFormat.format(priceDto.getVolume()));
        session.setAttribute("usedWeight", doubleFormat.format(priceDto.getUsedWeight()));
        session.setAttribute("targetReceipt", doubleFormat.format(priceDto.getTargetReceiptCost()));
        session.setAttribute("interCityCost", doubleFormat.format(priceDto.getInterCityCost()));
        session.setAttribute("targetDelivery", doubleFormat.format(priceDto.getTargetDeliveryCost()));
        session.setAttribute("insuranceCost", doubleFormat.format(priceDto.getInsuranceCost()));
        session.setAttribute("totalMoney", currencyFormat.format(priceDto.getTotal()));
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


    public BindingResult checkShippingCreationForm(OrderDataDto orderDataDto, BindingResult bindingResult) {
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
