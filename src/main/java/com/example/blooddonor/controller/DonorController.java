package com.example.blooddonor.controller;

import com.example.blooddonor.dto.ApiResponse;
import com.example.blooddonor.dto.DonorRequest;
import com.example.blooddonor.model.Donor;
import com.example.blooddonor.service.DonorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles all donor-related web requests (register, search, list, update, delete).
@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
@Tag(name = "Donors", description = "Register and search for blood donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @Operation(summary = "Register a new blood donor")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Donor>> register(@Valid @RequestBody DonorRequest request) {
        Donor saved = donorService.registerDonor(request);
        return ResponseEntity.ok(ApiResponse.success("Donor registered successfully", saved));
    }

    @Operation(summary = "Search for available donors nearby, by blood group")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Donor>>> search(
            @RequestParam String bloodGroup,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radiusKm
    ) {
        List<Donor> donors = donorService.searchNearbyDonors(bloodGroup, lat, lng, radiusKm);
        String message = donors.isEmpty()
                ? "No donors found nearby"
                : donors.size() + " donor(s) found nearby";
        return ResponseEntity.ok(ApiResponse.success(message, donors));
    }

    @Operation(summary = "List all registered donors")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Donor>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("All donors fetched", donorService.getAllDonors()));
    }

    @Operation(summary = "Get a single donor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Donor>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Donor found", donorService.getDonorById(id)));
    }

    @Operation(summary = "Mark a donor as available or unavailable")
    @PatchMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<Donor>> setAvailability(
            @PathVariable String id,
            @RequestParam boolean available
    ) {
        Donor updated = donorService.setAvailability(id, available);
        String message = available ? "Donor marked as available" : "Donor marked as unavailable";
        return ResponseEntity.ok(ApiResponse.success(message, updated));
    }

    @Operation(summary = "Delete a donor record")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        donorService.deleteDonor(id);
        return ResponseEntity.ok(ApiResponse.success("Donor deleted successfully", null));
    }
}
