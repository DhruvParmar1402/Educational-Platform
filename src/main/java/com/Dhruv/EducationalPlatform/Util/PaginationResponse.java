package com.Dhruv.EducationalPlatform.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private Object data;
    private String lastEvaluatedKey;
    private int limit;
    private boolean hasMore;
}