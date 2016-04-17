package com.samsung.rd.dogex.model.domain;

import com.samsung.rd.dogex.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Doge {
    private final long id;
    private final long groupId;
    private final String name;
    private final int iconId;

    private final static List<Integer> drawables = new ArrayList<>();
    private final static Random random = new Random();

    static {
        drawables.add(R.drawable.casual);
        drawables.add(R.drawable.casual_left);
        drawables.add(R.drawable.cool);
        drawables.add(R.drawable.ice);
        drawables.add(R.drawable.lion);
        drawables.add(R.drawable.painting);
        drawables.add(R.drawable.rainy);
    }


    public Doge(long id, long groupId, String name) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        iconId = drawables.get(random.nextInt(drawables.size()));
    }

    public long getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }
}
