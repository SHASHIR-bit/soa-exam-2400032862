package com.klef.soa.docker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "waste_collection")
public class WasteCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false)
    private String collectionDay;

    @Column(nullable = false)
    private Integer truckCount;

    @Column(nullable = false)
    private Boolean active;

    public WasteCollection() {
    }

    public WasteCollection(Long id, String zoneName, String collectionDay, Integer truckCount, Boolean active) {
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
