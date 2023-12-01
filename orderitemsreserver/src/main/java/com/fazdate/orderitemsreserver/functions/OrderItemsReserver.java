package com.fazdate.orderitemsreserver.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Optional;

public class OrderItemsReserver {

    @FunctionName("OrderItemsReserver")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = HttpMethod.POST, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request, final ExecutionContext context) throws URISyntaxException, InvalidKeyException, StorageException {

        context.getLogger().info("OrderItemsReserver processed a request");

        String body = StringUtils.defaultIfEmpty(String.valueOf(request.getBody()), StringUtils.EMPTY);
        body = body.substring(body.indexOf('[') + 1, body.lastIndexOf(']')); // removing "Optional" and the square brackets

        try {
            uploadTextToBlob(body, getBlobContainer(getStorageAccount(), "orders"));
        } catch (IOException e) {
            return handleResponse(request, "There was an exception, while trying to upload the file to the container\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return handleResponse(request, "Order is saved", HttpStatus.OK);
    }

    private static CloudStorageAccount getStorageAccount() throws URISyntaxException, InvalidKeyException {
        return CloudStorageAccount.parse(System.getenv("AZURE_STORAGE"));
    }

    private static CloudBlobContainer getBlobContainer(CloudStorageAccount storageAccount, String containerName) throws URISyntaxException, StorageException {
        CloudBlobClient client = storageAccount.createCloudBlobClient();
        return client.getContainerReference(containerName);
    }

    private static void uploadTextToBlob(String content, CloudBlobContainer container) throws URISyntaxException, StorageException, IOException {
        CloudBlockBlob blockBlob = container.getBlockBlobReference("order.json");
        blockBlob.uploadText(content);
    }

    private static HttpResponseMessage handleResponse(HttpRequestMessage<Optional<String>> request, String message, HttpStatus httpStatus) {
        return request.createResponseBuilder(httpStatus).body(message).build();
    }

}
