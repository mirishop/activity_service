package com.hh.mirishop.activity.comment.controller;

import com.hh.mirishop.activity.comment.dto.CommentRequest;
import com.hh.mirishop.activity.comment.service.CommentService;
import com.hh.mirishop.activity.common.dto.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /*
    댓글 작성
    */
    @PostMapping
    public ResponseEntity<BaseResponse<URI>> createComment(@Valid @RequestBody CommentRequest commentRequest,
                                                           @PathVariable("postId") Long postId,
                                                           @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        Long commentId = commentService.createCommentOrReply(commentRequest, currentMemberNumber, postId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(commentId)
                .toUri();

        return ResponseEntity.ok(BaseResponse.of("댓글이 생성되었습니다.", true, location));
    }

    /*
    댓글 삭제(대댓글도 가능)
    */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable("commentId") Long commentId,
                                                            @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        commentService.deleteComment(commentId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("댓글이 삭제되었습니다.", true, null));
    }

    /*
    대댓글 작성
    */
    @PostMapping("/reply")
    public ResponseEntity<BaseResponse<URI>> createReply(@Valid @RequestBody CommentRequest commentRequest,
                                                         @PathVariable("postId") Long postId,
                                                         @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        Long commentId = commentService.createCommentOrReply(commentRequest, currentMemberNumber, postId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(commentId)
                .toUri();

        return ResponseEntity.ok(BaseResponse.of("대댓글이 생성되었습니다.", true, location));
    }
}
