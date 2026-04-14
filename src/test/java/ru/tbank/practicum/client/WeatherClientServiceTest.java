package ru.tbank.practicum.client;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestClient;
import ru.tbank.practicum.config.WeatherServiceProperties;
import ru.tbank.practicum.dto.external.DtoCoordinateRequest;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.service.WeatherClientService;

class WeatherClientServiceTest {

    private static WireMockServer wireMockServer;
    private WeatherClientService service;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();


    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(42124);
        wireMockServer.start();

        RestClient restClient =
                RestClient.builder().baseUrl(wireMockServer.baseUrl()).build();

        WeatherServiceProperties properties = new WeatherServiceProperties(
                wireMockServer.baseUrl(), "/weather", new WeatherServiceProperties.Cred("test-token"), null, null);

        service = new WeatherClientService(restClient, properties);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getWeatherByCoordinate_returnsParsedResponse() {
        wireMockServer.stubFor(get(urlPathEqualTo("/weather"))
                .withQueryParam("lat", equalTo("0.0"))
                .withQueryParam("lon", equalTo("0.0"))
                .withQueryParam("units", equalTo("metric"))
                .withQueryParam("appid", equalTo("test-token"))
                .willReturn(okJson(readResource("classpath:stubs/weather/success-response.json"))));

        DtoCoordinateRequest request = new DtoCoordinateRequest(0.0, 0.0);

        DtoWeatherResponse response = service.getWeatherByCoordinate(request);

        assertNotNull(response);

        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/weather"))
                .withQueryParam("lat", equalTo("0.0"))
                .withQueryParam("lon", equalTo("0.0"))
                .withQueryParam("units", equalTo("metric"))
                .withQueryParam("appid", equalTo("test-token")));
    }

    private String readResource(String path) {
        Resource resource = resourceLoader.getResource(path);

        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }
}
