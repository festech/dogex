package com.samsung.rd.dogex.model;

import com.samsung.rd.dogex.model.domain.DogeGroup;

import java.util.Collections;
import java.util.List;

/**
 * Fallback data provider class in case of JSON source failure
 */
public class EmptyDogeDataProvider implements DogeDataProvider {

    /**
     * @return empty List
     */
    @Override
    public List<DogeGroup> getAllNotEmpty() {
        return Collections.emptyList();
    }
}
