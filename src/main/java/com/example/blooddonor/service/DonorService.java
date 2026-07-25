package com.example.blooddonor.service;

import com.example.blooddonor.dto.DonorRequest;
import com.example.blooddonor.exception.ResourceNotFoundException;
import com.example.blooddonor.model.Donor;
import com.example.blooddonor.repository.DonorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;

// All the "business logic" for donors lives here.
// The Controller just handles web requests/responses; this class does the actual work.
@Service
public class DonorService {

    private static final Logger log = LoggerFactory.getLogger(DonorService.class);

    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor registerDonor(DonorRequest request) {
        GeoJsonPoint point = new GeoJsonPoint(request.getLongitude(), request.getLatitude());
        Donor donor = new Donor(
                request.getName().trim(),
                request.getBloodGroup().trim().toUpperCase(),
                request.getPhone().trim(),
                request.getEmail() != null ? request.getEmail().trim() : null,
                point,
                request.getCity()
        );
        Donor saved = donorRepository.save(donor);
        log.info("New donor registered: {} ({})", saved.getName(), saved.getBloodGroup());
        return saved;
    }

    public List<Donor> searchNearbyDonors(String bloodGroup, double lat, double lng, double radiusKm) {
        GeoJsonPoint point = new GeoJsonPoint(lng, lat);
        Distance distance = new Distance(radiusKm, Metrics.KILOMETERS);
        List<Donor> donors = donorRepository.findByBloodGroupAndAvailableTrueAndLocationNear(
                bloodGroup.trim().toUpperCase(), point, distance);
        log.info("Search for blood group {} within {} km found {} donor(s)", bloodGroup, radiusKm, donors.size());
        return donors;
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public Donor getDonorById(String id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
    }

    public Donor setAvailability(String id, boolean available) {
        Donor donor = getDonorById(id);
        donor.setAvailable(available);
        return donorRepository.save(donor);
    }

    public void deleteDonor(String id) {
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
        log.info("Donor deleted: {}", id);
    }
}
