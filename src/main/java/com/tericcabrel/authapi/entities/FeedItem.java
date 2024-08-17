package com.tericcabrel.authapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_like_count", columnList = "likeCount")
})
@EntityListeners(AuditingEntityListener.class)
public class FeedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String userId; // ID of the user who posted the item

    @Column(nullable = false)
    private String userName;

    @Column
    private String userProfileImage; // URL or base64 encoded image (optional)

    @Lob
    private String caption; // Text content of the post (optional)

    @ElementCollection
    @Column(nullable = false)
    private List<String> mediaUrls; // URLs to images or videos in the post

    private int likeCount;
    private int commentCount;

    private boolean isLikedByCurrentUser; // Track if the current user liked the post (optional)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private boolean isArchived; // Whether the post is archived by the user
}
