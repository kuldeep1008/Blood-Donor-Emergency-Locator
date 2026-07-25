package com.example.blooddonor.repository;

import com.example.blooddonor.model.EmergencyRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmergencyRequestRepository extends MongoRepository<EmergencyRequest, String> {
    List<EmergencyRequest> findByStatus(String status);
}
