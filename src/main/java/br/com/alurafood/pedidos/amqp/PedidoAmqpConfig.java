package br.com.alurafood.pedidos.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoAmqpConfig {

    @Bean
    public Jackson2JsonMessageConverter getJackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate getRabbitTemplate(ConnectionFactory connectionFactory,
                                            Jackson2JsonMessageConverter jackson2JsonMessageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue filaDetalhesPedido(){
        return QueueBuilder
                .nonDurable("pagamentos.detalhes-pedido")
                .deadLetterExchange(this.deadLetterExchange().getName())
                .build();
    }

    @Bean
    public Queue filaDlqDetalhesPedido(){
        return QueueBuilder
                .nonDurable("pagamentos.detalhes-pedido-dlq")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return ExchangeBuilder
                .fanoutExchange("pagamentos.ex")
                .build();
    }

    @Bean
    public FanoutExchange deadLetterExchange(){
        return ExchangeBuilder
                .fanoutExchange("pagamentos.dlx")
                .build();
    }

    @Bean
    public Binding bindingPagamentosPedidos(){
        return BindingBuilder
                .bind(filaDetalhesPedido())
                .to(fanoutExchange());
    }

    @Bean
    public Binding bindingDlxPagamentosPedidos(){
        return BindingBuilder
                .bind(this.filaDlqDetalhesPedido())
                .to(this.deadLetterExchange());
    }

}
