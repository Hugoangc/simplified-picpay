package com.practice.picpay.notification;


import com.practice.picpay.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }
    public void notify(Transaction transaction) {
        notificationProducer.sendNotification(transaction);
    }


}
