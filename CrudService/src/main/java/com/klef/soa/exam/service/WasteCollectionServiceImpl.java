package com.klef.soa.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.soa.exam.entity.WasteCollection;
import com.klef.soa.exam.repository.WasteCollectionRepository;

@Service
public class WasteCollectionServiceImpl implements WasteCollectionService {

    @Autowired
    private WasteCollectionRepository wasteCollectionRepository;

    @Override
    public WasteCollection addWasteCollection(WasteCollection wasteCollection) {
        wasteCollection.setId(null);
        return wasteCollectionRepository.save(wasteCollection);
    }

    @Override
    public List<WasteCollection> displayAll() {
        return wasteCollectionRepository.findAll();
    }

    @Override
    public String deleteWasteCollection(Long id) {
        if (wasteCollectionRepository.existsById(id)) {
            wasteCollectionRepository.deleteById(id);
            return "WasteCollection record deleted successfully with ID: " + id;
        }
        return "WasteCollection record not found with ID: " + id;
    }
}
