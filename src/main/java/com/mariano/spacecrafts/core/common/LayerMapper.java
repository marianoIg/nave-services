package com.mariano.spacecrafts.core.common;

import org.springframework.data.domain.Page;

import java.util.List;

public interface LayerMapper<B, O> {

    B toBusinessLayer(O outerLayerObject);

    O toOuterLayer(B businessObject);

    default Page<O> toOuterLayer(Page<B> businessPage) {
        return businessPage.map(this::toOuterLayer);
    }

    default List<O> toOuterLayer(List<B> businessList) {
        return businessList.stream().map(this::toOuterLayer).toList();
    }

    default Page<B> toBusinessLayer(Page<O> outerPage) {
        return outerPage.map(this::toBusinessLayer);
    }

    default List<B> toBusinessLayer(List<O> outerList) {
        return outerList.stream().map(this::toBusinessLayer).toList();
    }
}

