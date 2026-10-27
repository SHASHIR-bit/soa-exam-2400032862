package com.klef.soa.docker.dto;

public class WasteCollectionResponseDTO {

    private Long id;
    private String zoneName;
    private String collectionDay;
    private Integer truckCount;
    private Boolean active;

    public WasteCollectionResponseDTO() {
    }

    public WasteCollectionResponseDTO(Long id, String zoneName, String collectionDay, Integer truckCount, Boolean active) {
        this.id = id;
        this.zoneName = zoneName;
        this.collectionDay = collectionDay;
        this.truckCount = truckCount;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
