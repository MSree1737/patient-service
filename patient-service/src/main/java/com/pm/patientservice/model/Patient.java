package com.pm.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String address;

    @Column(name = "birth_of_date", nullable = false)
    private LocalDate birthOfDate;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String firstName) {
        this.name = firstName;
    }

    public @NotNull @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email String email) {
        this.email = email;
    }

    public @NotNull String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    public @NotNull LocalDate getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(@NotNull LocalDate birthDate) {
        this.birthOfDate = birthDate;
    }

    public @NotNull LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(@NotNull LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }





}
