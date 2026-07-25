package com.example.blooddonor.dto;

import com.example.blooddonor.model.Donor;
import com.example.blooddonor.model.EmergencyRequest;

import java.util.List;

// Returned right after someone submits an emergency request:
// the saved request PLUS the list of matching donors found nearby, in one go.
public class EmergencyResponse {

    private EmergencyRequest request;
    private List<Donor> matchingDonors;

    public EmergencyResponse(EmergencyRequest request, List<Donor> matchingDonors) {
        this.request = request;
        this.matchingDonors = matchingDonors;
    }

    public EmergencyRequest getRequest() {
        return request;
    }

    public void setRequest(EmergencyRequest request) {
        this.request = request;
    }

    public List<Donor> getMatchingDonors() {
        return matchingDonors;
    }

    public void setMatchingDonors(List<Donor> matchingDonors) {
        this.matchingDonors = matchingDonors;
    }
}
