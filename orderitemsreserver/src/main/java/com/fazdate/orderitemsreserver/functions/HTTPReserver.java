package com.fazdate.orderitemsreserver.functions;

import com.fazdate.orderitemsreserver.utils.FunctionUtil;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class HTTPReserver {

    @FunctionName("HTTPReserver")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = HttpMethod.POST, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request, final ExecutionContext context) {

        context.getLogger().info("OrderItemsReserver is processing a request");

        String body = StringUtils.defaultIfEmpty(String.valueOf(request.getBody()), StringUtils.EMPTY);
        body = body.substring(body.indexOf('[') + 1, body.lastIndexOf(']')); // removing "Optional" and the square brackets

        try {
            FunctionUtil.uploadTextToBlob(body, "order.json");
        } catch (Exception e) {
            return handleResponse(request, "There was an exception, while trying to upload the file to the container\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return handleResponse(request, "Order is saved", HttpStatus.OK);
    }

    private static HttpResponseMessage handleResponse(HttpRequestMessage<Optional<String>> request, String message, HttpStatus httpStatus) {
        return request.createResponseBuilder(httpStatus).body(message).build();
    }

}
