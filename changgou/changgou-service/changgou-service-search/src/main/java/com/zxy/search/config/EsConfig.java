package com.zxy.search.config;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EsConfig   {
//public class EsConfig  extends AbstractElasticsearchConfiguration {

  /*  @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("39.106.55.241:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }*/

}