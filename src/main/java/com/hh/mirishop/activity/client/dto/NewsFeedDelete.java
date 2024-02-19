package com.hh.mirishop.activity.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsFeedDelete {

    private Long activityId;
    private String newsFeedType;  // 활동 유형 : post/comment/like
    private Boolean isDeleted;
}
