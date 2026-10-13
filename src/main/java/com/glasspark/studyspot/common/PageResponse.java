package com.glasspark.studyspot.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> list;
    private PageInfo page;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int current;
        private int size;
        private int totalPages;
        private long totalElements;
        private boolean first;
        private boolean last;
    }

    public static <T> PageResponse<T> of(List<T> list, int current, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(
                list,
                new PageInfo(
                        current,
                        size,
                        totalPages,
                        totalElements,
                        current == 0,
                        current >= totalPages - 1
                )
        );
    }
}