package com.example.blooddonor.service;

import com.example.blooddonor.dto.EmergencyRequestDto;
import com.example.blooddonor.dto.EmergencyResponse;
import com.example.blooddonor.exception.ResourceNotFoundException;
import com.example.blooddonor.model.Donor;
import com.example.blooddonor.model.EmergencyRequest;
import com.example.blooddonor.repository.EmergencyRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;

// Handles the "Need Blood Urgently" (Help) feature:
// saves the request AND immediately finds matching donors nearby, in one step.
@Service
public class EmergencyService {

    private static final Logger log = LoggerFactory.getLogger(EmergencyService.class);

    private final EmergencyRequestRepository emergencyRequestRepository;
    private final DonorService donorService;
    private final EmailService emailService;

    public EmergencyService(EmergencyRequestRepository emergencyRequestRepository, DonorService donorService,
                             EmailService emailService) {
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.donorService = donorService;
        this.emailService = emailService;
    }

    public EmergencyResponse createRequestAndFindDonors(EmergencyRequestDto dto) {
        GeoJsonPoint point = new GeoJsonPoint(dto.getLongitude(), dto.getLatitude());

        EmergencyRequest request = new EmergencyRequest(
                dto.getPatientName().trim(),
                dto.getBloodGroup().trim().toUpperCase(),
                dto.getHospitalName(),
                dto.getContactNumber().trim(),
                point,
                dto.getCity()
        );
        EmergencyRequest saved = emergencyRequestRepository.save(request);
        log.warn("EMERGENCY REQUEST created: {} needs {} near {}", saved.getPatientName(), saved.getBloodGroup(), saved.getCity());

        List<Donor> matchingDonors = donorService.searchNearbyDonors(
                dto.getBloodGroup(), dto.getLatitude(), dto.getLongitude(), dto.getRadiusKm());

        // Notify every matching donor by email (donors without an email on file are skipped)
        for (Donor donor : matchingDonors) {
            emailService.sendAlert(
                    donor.getEmail(),
                    donor.getName(),
                    saved.getPatientName(),
                    saved.getBloodGroup(),
                    saved.getHospitalName(),
                    saved.getContactNumber()
            );
        }

        return new EmergencyResponse(saved, matchingDonors);
    }

    public List<EmergencyRequest> getActiveRequests() {
        return emergencyRequestRepository.findByStatus("ACTIVE");
    }

    public EmergencyRequest resolveRequest(String id) {
        EmergencyRequest request = emergencyRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency request not found: " + id));
        request.setStatus("RESOLVED");
        return emergencyRequestRepository.save(request);
    }
}
