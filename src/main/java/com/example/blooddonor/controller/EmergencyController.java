package com.example.blooddonor.controller;

import com.example.blooddonor.dto.ApiResponse;
import com.example.blooddonor.dto.EmergencyRequestDto;
import com.example.blooddonor.dto.EmergencyResponse;
import com.example.blooddonor.model.EmergencyRequest;
import com.example.blooddonor.service.EmergencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// The "Help / Need Blood Urgently" feature.
// One call both saves the emergency request AND returns matching donors instantly.
@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins = "*")
@Tag(name = "Emergency Help", description = "Submit an urgent blood request and get matching donors instantly")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @Operation(summary = "Submit an urgent blood request - returns matching nearby donors immediately")
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<EmergencyResponse>> requestHelp(@Valid @RequestBody EmergencyRequestDto dto) {
        EmergencyResponse response = emergencyService.createRequestAndFindDonors(dto);
        String message = response.getMatchingDonors().isEmpty()
                ? "Request saved, but no matching donors found nearby yet"
                : response.getMatchingDonors().size() + " matching donor(s) found nearby";
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    @Operation(summary = "List all currently active emergency requests")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<EmergencyRequest>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success("Active emergency requests", emergencyService.getActiveRequests()));
    }

    @Operation(summary = "Mark an emergency request as resolved (blood was found)")
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<EmergencyRequest>> resolve(@PathVariable String id) {
        EmergencyRequest resolved = emergencyService.resolveRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Request marked as resolved", resolved));
    }
}
