package com.swaksha.consentmanagerservice.patient;

import com.swaksha.consentmanagerservice.entity.PatientAuth;
import com.swaksha.consentmanagerservice.repository.PatientAuthRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Component
@RequiredArgsConstructor
public class PatientService {

    @Autowired
    private final PatientAuthRepo patientAuthRepo;

    @Autowired
    public boolean verifyPin(String patientSSID, String encPin) {
//        String originalPin = this.patientAuthRepo.getPatientPin(patientSSID);
        ArrayList<PatientAuth> p = (ArrayList<PatientAuth>) this.patientAuthRepo.findBySsid(patientSSID);

        if (p.size()<1){
            return false;
        }

        else if (p.get(0).getAuthPin() == encPin){
            return true;
        }

        return false;
    }

    @Autowired
    public boolean register(String ssid, String phoneNum, String encPin) {
        PatientAuth patientAuth = this.patientAuthRepo.save(new PatientAuth(ssid, phoneNum, encPin));
        if (patientAuth == null){
            return false;
        }
        return true;
    }

    @Autowired
    public boolean updatePin(String ssid, String newPin) {
        int update = this.patientAuthRepo.updatePin(ssid, newPin);
        return true;
    }
}
