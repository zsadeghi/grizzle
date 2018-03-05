package me.theyinspire.projects.grizzle.explorer.dto;

import java.util.List;

public class ResultPage {

    private final long total;
    private final int pageNumber;
    private final int pageSize;
    private final List<List<Object>> values;
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
    private final int totalPages;
    private final String query;

    public ResultPage(final long total, final int pageNumber, final int pageSize, final List<List<Object>> values,
                      final int totalPages, final String query) {
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.values = values;
        this.query = query;
        this.hasNextPage = pageSize * (pageNumber - 1) < totalPages;
        this.hasPreviousPage = pageNumber > 1;
        this.totalPages = totalPages;
    }

    public long getTotal() {
        return total;
    }

    public List<List<Object>> getValues() {
        return values;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "ResultPage{" +
                "total=" + total +
                ", pageNumber=" + pageNumber +
                ", hasPreviousPage=" + hasPreviousPage +
                ", hasNextPage=" + hasNextPage +
                ", pageSize=" + pageSize +
                ", query=" + query +
                ", values=" + values +
                '}';
    }

}
