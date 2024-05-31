package com.ourpos.api.store.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.ourpos.api.store.Location;
import com.ourpos.api.store.dto.request.StoreRequestDto;
import com.ourpos.api.store.dto.response.StoreResponseDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:config.properties")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    @Value("${google.api.key}")
    private String apiKey;

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void createStore(StoreRequestDto storeRequestDto) {
        log.info(storeRequestDto.toString());
        Customer customer = customerRepository.findById(storeRequestDto.getCustomerId())
            .orElseThrow(() -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));

        Location location = getLocation(storeRequestDto.getStoreAddress().getAddressBase());
        storeRequestDto.getStoreAddress().setLatitude(location.latitude());
        storeRequestDto.getStoreAddress().setLongitude(location.longitude());

        Store store = storeRequestDto.toEntity(customer);
        storeRepository.save(store);
    }

    public List<StoreResponseDto> findStoresOrderBy(Double latitude, Double longitude) {
        List<Store> stores = storeRepository.findAllWithAddress();

        return stores.stream().map(store ->
            new StoreResponseDto(store, calculateDistance(latitude, longitude, store.getAddress().getLatitude(),
                store.getAddress().getLongitude()))).sorted().toList();
    }

    public List<StoreResponseDto> findStoresOrderByDeliveryDistance(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));

        CustomerAddress customerDefaultAddress = customer.getCustomerAddresses()
            .stream().filter(CustomerAddress::getDefaultYn)
            .findFirst().orElseThrow(() -> new IllegalArgumentException("기본 주소가 존재하지 않습니다."));

        Location location = getLocation(customerDefaultAddress.getAddressBase());

        List<Store> stores = storeRepository.findAllWithAddress();
        Double latitude = location.latitude();
        Double longitude = location.longitude();
        return stores.stream().map(store ->
            new StoreResponseDto(store, calculateDistance(latitude, longitude, store.getAddress().getLatitude(),
                store.getAddress().getLongitude()))).sorted().toList();
    }

    private Location getLocation(String addressBase) {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(apiKey)
            .build();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                addressBase).await();
            if (results.length > 0) {
                double latitude = results[0].geometry.location.lat;
                double longitude = results[0].geometry.location.lng;
                log.info("위치 정보를 가져왔습니다. 위도: {}, 경도: {}", latitude, longitude);
                return new Location(latitude, longitude);
            } else {
                throw new IllegalArgumentException("위치 정보를 가져오는데 실패했습니다.");
            }
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalArgumentException("위치 정보를 가져오는데 실패했습니다.");
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radius of the earth in meters

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // distance in meters
    }
}
