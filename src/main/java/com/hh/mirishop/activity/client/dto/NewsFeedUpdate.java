package com.hh.mirishop.activity.client.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsFeedUpdate {

    private Long activityId;
    private String newsFeedType;  // 활동 유형 : post/comment/like
    private LocalDateTime updatedAt;
}
