package com.samsung.rd.dogex.model.domain;

import java.util.ArrayList;
import java.util.List;

public class DogeGroup {
    private final long id;
    private final String name;
    private final List<Doge> doges = new ArrayList<>();

    public DogeGroup(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addDoge(Doge doge) {
        doges.add(doge);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Doge getDoge(int index) {
        return doges.get(index);
    }

    public int getDogesCount() {
        return doges.size();
    }
}

