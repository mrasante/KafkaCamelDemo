package com.esri.support.messaging;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;


/**
 * A singleton class to create a static reference to a 
 * Kafka producer that his the broker at localhost:9092
 * 
 * @author kwasi
 *
 */


public class AppKafkaProducerClient {

	private static Producer<String, String> appClientProducer = null;
	private static final Logger APP_LOGGER = LoggerFactory.getLogger(AppKafkaProducerClient.class); 
	private static AppKafkaProducerClient appKafkaClient = null;
	
	private AppKafkaProducerClient() {
		final Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		appClientProducer = new KafkaProducer<>(props);
		
	}
	
	
	public static AppKafkaProducerClient getInstance() {
		if(appKafkaClient == null)
			appKafkaClient = new AppKafkaProducerClient();
		return appKafkaClient;
	}

	
	public Producer<String, String> getAppClientProducer() {
		return appClientProducer;
	}
	
	public void sendToKafkaProducer(String topic, String message) {
		Future<RecordMetadata> sender = appClientProducer.send(new ProducerRecord<String, String>(topic, 0, "Chat Message", message), 
				(metadata, exception) ->{
					/*APP_LOGGER.info("Timestamp of Message: "+ LocalDateTime.ofInstant(Instant.ofEpochSecond(metadata.timestamp()),
							ZoneId.systemDefault()));
					
					APP_LOGGER.info("Exception in sending message: ", exception);*/
					
					System.out.println("Testing message");
				}
				);
	}
}
