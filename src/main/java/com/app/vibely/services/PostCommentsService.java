package com.app.vibely.services;

import com.app.vibely.entities.Comment;
import com.app.vibely.entities.Post;
import com.app.vibely.entities.User;
import com.app.vibely.exceptions.ResourceNotFoundException;
import com.app.vibely.repositories.CommentRepository;
import com.app.vibely.repositories.PostRepository;
import com.app.vibely.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class PostCommentsService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // ✅ Create a comment
    @Transactional
    public Comment createComment(Integer postId, Integer userId, String text) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not authorized to perform this action"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setText(text);
        comment.setCreatedAt(Instant.now());

        return commentRepository.save(comment);
    }

    // ✅ Delete a comment
    @Transactional
    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException( "Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new BadCredentialsException("User not authorized to perform this action");
        }

        commentRepository.delete(comment);
    }

    // ✅ Get comments of a post with pagination
    public List<Comment> getCommentsByPostId(Integer postId, int page, int size) {
        // Ensure the post exists
        if (!postRepository.existsById(postId)) throw new ResourceNotFoundException("Post not found");


        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);
        return commentPage.getContent();
    }
}
