package com.nazir.pos.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class Pagination {

    private Integer pageNo;     // default 0
    private Integer pageSize;   // default 20
    private String sortBy;      // default createdAt
    private Sort.Direction sortOrder = Sort.Direction.DESC;

    public Pageable toPageable(String defaultSort) {
        int pNo = pageNo == null ? 0 : pageNo;
        int pSize = pageSize == null ? 20 : pageSize;
        String sort = (sortBy == null || sortBy.isBlank()) ? defaultSort : sortBy;

        return PageRequest.of(pNo, pSize, Sort.by(sortOrder, sort));
    }
}
