package com.klef.soa.exam.service;

import java.util.List;

import com.klef.soa.exam.entity.WasteCollection;

public interface WasteCollectionService {
    WasteCollection addWasteCollection(WasteCollection wasteCollection);
    List<WasteCollection> displayAll();
    String deleteWasteCollection(Long id);
}
