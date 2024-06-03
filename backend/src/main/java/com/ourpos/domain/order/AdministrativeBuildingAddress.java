package com.ourpos.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "administrative_building_address")
public class AdministrativeBuildingAddress {

    @Id
    @Column(name = "administrative_building_address_code")
    private String code;

    @Column(name = "administrative_building_address_city")
    private String city;

    @Column(name = "administrative_building_address_town")
    private String town;

    @Column(name = "administrative_building_address_village")
    private String village;

    @Builder
    public AdministrativeBuildingAddress(String code, String city, String town, String village) {
        this.code = code;
        this.city = city;
        this.town = town;
        this.village = village;
    }
}
