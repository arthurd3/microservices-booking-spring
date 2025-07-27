package com.arthur.orderservice.service;

import com.arthur.bookingservice.event.BookingEvent;
import com.arthur.orderservice.client.InventoryServiceClient;
import com.arthur.orderservice.entity.Order;
import com.arthur.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;


    @KafkaListener(topics = "booking" , groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent){
        log.info("Order event received: {}", bookingEvent);

        Order order = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);


        inventoryServiceClient.updateInventory(order.getEventId() , order.getTicketCount());

        log.info("Order has been updated");
    }

    private Order createOrder(BookingEvent bookingEvent){
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

}
