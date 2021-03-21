package com.movie.core.service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


@Component
public class PagingUtils {
    public Pageable setPageable(int page, int limit, String sortExpression, String sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortExpression != null && sortDirection != null) {
            sort = Sort.by(sortDirection.equals("1") ? Sort.Direction.ASC : Sort.Direction.DESC, sortExpression);
        }
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        return pageable;
    }
}
