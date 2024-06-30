package org.hu.reservation.service;

import com.azure.communication.email.EmailAsyncClient;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollerFlux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailAsyncClient emailClient;

    @Value("${reservation.app.url}")
    private String reservationUrl;

    @Value("${azure.communication.service.sender.email}")
    private String senderEmail;

    public void sendEmail(String toEmail, String token) {

        EmailMessage message = new EmailMessage()
                .setSenderAddress(senderEmail)
                .setToRecipients(toEmail)
                .setSubject("Thank you for your reservation")
                .setBodyHtml("We confirm your reservation with token <a href='%s/cancelReservation/%s'>%s</a>".formatted(reservationUrl, token, token));

        PollerFlux<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(message);
        poller.subscribe(
                response -> {
                    if (response.getStatus() == LongRunningOperationStatus.SUCCESSFULLY_COMPLETED) {
                        logger.info("Successfully sent the email (operation id: %s)".formatted(response.getValue().getId()));
                    } else {
                        logger.info("Email send status: " + response.getStatus());
                    }
                },
                error -> {
                    logger.error("Error occurred while sending email: " + error.getMessage(), error);
                }
        );
    }

}
