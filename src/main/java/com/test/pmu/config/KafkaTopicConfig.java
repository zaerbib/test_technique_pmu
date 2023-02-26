package com.test.pmu.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value(value = "${microservice.kafka.topics.course-event-store:course-event-store}")
    private String courseTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic courseEventStoreTopicInitializer(KafkaAdmin kafkaAdmin) {
        try {
            final var topic = new NewTopic(courseTopicName, 3, (short) 1);
            kafkaAdmin.createOrModifyTopics(topic);
            log.info("(bankAccountEventStoreTopicInitializer) topic: {}", topic);
            return topic;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
