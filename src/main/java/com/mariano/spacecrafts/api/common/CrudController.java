package com.mariano.spacecrafts.api.common;

import com.mariano.spacecrafts.core.common.CrudService;
import com.mariano.spacecrafts.core.common.LayerMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public abstract class CrudController<P, B> {

    private final LayerMapper<B, P> mapper;
    private final CrudService<B> service;

    protected CrudController(LayerMapper<B, P> mapper, CrudService<B> service) {
        this.mapper = mapper;
        this.service = service;
    }

    public ResponseEntity<Page<P>> getByName(String name,Pageable pageable) {
        var page = service.getByName(name,pageable);
        var mappedPage = page.map(mapper::toOuterLayer);
        return ResponseEntity.ok(mappedPage);
    }

    public ResponseEntity<Page<P>> getAll(Pageable pageable) {
        var page = service.getAll(pageable);
        var mappedPage = page.map(mapper::toOuterLayer);
        return ResponseEntity.ok(mappedPage);
    }

    public ResponseEntity<P> getById(Long id) {
        var entity = service.getById(id);
        var dto = mapper.toOuterLayer(entity);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<P> create(@Valid P request) {
        var businessRequest = mapper.toBusinessLayer(request);
        var created = mapper.toOuterLayer(service.create(businessRequest));
        return ResponseEntity.ok(created);
    }

    public ResponseEntity<P> update(@Valid P request) {
        var businessRequest = mapper.toBusinessLayer(request);
        var updated = mapper.toOuterLayer(service.update(businessRequest));
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}