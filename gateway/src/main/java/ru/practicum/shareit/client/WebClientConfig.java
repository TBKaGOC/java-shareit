package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.user.UserClient;

@Configuration
public class WebClientConfig {
    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public BookingClient bookingClient(RestTemplateBuilder builder) {
        return new BookingClient(builder.uriTemplateHandler(
                    new DefaultUriBuilderFactory(serverUrl + "/bookings")
                )
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    @Bean
    public ItemClient itemClient(RestTemplateBuilder builder) {
        return new ItemClient(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl + "/items")
                )
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    @Bean
    public UserClient userClient(RestTemplateBuilder builder) {
        return new UserClient(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl + "/users")
                )
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    @Bean
    public RequestClient requestClient(RestTemplateBuilder builder) {
        return new RequestClient(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl + "/requests")
                )
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }
}
