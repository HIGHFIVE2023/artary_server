package com.highfive.artary.dto.diary;

import lombok.Getter;

@Getter
public class DiaryAllDto<T> {

    private T data;
    private PageInfo pageInfo;

    public DiaryAllDto(T data, PageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }
}
