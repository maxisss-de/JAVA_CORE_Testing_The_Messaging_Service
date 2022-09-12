package ru.netology.sender;

import java.util.Map;
import java.util.HashMap;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    public String MOSCOW_IP = "172.0.32.11";
    public String russianText = "Добро пожаловать";

    public String NEW_YORK_IP = "96.44.183.149";
    public String englishText = "Welcome";

    @BeforeAll
    public static void beforeAllMethod() {
        System.out.println("BeforAll call");
    }

    @BeforeEach
    public void beforeEachMethod() {
        System.out.println("BeforeEach call");
    }

    @AfterEach
    public void afterEachMethod() {
        System.out.println("AfterEach call");
    }

    @AfterAll
    public static void afterAllMethod() {
        System.out.println("AfterEach call");
    }

    @Test
        // Tест для проверки языка отправляемого сообщения (класс MessageSender) с использованием Mockito
    void testMessageSenderImpl_RUSSIA() {
        System.out.println("Тест №1 - проверка, что MessageSenderImpl всегда отправляет только русский текст, если ip относится к Российским адресам");
        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);

        Map<String, String> mock = new HashMap<>();
        mock.put(MessageSenderImpl.IP_ADDRESS_HEADER, MOSCOW_IP);

        Mockito.when(geoServiceImpl.byIp(MOSCOW_IP))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(localizationServiceImpl.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        String expected = russianText;
        String actual = messageSenderImpl.send(mock);
        assertEquals(expected, actual);
    }

    // Tест для проверки языка отправляемого сообщения (класс MessageSender) с использованием Mockito.
    @Test
    void testMessageSenderImpl_USA() {
        System.out.println("Тест №2 - проверка, что MessageSenderImpl всегда отправляет только английский текст, если ip относится к Американским адресам");
        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);

        Map<String, String> mock = new HashMap<>();
        mock.put(MessageSenderImpl.IP_ADDRESS_HEADER, NEW_YORK_IP);

        Mockito.when(geoServiceImpl.byIp(NEW_YORK_IP))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        Mockito.when(localizationServiceImpl.locale(Country.USA))
                .thenReturn("Welcome");

        String expected = englishText;
        String actual = messageSenderImpl.send(mock);
        assertEquals(expected, actual);
    }

    // Тест для проверки определения локации по ip (класс GeoServiceImpl)
    @Test
    void testDeterminingGeolocationByIp() {
        System.out.println("Тест №3 - проверка определения локации по ip");
        GeoServiceImpl geoServiceImpl = new GeoServiceImpl();
        Location expected = new Location("New York", Country.USA, " 10th Avenue", 32);
        Location actual = geoServiceImpl.byIp(NEW_YORK_IP);     // Проверил работу метода public Location byIp(String ip)
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getCountry(), actual.getCountry());
    }

    // Тест для проверки возвращаемого текста (класса LocalizationServiceImpl)
    @Test
    void testReturnTextChecksUSA() {
        System.out.println("Тест №4 - проверка возвращаемого текста страны США");
        LocalizationServiceImpl localizationServiceImpl = new LocalizationServiceImpl();
        String expected = englishText;
        String actual = localizationServiceImpl.locale(Country.USA);        // Проверил работу метода public String locale(Country country).
        assertEquals(expected, actual);
    }

    // Тест для проверки возвращаемого текста (класса LocalizationServiceImpl)
    @Test
    void testReturnTextChecksRUSSIA() {
        System.out.println("Тест №5 - проверка возвращаемого текста страны России");
        LocalizationServiceImpl localizationServiceImpl = new LocalizationServiceImpl();
        String expected = russianText;
        String actual = localizationServiceImpl.locale(Country.RUSSIA);     // Проверил работу метода public String locale(Country country).
        assertEquals(expected, actual);
    }
}