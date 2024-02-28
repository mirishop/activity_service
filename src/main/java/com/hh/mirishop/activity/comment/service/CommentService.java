package com.hh.mirishop.activity.comment.service;

import com.hh.mirishop.activity.comment.dto.CommentRequest;

public interface CommentService {

    Long createCommentOrReply(CommentRequest request, Long memberNumber, Long postId);

    void deleteComment(Long commentId, Long memberNumber);
}

