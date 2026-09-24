package com.pm.patientservice.kafka;
import com.pm.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(
            KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        sendEvent(patient, "PATIENT_CREATED");
    }

    public void sendEvent(Patient patient, String eventType) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(eventType)
                .build();

        try {
            kafkaTemplate.send("patient", event.toByteArray())
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish patient event ({}) for patientId={}: {}",
                                    eventType, patient.getId(), ex.getMessage());
                        } else {
                            log.info("Successfully published patient event ({}) to topic 'patient': patientId={}, offset={}",
                                    eventType, patient.getId(), result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending Patient event ({}): {}", eventType, e.getMessage());
        }
    }
}