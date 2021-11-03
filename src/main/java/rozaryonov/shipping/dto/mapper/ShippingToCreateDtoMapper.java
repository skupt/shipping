package rozaryonov.shipping.dto.mapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rozaryonov.shipping.dto.ShippingToCreateDto;
import rozaryonov.shipping.exception.ShippingStatusNotFoundException;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.ShippingStatusRepository;

@Slf4j
@RequiredArgsConstructor
public class ShippingToCreateDtoMapper {
    private final ShippingStatusRepository shippingStatusRepository;

    public Shipping toShipping(ShippingToCreateDto shippingToCreateDto) {
        return Shipping.builder()
                .person(shippingToCreateDto.getPerson())
                .creationTimestamp(shippingToCreateDto.getCreationTs())
                .loadLocality(shippingToCreateDto.getLoadLocality())
                .shipper(shippingToCreateDto.getShipper())
                .downloadDatetime(shippingToCreateDto.getDownloadTs())
                .downloadAddress(shippingToCreateDto.getDownloadAddress())
                .unloadLocality(shippingToCreateDto.getUnloadLocality())
                .consignee(shippingToCreateDto.getConsignee())
                .unloadAddress(shippingToCreateDto.getUnloadAddress())
                .distance(shippingToCreateDto.getDistance())
                .weight(shippingToCreateDto.getWeight())
                .volume(shippingToCreateDto.getVolume())
                .fare(shippingToCreateDto.getFare())
                .shippingStatus(shippingStatusRepository.findById(shippingToCreateDto.getShippingStatusId())
                        .orElseThrow(() -> new ShippingStatusNotFoundException(
                                "No SippingStatus found in AuthUserService.createShipping()")))
                .build();
    }
}
