package rozaryonov.shipping.dto;

import lombok.Data;
import rozaryonov.shipping.exception.LocalityNotFoundException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.LocalityRepository;

import java.time.Duration;

@Data
public class ShippingResultPriceAttributesDto {
    private double distance;
    private long durationDays;
    private double volumeWeight;
    private double volume;
    private double usedWeight;
    private double targetReceiptCost;
    private double interCityCost;
    private double targetDeliveryCost;
    private double insuranceCost;
    private double total;
    private Locality loadLocality;
    private Locality unloadLocality;
    private String route;
    private double paperwork;

    public ShippingResultPriceAttributesDto(Tariff tariff, ShippingResultFormParametersDto formDto, double distance,
                                            LocalityRepository localityRepository, String route) {
        double truckVelocity = tariff.getTruckVelocity();
        double dencity = tariff.getDensity();
        double paperwork = tariff.getPaperwork();
        double targetReceiptDist = tariff.getTargetedReceipt();
        double targetDeliveryDist = tariff.getTargetedDelivery();
        double shippingRate = tariff.getShippingRate();
        double insuranceWorth = tariff.getInsuranceWorth();
        double insuranceRate = tariff.getInsuranceRate();

        this.distance = distance;
        Duration duration = Duration.ofHours((long) (distance / truckVelocity + 48));
        durationDays = duration.toDays();
        volumeWeight = formDto.getLength() * formDto.getWidth() * formDto.getHeight() / 1000 * dencity;
        volume = formDto.getLength() * formDto.getWidth() * formDto.getHeight() / 1000;
        usedWeight = Double.max(formDto.getWeight(), volumeWeight);
        targetReceiptCost = targetReceiptDist * usedWeight * shippingRate / 100;
        interCityCost = distance * usedWeight * shippingRate / 100;
        targetDeliveryCost = targetDeliveryDist * usedWeight * shippingRate / 100;
        insuranceCost = usedWeight * insuranceWorth * insuranceRate;
        total = paperwork + targetReceiptCost + interCityCost + targetDeliveryCost + insuranceCost;
        loadLocality = localityRepository.findById(formDto.getDepartureId())
                .orElseThrow(() -> new LocalityNotFoundException("id=" + formDto.getDepartureId()));
        unloadLocality = localityRepository.findById(formDto.getArrivalId())
                .orElseThrow(() -> new LocalityNotFoundException("id=" + formDto.getArrivalId()));
        this.route = route;
        this.paperwork = paperwork;
    }
}
