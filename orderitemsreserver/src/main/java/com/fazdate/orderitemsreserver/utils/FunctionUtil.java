package com.fazdate.orderitemsreserver.utils;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableServiceException;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public final class FunctionUtil {

    private static final String AZURE_STORAGE = System.getenv("AZURE_STORAGE");
    private static final String EVENT_GRID_ENDPOINT = System.getenv("EVENT_GRID_ENDPOINT");
    private static final String EVENT_GRID_KEY = System.getenv("EVENT_GRID_KEY");
    private static final String BLOB_UPLOAD = "BlobUpload";
    private static final String BLOB_UPLOAD_FAILED = "BlobUploadFailed";
    private static final String BLOB_UPLOAD_FAILURES = "blobUploadFailures";
    private static final String FAILED_COUNT = "failedCount";

    private FunctionUtil() {
    }

    public static void uploadTextToBlob(String blobReference, String content) throws Exception {
        CloudBlobContainer container = getBlobContainer(getStorageAccount(), "orders");
        CloudBlockBlob blockBlob = container.getBlockBlobReference(blobReference);
        blockBlob.uploadText(content);
    }

    public static void handleBlobUploadException(String userId, String message) {
        TableClient tableClient = getTableClient();
        TableEntity entity = getTableEntity(userId, tableClient);

        if (entity == null) {
            tableClient.createEntity(new TableEntity(BLOB_UPLOAD, userId).addProperty(FAILED_COUNT, 1));
        } else {
            entity.addProperty(FAILED_COUNT, (int) entity.getProperty(FAILED_COUNT) + 1);
            tableClient.updateEntity(entity);
        }

        if ((int) entity.getProperty(FAILED_COUNT) >= 3) {
            sendBlobUploadFailedMessageToEventGrid(message);
            entity.addProperty(FAILED_COUNT, 0);
            tableClient.updateEntity(entity);
        }
    }

    private static TableEntity getTableEntity(String userId, TableClient tableClient) {
        TableEntity entity;

        try {
            entity = tableClient.getEntity(BLOB_UPLOAD, userId);
        } catch (TableServiceException ex) {
            if (ex.getResponse().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                entity = null;
            } else {
                throw ex;
            }
        }

        return entity;
    }

    private static TableClient getTableClient() {
        TableServiceClient tableServiceClient = new TableServiceClientBuilder()
                .connectionString(AZURE_STORAGE)
                .buildClient();

        tableServiceClient.createTableIfNotExists(BLOB_UPLOAD_FAILURES);
        return tableServiceClient.getTableClient(BLOB_UPLOAD_FAILURES);
    }

    private static EventGridPublisherClient<EventGridEvent> getEventGridEventEventGridPublisherClient() {
        return new EventGridPublisherClientBuilder().endpoint(EVENT_GRID_ENDPOINT).credential(new AzureKeyCredential(EVENT_GRID_KEY)).buildEventGridEventPublisherClient();
    }

    private static void sendBlobUploadFailedMessageToEventGrid(String message) {
        getEventGridEventEventGridPublisherClient().sendEvent(new EventGridEvent(BLOB_UPLOAD_FAILED, BLOB_UPLOAD_FAILED, BinaryData.fromString(message), "1.0"));
    }

    private static CloudBlobContainer getBlobContainer(CloudStorageAccount storageAccount, String containerName) throws URISyntaxException, StorageException {
        CloudBlobClient client = storageAccount.createCloudBlobClient();
        return client.getContainerReference(containerName);
    }

    private static CloudStorageAccount getStorageAccount() throws URISyntaxException, InvalidKeyException {
        return CloudStorageAccount.parse(AZURE_STORAGE);
    }

}
