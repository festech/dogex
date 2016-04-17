package com.samsung.rd.dogex.model;


import com.samsung.rd.dogex.model.domain.DogeGroup;

import java.util.List;

/**
 * Interface of product data providers
 */
public interface DogeDataProvider {
    /**
     * Get all products, divided into defined product groups. Doesn't return empty groups.
     * @return list of nonempty groups of products
     */
    List<DogeGroup> getAllNotEmpty();
}
