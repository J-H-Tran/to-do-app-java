package co.jht.model.domain.response.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper<E, D> {

    D toDTO(E entity);

    E toEntity(D dto);

    default List<D> toDTOList(List<E> entities) {
        return entities
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    default List<E> toEntityList(List<D> dtos) {
        return dtos
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}