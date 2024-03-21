package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {

    private LocalDate birthDate;
    private boolean privacyPolicyAgreed;
    private boolean locationBasedServicesAgreed;
    private boolean dataPolicyAgreed;


}
