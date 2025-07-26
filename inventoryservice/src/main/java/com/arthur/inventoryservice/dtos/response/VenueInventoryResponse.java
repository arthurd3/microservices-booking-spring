package com.arthur.inventoryservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationInventoryResponse {
    private Long venueId;
    private String venueName;
    private Long totalCapacity;
}
