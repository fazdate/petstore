package com.fazdate.orderitemsreserver.utils;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public final class FunctionUtil {

    private FunctionUtil() {
    }

    private static CloudStorageAccount getStorageAccount() throws URISyntaxException, InvalidKeyException {
        return CloudStorageAccount.parse(System.getenv("AZURE_STORAGE"));
    }

    private static CloudBlobContainer getBlobContainer(CloudStorageAccount storageAccount, String containerName) throws URISyntaxException, StorageException {
        CloudBlobClient client = storageAccount.createCloudBlobClient();
        return client.getContainerReference(containerName);
    }

    public static void uploadTextToBlob(String content, String blobReference) throws Exception {
        CloudBlobContainer container = getBlobContainer(getStorageAccount(), "orders");
        CloudBlockBlob blockBlob = container.getBlockBlobReference(blobReference);
        blockBlob.uploadText(content);
    }
}
