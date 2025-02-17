package com.mariano.spacecrafts.core.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<T> {
    Page<T> getAll(Pageable pageable);

    Page<T> getByName(String name,Pageable pageable);

    T getById(@NotNull Long id);

    T create(@Valid T request);

    T update(@Valid T request);

    void delete(@NotNull Long id);
}

