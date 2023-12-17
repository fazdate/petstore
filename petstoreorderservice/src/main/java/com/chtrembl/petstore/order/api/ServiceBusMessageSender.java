package com.chtrembl.petstore.order.api;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusMessageSender.class);

    @Value("${petstore.service.servicebus.connection_string}")
    private String serviceBusConnectionString;
    @Value("${petstore.service.servicebus.queue_name}")
    private String queueName;

    public void sendMessage(String message) {
        try (ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(serviceBusConnectionString)
                .sender()
                .queueName(queueName)
                .buildClient()) {

            senderClient.sendMessage(new ServiceBusMessage(message));
            LOGGER.info("Order message has been successfully sent to Azure Service Bus: {}", message);
        } catch (Exception e) {
            LOGGER.error("Failed to sent the order message to Azure Service Bus", e);
        }
    }
}