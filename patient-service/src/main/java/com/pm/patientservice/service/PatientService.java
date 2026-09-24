package com.pm.patientservice.service;

import billing.BillingResponse;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistsException;
import com.pm.patientservice.exceptions.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    private static final Logger log =
            LoggerFactory.getLogger(PatientService.class);

    public PatientService(
            PatientRepository patientRepository,
            BillingServiceGrpcClient billingServiceGrpcClient,
            KafkaProducer kafkaProducer
    ) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatient() {

        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    public PatientResponseDTO createPatient(
            PatientRequestDTO patientRequestDTO
    ) {

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already exists: " +
                            patientRequestDTO.getEmail()
            );
        }

        // 1. Save patient
        Patient patient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO)
        );

        // 2. Create billing account through gRPC
        BillingResponse billingResponse =
                billingServiceGrpcClient.createBillingAccount(
                        patient.getId().toString(),
                        patient.getName(),
                        patient.getEmail()
                );

        log.info(
                "Billing account created: {}",
                billingResponse
        );

        // 3. Publish patient-created event to Kafka
        kafkaProducer.sendEvent(patient);

        log.info(
                "Patient created event published: patientId={}",
                patient.getId()
        );

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(
            UUID id,
            PatientRequestDTO patientRequestDTO
    ) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(
                        () -> new PatientNotFoundException(
                                "Patient Not Found: " + id
                        )
                );

        if (patientRepository.existsByEmailAndIdNot(
                patientRequestDTO.getEmail(),
                id
        )) {
            throw new EmailAlreadyExistsException(
                    "Email already exists: " +
                            patientRequestDTO.getEmail()
            );
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());

        patient.setBirthOfDate(
                LocalDate.parse(
                        patientRequestDTO.getBirthDate().toString()
                )
        );

        Patient updatedPatient = patientRepository.save(patient);

        kafkaProducer.sendEvent(updatedPatient, "PATIENT_UPDATED");

        log.info(
                "Patient updated event published: patientId={}",
                updatedPatient.getId()
        );

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}