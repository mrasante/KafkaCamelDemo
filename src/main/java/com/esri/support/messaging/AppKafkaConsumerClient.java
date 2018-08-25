package com.esri.support.messaging;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class AppKafkaConsumerClient {
	
	private static Consumer<String, String> appClientConsumer = null;
	private static final Logger APP_LOGGER = LoggerFactory.getLogger(AppKafkaProducerClient.class); 
	private static AppKafkaConsumerClient appKafkaClient = null;
	
	private AppKafkaConsumerClient() {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.80.128:9092"); //"csc-kasante7l3.esri.com:9092");
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, "interview_test_app");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		appClientConsumer = new KafkaConsumer<>(props);
	}
	
	public static AppKafkaConsumerClient getInstance() {
		if(appKafkaClient == null)
			appKafkaClient = new AppKafkaConsumerClient();
		return appKafkaClient;
	}

	
	public static Consumer<String, String> getAppConsumer() {
		return appClientConsumer;
	}
	
	public void subscribeToTopic(String topic) {
		appClientConsumer.subscribe(Arrays.asList("firstTopic", "secondTopic"));
	}
}
