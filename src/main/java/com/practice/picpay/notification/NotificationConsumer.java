package com.practice.picpay.notification;


import com.practice.picpay.authorization.AuthorizerService;
import com.practice.picpay.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6")
                .build();
    }


    // Idealmente seria melhor ter um objeto notificação com a transação dentro com dtos na aplicação
    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotification(Transaction transaction) {
        log.info("Received transaction notification", transaction);
        var response = restClient.get()
                .retrieve()
                .toEntity(Notification.class);

        if(response.getStatusCode().isError() || !response.getBody().message())
            throw new NotificationException("Error sending notification");

        log.info("Notifcation has been sent {}...", response.getBody());
    }
}
