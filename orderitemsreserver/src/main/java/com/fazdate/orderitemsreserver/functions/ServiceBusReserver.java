package com.fazdate.orderitemsreserver.functions;

import com.fazdate.orderitemsreserver.utils.FunctionUtil;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;
import org.json.JSONObject;

public class ServiceBusReserver {

    private static final String FAILED_TO_UPLOAD_MESSAGE = "Failed to upload message";

    @FunctionName("SBReserver")
    public void run(@ServiceBusQueueTrigger(name = "req", queueName = "queue", connection = "SERVICEBUS_CONNECTION_STRING") String message, final ExecutionContext context) {
        context.getLogger().info("OrderItemsReserver is processing a request");

        JSONObject jsonObject = new JSONObject(message);
        String userId = jsonObject.getString("id") + ".json";

       if (jsonObject.has("id")) {
            try {
                FunctionUtil.uploadTextToBlob(userId, message);
                context.getLogger().info("Message was uploaded successfuly");
            } catch (Exception e) {
                FunctionUtil.handleBlobUploadException(userId, message);
            }
       }
    }
}
