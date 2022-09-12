package ru.netology.sender;

import java.util.Map;
import java.util.Objects;

import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

public class MessageSenderImpl implements MessageSender {

    public static final String IP_ADDRESS_HEADER = "x-real-ip";
    private final GeoService geoService;

    private final LocalizationService localizationService;

    public MessageSenderImpl(GeoService geoService, LocalizationService localizationService) {
        this.geoService = geoService;
        this.localizationService = localizationService;
    }


    public String send(Map<String, String> headers) {
        String ipAddress = String.valueOf(headers.get(IP_ADDRESS_HEADER));
        if (ipAddress != null && !ipAddress.isEmpty()) {
            Location location = geoService.byIp(ipAddress);
            System.out.printf("Отправлено сообщение: %s", localizationService.locale(location.getCountry()));
            return localizationService.locale(location.getCountry());
        }
        return localizationService.locale(Country.USA);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageSenderImpl)) return false;
        MessageSenderImpl that = (MessageSenderImpl) o;
        return Objects.equals(geoService, that.geoService) && Objects.equals(localizationService, that.localizationService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geoService, localizationService);
    }

    @Override
    public String toString() {
        return "MessageSenderImpl{" +
                "geoService=" + geoService +
                ", localizationService=" + localizationService +
                '}';
    }

}