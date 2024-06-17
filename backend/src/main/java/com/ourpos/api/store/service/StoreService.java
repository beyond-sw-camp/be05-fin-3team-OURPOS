package com.ourpos.api.store.service;

import java.util.List;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.map.MapService;
import com.ourpos.api.store.Location;
import com.ourpos.api.store.dto.request.StoreRequestDto;
import com.ourpos.api.store.dto.response.StoreResponseDto;
import com.ourpos.auth.exception.LoginRequiredException;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.manager.Manager;
import com.ourpos.domain.manager.ManagerRepository;
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

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final MapService mapService;
    private final ManagerRepository managerRepository;

    @Transactional
    public void createStore(StoreRequestDto storeRequestDto) {
        log.info(storeRequestDto.toString());
        Manager manager = managerRepository.findById(storeRequestDto.getManagerId())
            .orElseThrow(() -> new IllegalArgumentException("해당 매니저가 존재하지 않습니다."));

        Location location = mapService.getLocation(storeRequestDto.getStoreAddress().getAddressBase());
        storeRequestDto.getStoreAddress().setLatitude(location.latitude());
        storeRequestDto.getStoreAddress().setLongitude(location.longitude());

        Store store = storeRequestDto.toEntity(manager);
        storeRepository.save(store);
    }

    public List<StoreResponseDto> findStoresOrderBy(Location location) {
        List<Store> stores = storeRepository.findAllWithAddress();

        return stores.stream().map(store ->
                new StoreResponseDto(store, mapService.getDuration(
                    location,
                    new Location(store.getAddress().getLatitude(), store.getAddress().getLongitude()))))
            .sorted().toList();
    }

    public List<StoreResponseDto> findStoresOrderByDeliveryDistance(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId)
            .orElseThrow(() -> new LoginRequiredException("해당 고객이 존재하지 않습니다."));

        CustomerAddress customerDefaultAddress = customer.getCustomerAddresses()
            .stream().filter(CustomerAddress::getDefaultYn)
            .findFirst().orElseThrow(() -> new IllegalArgumentException("기본 주소가 존재하지 않습니다."));

        Location location = mapService.getLocation(customerDefaultAddress.getAddressBase());

        List<Store> stores = storeRepository.findAllWithAddress();
        Double latitude = location.latitude();
        Double longitude = location.longitude();
        return stores.stream().map(store ->
            new StoreResponseDto(store, mapService.getDuration(
                new Location(latitude, longitude),
                new Location(store.getAddress().getLatitude(), store.getAddress().getLongitude())
            ))).sorted().toList();
    }
}
