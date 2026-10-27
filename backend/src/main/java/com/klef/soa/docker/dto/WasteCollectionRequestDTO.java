package com.klef.soa.docker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WasteCollectionRequestDTO {

    @NotBlank(message = "Zone name is required")
    @Size(min = 2, max = 100, message = "Zone name must be between 2 and 100 characters")
    private String zoneName;

    @NotBlank(message = "Collection day is required")
    private String collectionDay;

    @NotNull(message = "Truck count is required")
    @Min(value = 1, message = "Truck count must be at least 1")
    @Max(value = 50, message = "Truck count cannot exceed 50")
    private Integer truckCount;

    @NotNull(message = "Active status is required")
    private Boolean active;

    public WasteCollectionRequestDTO() {
    }

    public WasteCollectionRequestDTO(String zoneName, String collectionDay, Integer truckCount, Boolean active) {
        this.zoneName = zoneName;
        this.collectionDay = collectionDay;
        this.truckCount = truckCount;
        this.active = active;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getCollectionDay() {
        return collectionDay;
    }

    public void setCollectionDay(String collectionDay) {
        this.collectionDay = collectionDay;
    }

    public Integer getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(Integer truckCount) {
        this.truckCount = truckCount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
