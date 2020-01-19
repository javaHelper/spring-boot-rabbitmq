package com.example.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${javainuse.rabbitmq.queue}")
	String queueName;

	@Value("${javainuse.rabbitmq.exchange}")
	String exchange;

	@Value("${javainuse.rabbitmq.routingkey}")
	private String routingkey;

	// Simple container collecting information to describe a queue. Used in conjunction with AmqpAdmin.
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	// Simple container collecting information to describe a direct exchange.
	// Used in conjunction with administrative operations.
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	/** Simple container collecting information to describe a binding. Takes String destination and exchange names as
	 * arguments to facilitate wiring using code based configuration. Can be used in conjunction with {@link AmqpAdmin}, or
	 * created via a {@link BindingBuilder}.
	 * 
	 */
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}

	// Message converter interface.
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	 /** Specifies a basic set of AMQP operations.
	 *
	 * Provides synchronous send and receive methods. The {@link #convertAndSend(Object)} and
	 * {@link #receiveAndConvert()} methods allow let you send and receive POJO objects.
	 * Implementations are expected to delegate to an instance of
	 * {@link org.springframework.amqp.support.converter.MessageConverter} to perform
	 * conversion to and from AMQP byte[] payload type.*/
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}
