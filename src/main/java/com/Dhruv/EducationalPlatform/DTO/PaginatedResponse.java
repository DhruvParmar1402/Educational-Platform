package com.Dhruv.EducationalPlatform.DTO;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> data;
    private String lastEvaluatedKey;

    public PaginatedResponse(List<T> data, String lastEvaluatedKey) {
        this.data = data;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    public List<T> getData() {
        return data;
    }

    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }
}
