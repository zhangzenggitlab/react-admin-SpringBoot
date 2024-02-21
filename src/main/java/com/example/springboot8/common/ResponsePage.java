package com.example.springboot8.common;

import lombok.Data;

@Data
public class ResponsePage {
    private int page;

    private int pageSize;

    private int total;

    private Object data;


    public ResponsePage(int page, int pageSize, int total, Object data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }
}
