package com.arthur.bookingservice.service;

import com.arthur.bookingservice.client.InventoryServiceClient;
import com.arthur.bookingservice.dto.request.BookingRequest;
import com.arthur.bookingservice.dto.response.BookingResponse;
import com.arthur.bookingservice.dto.response.InventoryResponse;
import com.arthur.bookingservice.entity.Customer;
import com.arthur.bookingservice.event.BookingEvent;
import com.arthur.bookingservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingResponse createBooking(final BookingRequest request) {
        final Customer customer = customerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        log.info("Creating new booking for customer {}", customer.getId());

        if(inventoryResponse.getCapacity() < request.getTicketCount())
            throw new RuntimeException("Capacity less than ticket count");

        final BookingEvent bookingEvent = createBookingEvent(request , customer , inventoryResponse);

        kafkaTemplate.send("booking", bookingEvent);
        log.info("BookingEvent sent to Kafka {}", bookingEvent);


        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(final BookingRequest request ,
                                            final Customer customer ,
                                            final InventoryResponse inventoryResponse) {

        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();

    }
}
