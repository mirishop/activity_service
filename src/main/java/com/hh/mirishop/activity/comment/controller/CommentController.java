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

    /**
     * postId과 댓글 내용을 입력 받아서 댓글을 생성합니다.
     * URI를 반환하여 프론트로 전달합니다.
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

    /**
     * commentId를 받아서 댓글을 삭제합니다.(대댓글도 가능)
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable("commentId") Long commentId,
                                                            @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        commentService.deleteComment(commentId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("댓글이 삭제되었습니다.", true, null));
    }

    /**
     * 댓글 내용을 받아서 대댓글을 생성합니다.
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
