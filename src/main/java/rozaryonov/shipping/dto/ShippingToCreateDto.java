package rozaryonov.shipping.dto;

import lombok.Data;
import rozaryonov.shipping.AppConst;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ShippingToCreateDto {
    private String shipper;
    private String downloadAddress;
    private String consignee;
    private String unloadAddress;
    private Timestamp downloadTs;
    private double distance;
    private double weight;
    private double volume;
    private BigDecimal fare;
    private Timestamp creationTs;

    private Locality loadLocality;
    private Locality unloadLocality;
    private Long shippingStatusId;
    private Person person;



    public ShippingToCreateDto(OrderDataDto orderDataDto, HttpSession session) {
        shipper = orderDataDto.getShipper();
        downloadAddress = orderDataDto.getDownloadAddress();
        consignee = orderDataDto.getConsignee();
        unloadAddress = orderDataDto.getUnloadAddress();
        downloadTs = Timestamp.valueOf(orderDataDto.getDownloadDatetime() + " 00:00:00");

        distance = (Double) session.getAttribute("distanceD");
        weight = (Double) session.getAttribute("weightD");
        volume = (Double) session.getAttribute("volumeD");
        fare = BigDecimal.valueOf((Double) session.getAttribute("totalD"));
        creationTs = Timestamp.valueOf(LocalDateTime.now());

        person  = ((Person) session.getAttribute("person"));
        shippingStatusId = AppConst.SHIPPING_STATUS_JUST_CREATED;
        loadLocality = (Locality) session.getAttribute("loadLocality");
        unloadLocality = (Locality) session.getAttribute("unloadLocality");
    }
}
