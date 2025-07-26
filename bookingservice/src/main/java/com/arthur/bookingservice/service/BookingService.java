package com.arthur.bookingservice.service;

import com.arthur.bookingservice.client.InventoryServiceClient;
import com.arthur.bookingservice.dto.request.BookingRequest;
import com.arthur.bookingservice.dto.response.BookingResponse;
import com.arthur.bookingservice.dto.response.InventoryResponse;
import com.arthur.bookingservice.entity.Customer;
import com.arthur.bookingservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public BookingResponse createBooking(final BookingRequest request) {
        final Customer customer = customerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        System.out.println("Inventory service Response = " + inventoryResponse);

        return BookingResponse.builder().build();
    }
}
