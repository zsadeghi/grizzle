package me.theyinspire.projects.grizzle.service;

import java.util.List;

public class ResultPage<E> {

    private final List<E> content;
    private final int pageNumber;
    private final int totalPages;
    private final int pageSize;
    private final long totalItems;
    private final long time;

    public ResultPage(final List<E> content, final int pageNumber, final int totalPages, final int pageSize,
                      final long totalItems, final long time) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.time = time;
    }

    public List<E> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ResultPage{" +
                "content=" + content +
                ", pageNumber=" + pageNumber +
                ", totalPages=" + totalPages +
                ", pageSize=" + pageSize +
                ", totalItems=" + totalItems +
                ", time=" + time +
                '}';
    }

}
