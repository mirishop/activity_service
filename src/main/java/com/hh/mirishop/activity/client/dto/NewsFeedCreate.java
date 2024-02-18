package com.hh.mirishop.activity.client.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsFeedCreate {

    private String id;

    private Long memberNumber;

    private String newsfeedType; // 활동 유형: post/comment/like -> 받아줄때는 enum으로 변환필요

    private Long activityId; // 활동 고유 ID (게시글 ID, 댓글 ID, 좋아요 ID)

    private Long targetPostId; // 해당하는 활동이 속한 게시글 ID

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;
}
