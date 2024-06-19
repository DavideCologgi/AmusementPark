package org.plantict.models;

import lombok.Data;

import java.util.List;

@Data
public class SearchContainer<T> {

    private long total;
    private int page;
    private int size;
    private Sort sort;
    private List<T> data;

    public SearchContainer(long total, int page, int size, Sort sort, List<T> data) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.data = data;
    }
}
