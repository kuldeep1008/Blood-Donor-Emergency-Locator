package com.example.blooddonor.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Represents someone urgently asking for blood ("Help" feature).
@Document(collection = "emergency_requests")
public class EmergencyRequest {

    @Id
    private String id;

    private String patientName;
    private String bloodGroup;
    private String hospitalName;
    private String contactNumber;
    private GeoJsonPoint location;
    private String city;
    private String status = "ACTIVE"; // ACTIVE or RESOLVED

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    public EmergencyRequest() {
    }

    public EmergencyRequest(String patientName, String bloodGroup, String hospitalName,
                             String contactNumber, GeoJsonPoint location, String city) {
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.hospitalName = hospitalName;
        this.contactNumber = contactNumber;
        this.location = location;
        this.city = city;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
