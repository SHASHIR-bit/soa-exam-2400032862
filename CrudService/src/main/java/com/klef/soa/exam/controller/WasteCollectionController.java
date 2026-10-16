package com.klef.soa.exam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klef.soa.exam.entity.WasteCollection;
import com.klef.soa.exam.service.WasteCollectionService;

@RestController
public class WasteCollectionController {

    @Autowired
    private WasteCollectionService wasteCollectionService;

    @GetMapping("/")
    public ResponseEntity<String> rootRoute() {
        return ResponseEntity.ok("WasteCollection CRUD Service is running...!!!");
    }

    @PostMapping("/Add")
    public ResponseEntity<WasteCollection> addWasteCollection(@RequestBody WasteCollection wasteCollection) {
        return new ResponseEntity<>(wasteCollectionService.addWasteCollection(wasteCollection), HttpStatus.CREATED);
    }

    @GetMapping("/DisplayAll")
    public ResponseEntity<List<WasteCollection>> displayAll() {
        return ResponseEntity.ok(wasteCollectionService.displayAll());
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteWasteCollection(@PathVariable Long id) {
        return ResponseEntity.ok(wasteCollectionService.deleteWasteCollection(id));
    }
}
