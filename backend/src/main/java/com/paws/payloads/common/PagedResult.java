package com.paws.payloads.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResult <T>{
    private List<T> data;
    private int pageIndex;
    private int pageSize;
    private int totalPages;

    public boolean isEmpty() {
        return totalPages > 0;
    }

    public boolean hasPrevious() {
        return (pageIndex - 1) >= 0;
    }

    public boolean hasNext() {
        return (pageIndex + 1) < totalPages;
    }



}
