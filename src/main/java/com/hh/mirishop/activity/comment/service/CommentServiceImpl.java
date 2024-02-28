package com.hh.mirishop.activity.comment.service;

import com.hh.mirishop.activity.client.NewsfeedFeignClient;
import com.hh.mirishop.activity.client.UserFeignClient;
import com.hh.mirishop.activity.client.dto.NewsFeedCreate;
import com.hh.mirishop.activity.client.dto.NewsFeedDelete;
import com.hh.mirishop.activity.comment.dto.CommentRequest;
import com.hh.mirishop.activity.comment.entity.Comment;
import com.hh.mirishop.activity.comment.repository.CommentRepository;
import com.hh.mirishop.activity.common.exception.CommentException;
import com.hh.mirishop.activity.common.exception.ErrorCode;
import com.hh.mirishop.activity.common.exception.PostException;
import com.hh.mirishop.activity.like.repository.LikeRepository;
import com.hh.mirishop.activity.post.entity.Post;
import com.hh.mirishop.activity.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserFeignClient userFeignClient;
    private final NewsfeedFeignClient newsfeedFeignClient;

    /**
     * 유저 검증 후 댓글을 작성하는 메소드
     * 부모 댓글이 없으면 일반 댓글, 있으면 대댓글 생성
     */
    @Override
    @Transactional
    public Long createCommentOrReply(CommentRequest request, Long memberNumber, Long postId) {
        userFeignClient.findMemberByNumber(memberNumber);
        Post post = findPostById(postId);

        // 부모 댓글이 있는지 검증하는 로직
        Comment parentComment = validateAndGetParentComment(request.getParentCommentId());

        Comment comment = Comment.builder()
                .post(post)
                .content(request.getContent())
                .memberNumber(memberNumber)
                .parentComment(parentComment)
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
        createNewsFeedForComment(comment);

        return comment.getCommentId();
    }

    /**
     * 댓글을 검증 후 유저가 단 댓글 확인, 이후 댓글을 삭제하는 메소드
     */
    @Override
    @SoftDelete
    @Transactional
    public void deleteComment(Long commentId, Long currentMemberNumber) {
        Comment comment = findCommentById(commentId);

        checkAuthorizedMember(currentMemberNumber, comment);

        comment.delete(true);
        commentRepository.save(comment);

        deleteNewsFeedForComment(comment);
    }

    private Comment validateAndGetParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }

        Comment parentComment = findParentCommentById(parentCommentId);

        // 대댓글의 대댓글은 불가능(depth를 1로 고정)
        if (parentComment.getParentComment() != null) {
            throw new CommentException(ErrorCode.SUBCOMMENT_NOT_ALLOWED);
        }
        return parentComment;
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findParentCommentById(Long parentCommentId) {
        return commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CommentException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void checkAuthorizedMember(Long currentMemberNumber, Comment comment) {
        if (!Objects.equals(comment.getMemberNumber(), currentMemberNumber)) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }
    }

    private void createNewsFeedForComment(Comment comment) {
        NewsFeedCreate newsFeedCreate = NewsFeedCreate.builder()
                .memberNumber(comment.getMemberNumber())
                .newsFeedType("COMMENT")
                .activityId(comment.getCommentId())
                .targetPostId(comment.getPost().getPostId())
                .createdAt(comment.getCreatedAt())
                .isDeleted(false)
                .build();

        newsfeedFeignClient.createNewsFeed(newsFeedCreate);
    }


    private void deleteNewsFeedForComment(Comment comment) {
        NewsFeedDelete newsFeedDelete = NewsFeedDelete.builder()
                .newsFeedType("COMMENT")
                .activityId(comment.getCommentId())
                .isDeleted(comment.getIsDeleted())
                .build();

        newsfeedFeignClient.deleteNewsFeed(newsFeedDelete);
    }
}
