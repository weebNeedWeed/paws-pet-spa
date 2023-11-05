package com.paws.services.common.payloads;

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
}
