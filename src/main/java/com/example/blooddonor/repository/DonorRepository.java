package com.example.blooddonor.repository;

import com.example.blooddonor.model.Donor;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Spring automatically creates the database code for us, based on method names.
public interface DonorRepository extends MongoRepository<Donor, String> {

    // Finds all donors of a certain blood group, near a given point, within a distance.
    // "Near" search is a built-in MongoDB geospatial feature.
    List<Donor> findByBloodGroupAndAvailableTrueAndLocationNear(String bloodGroup, GeoJsonPoint point, Distance distance);

    // Finds all available donors near a point (any blood group)
    List<Donor> findByAvailableTrueAndLocationNear(GeoJsonPoint point, Distance distance);
}
