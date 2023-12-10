package com.chtrembl.petstore.order.api;

import com.azure.cosmos.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class CosmosDbConfiguration {

    @Value("${spring.cosmos.account_endpoint}")
    private String accountEndpoint;
    @Value("${spring.cosmos.account_key}")
    private String accountKey;

    @Bean
    public CosmosContainer ordersContainer(CosmosClient cosmosClient) {
        return cosmosClient.getDatabase("orders").getContainer("orders");
    }

    @Bean
    public CosmosClient cosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(accountEndpoint)
                .key(accountKey)
                .preferredRegions(Collections.singletonList("East US"))
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildClient();
    }

}