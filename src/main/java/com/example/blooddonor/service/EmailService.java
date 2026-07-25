package com.example.blooddonor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Handles sending emergency alert emails to matching donors.
// Uses Spring's JavaMailSender, configured in application.properties.
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sends one email. If it fails (e.g. no internet, wrong credentials),
    // we log it and move on - we NEVER want a failed email to crash the
    // whole emergency request.
    public void sendAlert(String toEmail, String donorName, String patientName,
                           String bloodGroup, String hospitalName, String contactNumber) {
        if (toEmail == null || toEmail.isBlank()) {
            return; // this donor didn't provide an email - skip silently
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("🩸 Urgent: " + bloodGroup + " blood needed nearby");
            message.setText(
                    "Hi " + donorName + ",\n\n" +
                    "Someone nearby urgently needs " + bloodGroup + " blood.\n\n" +
                    "Patient: " + patientName + "\n" +
                    (hospitalName != null && !hospitalName.isBlank() ? "Hospital: " + hospitalName + "\n" : "") +
                    "Contact number: " + contactNumber + "\n\n" +
                    "If you're available and able to help, please reach out directly using the number above.\n\n" +
                    "Thank you for being a registered donor on Pulse — you might just save a life today.\n\n" +
                    "- Team Pulse"
            );
            mailSender.send(message);
            log.info("Alert email sent to {}", toEmail);
        } catch (Exception e) {
            // Common causes: wrong app password, no internet, blocked SMTP port on campus WiFi
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}
