package com.klef.soa.docker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.soa.docker.dto.WasteCollectionRequestDTO;
import com.klef.soa.docker.dto.WasteCollectionResponseDTO;
import com.klef.soa.docker.service.WasteCollectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
@Tag(name = "Waste Collection Controller", description = "Operations for managing Waste Collection records")
public class WasteCollectionController {

    @Autowired
    private WasteCollectionService service;

    @GetMapping("/")
    @Operation(summary = "Root health endpoint", description = "Checks whether the waste collection backend service is running")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Waste Collection Backend Service Running (Docker Project)");
    }

    @PostMapping({"/api/waste-collections", "/Add"})
    @Operation(summary = "Add a new waste collection record", description = "Registers a new waste collection zone. ID is auto-generated.")
    public ResponseEntity<WasteCollectionResponseDTO> addWasteCollection(
            @Valid @RequestBody WasteCollectionRequestDTO requestDTO) {
        WasteCollectionResponseDTO created = service.addWasteCollection(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping({"/api/waste-collections", "/DisplayAll"})
    @Operation(summary = "Display all waste collection records", description = "Fetches a list of all existing waste collection records.")
    public ResponseEntity<List<WasteCollectionResponseDTO>> getAllWasteCollections() {
        return ResponseEntity.ok(service.getAllWasteCollections());
    }

    @GetMapping("/api/waste-collections/{id}")
    @Operation(summary = "Get waste collection record by ID", description = "Retrieves details of a specific record by its primary key.")
    public ResponseEntity<?> getWasteCollectionById(@PathVariable Long id) {
        WasteCollectionResponseDTO found = service.getWasteCollectionById(id);
        if (found != null) {
            return ResponseEntity.ok(found);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Waste collection record not found with id: " + id);
    }

    @DeleteMapping({"/api/waste-collections/{id}", "/Delete/{id}"})
    @Operation(summary = "Delete a waste collection record", description = "Deletes an existing waste collection record by ID.")
    public ResponseEntity<String> deleteWasteCollection(@PathVariable Long id) {
        if (service.deleteWasteCollection(id)) {
            return ResponseEntity.ok("Waste Collection deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Waste Collection not found with id : " + id);
    }
}
