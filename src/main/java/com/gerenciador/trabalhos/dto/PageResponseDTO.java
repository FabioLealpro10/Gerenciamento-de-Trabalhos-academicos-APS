package com.gerenciador.trabalhos.dto;

import java.util.List;

<<<<<<< HEAD
import org.springframework.data.domain.Page;

=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
public record PageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {
<<<<<<< HEAD

    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }
=======
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
}
