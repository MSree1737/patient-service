package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PatientRequestDTO {
    @NotBlank
    @Size(max =100, message = "Name can't 100 character")
    private String name;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String birthDate;

    @NotBlank(groups = CreatePatientValidationGroup.class,message = "Registration date is required")
    private String registrationDate;

    public @NotBlank @Size(max = 100, message = "Name can't 100 character") String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 100, message = "Name can't 100 character") String name) {
        this.name = name;
    }

    public @NotBlank @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") String address) {
        this.address = address;
    }

    public @NotNull @NotBlank(message = "Date of birth is required") String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NotBlank(message = "Date of birth is required") String birthDate) {
        this.birthDate = birthDate;
    }

    public  String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate( String registrationDate) {
        this.registrationDate = registrationDate;
    }


}
