package com.klef.soa.docker.service;

import java.util.List;

import com.klef.soa.docker.dto.WasteCollectionRequestDTO;
import com.klef.soa.docker.dto.WasteCollectionResponseDTO;

public interface WasteCollectionService {

    WasteCollectionResponseDTO addWasteCollection(WasteCollectionRequestDTO requestDTO);

    List<WasteCollectionResponseDTO> getAllWasteCollections();

    WasteCollectionResponseDTO getWasteCollectionById(Long id);

    boolean deleteWasteCollection(Long id);
}
