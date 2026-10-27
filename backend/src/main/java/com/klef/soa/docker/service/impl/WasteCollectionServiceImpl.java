package com.klef.soa.docker.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.klef.soa.docker.dto.WasteCollectionRequestDTO;
import com.klef.soa.docker.dto.WasteCollectionResponseDTO;
import com.klef.soa.docker.entity.WasteCollection;
import com.klef.soa.docker.repository.WasteCollectionRepository;
import com.klef.soa.docker.service.WasteCollectionService;

@Service
@Transactional
public class WasteCollectionServiceImpl implements WasteCollectionService {

    @Autowired
    private WasteCollectionRepository repository;

    @Override
    public WasteCollectionResponseDTO addWasteCollection(WasteCollectionRequestDTO requestDTO) {
        WasteCollection entity = new WasteCollection();
        entity.setZoneName(requestDTO.getZoneName());
        entity.setCollectionDay(requestDTO.getCollectionDay());
        entity.setTruckCount(requestDTO.getTruckCount());
        entity.setActive(requestDTO.getActive());

        WasteCollection saved = repository.save(entity);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WasteCollectionResponseDTO> getAllWasteCollections() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WasteCollectionResponseDTO getWasteCollectionById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    @Override
    public boolean deleteWasteCollection(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private WasteCollectionResponseDTO mapToResponseDTO(WasteCollection entity) {
        return new WasteCollectionResponseDTO(
                entity.getId(),
                entity.getZoneName(),
                entity.getCollectionDay(),
                entity.getTruckCount(),
                entity.getActive()
        );
    }
}
