package com.hh.mirishop.activity.post.controller;

import com.hh.mirishop.activity.common.dto.BaseResponse;
import com.hh.mirishop.activity.post.dto.PostRequest;
import com.hh.mirishop.activity.post.dto.PostResponse;
import com.hh.mirishop.activity.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 글 내용 정보를 받아서 글을 생성합니다.
     */
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createPost(@Valid @RequestBody PostRequest postRequest,
                                                         @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        Long postId = postService.createPost(postRequest, currentMemberNumber);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(postId)
                .toUri();

        return ResponseEntity.created(location).body(BaseResponse.of("게시글이 생성되었습니다.", true, null));
    }

    /**
     * 유저가 작성한 본인의 모든 글을 볼 수 있습니다.
     */
    @GetMapping
    public ResponseEntity<BaseResponse<Page<PostResponse>>> getAllPosts(@RequestParam("page") int page,
                                                                        @RequestParam("size") int size,
                                                                        @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        Page<PostResponse> postList = postService.getAllpostsByMember(page - 1, size, currentMemberNumber);

        return ResponseEntity.ok(BaseResponse.of("게시글 목록 조회 성공", true, postList));
    }

    /**
     * 작성한 게시글 하나를 볼 수 있습니다.
     * postResponse로 게시글의 모든 정보를 전달합니다.
     */
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(@PathVariable Long postId){
        PostResponse postResponse = postService.getPost(postId);
        return ResponseEntity.ok(BaseResponse.of("게시글 내용 조회 성공", true, postResponse));
    }

    /**
     * postId와 게시글 내용을 받아서 글을 수정합니다.
     */
    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> updatePost(@PathVariable("postId") Long postId,
                                                         @RequestBody PostRequest postRequest,
                                                         @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        postService.updatePost(postId, postRequest, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("게시글이 업데이트되었습니다.", true, null));
    }

    /**
     * postId를 받아서 게시글을 삭제합니다.
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(@PathVariable("postId") Long postId,
                                                         @RequestHeader(name = "X-MEMBER-NUMBER") Long currentMemberNumber) {
        postService.deletePost(postId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("게시글이 삭제되었습니다.", true, null));
    }
}
