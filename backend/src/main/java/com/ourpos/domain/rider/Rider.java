package com.ourpos.domain.rider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.ourpos.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Rider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rider_id")
    private Long id;

    @Column(name = "rider_name")
    private String name;

    @Column(name = "rider_email")
    private String email;

    @Column(name = "rider_phone")
    private String phone;

    @Column(name = "rider_password")
    private String password;

    @Column(name = "rider_registered_number")
    private String registeredNumber;

    @Column(name = "rider_motor_license_number")
    private String motorLicenseNumber;

    @Builder
    private Rider(String name, String email, String phone, String password, String registeredNumber,
        String motorLicenseNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.registeredNumber = registeredNumber;
        this.motorLicenseNumber = motorLicenseNumber;
    }
}
