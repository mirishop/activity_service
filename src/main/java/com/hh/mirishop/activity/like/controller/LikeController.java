package com.hh.mirishop.activity.like.controller;

import com.hh.mirishop.activity.common.dto.BaseResponse;
import com.hh.mirishop.activity.like.service.LikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeServiceImpl likeService;

    /**
     * 게시글 정보를 받아서 좋아요를 생성합니다.
     */
    @PostMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<Void>> likePost(@PathVariable("postId") Long postId,
                                                       @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        likeService.likePost(postId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("글 좋아요가 완료 되었습니다.", true, null));
    }

    /**
     * 게시글 정보를 받아서 좋아요를 삭제합니다.
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<Void>> unlikePost(@PathVariable("postId") Long postId,
                                                         @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        likeService.unlikePost(postId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("글 좋아요 취소가 완료 되었습니다.", true, null));
    }

    /**
     * 댓글 정보를 받아서 좋아요를 생성합니다.
     */
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> likeComment(@PathVariable("commentId") Long commentId,
                                                          @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        likeService.likeComment(commentId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("댓글 좋아요가 완료 되었습니다.", true, null));
    }

    /**
     * 댓글 정보를 받아서 좋아요를 삭제합니다.
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> unlikeComment(@PathVariable("commentId") Long commentId,
                                                            @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        likeService.unlikeComment(commentId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("댓글 좋아요 취소가 완료 되었습니다.", true, null));
    }
}
