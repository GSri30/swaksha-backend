package com.swaksha.gatewayservice.request;

import com.swaksha.gatewayservice.entity.Doctor;
import com.swaksha.gatewayservice.entity.Hospital;
import com.swaksha.gatewayservice.entity.Patient;
import com.swaksha.gatewayservice.repository.DoctorRepo;
import com.swaksha.gatewayservice.repository.HospitalRepo;
import com.swaksha.gatewayservice.repository.PatientRepo;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Component
@RequiredArgsConstructor
public class RequestService {

    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    private final HospitalRepo hospitalRepo;

    public boolean validateSSID(String SSID) {
        boolean validity = false;
        ArrayList<Patient> p_arr = (ArrayList<Patient>) this.patientRepo.findBySsid(SSID);
        if (p_arr.size()>0 && p_arr.get(0).getSsid() == SSID){
            return true;
        }

        ArrayList<Doctor> d_arr = (ArrayList<Doctor>) this.doctorRepo.findBySsid(SSID);
        if (d_arr.size()>0 && d_arr.get(0).getSsid() == SSID){
            return true;
        }

        ArrayList<Hospital> h_arr = (ArrayList<Hospital>) this.hospitalRepo.findBySsid(SSID);
        if (h_arr.size()>0 && h_arr.get(0).getSsid() == SSID){
            return true;
        }

        return validity;
    }

    public boolean checkToApproveConsentFields(RequestController.ConsentObj consentObj) {
        boolean validity = true;

        if(consentObj.selfConsent() != true){
            validity = validateSSID(consentObj.doctorSSID()) && validateSSID(consentObj.hiuSSID());
        }
        validity = validity && validateSSID(consentObj.patientSSID()) && validateSSID(consentObj.hipSSID());
        if(!validity){
            return false;
        }

        if(consentObj.consentEndDate() == null || consentObj.requestInitiatedDate() == null)
            return false;

        if(consentObj.requestInitiatedDate().isAfter(consentObj.consentEndDate()))
            validity = false;

        return validity;
    }
}
