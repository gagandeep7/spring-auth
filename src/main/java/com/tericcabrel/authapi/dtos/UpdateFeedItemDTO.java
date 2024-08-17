package com.tericcabrel.authapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFeedItemDTO {

    @Size(max = 255, message = "Caption cannot exceed 255 characters")
    private String caption;

    @NotEmpty(message = "At least one media URL is required")
    private List<String> mediaUrls;

    private String userProfileImage;
    private boolean isArchived;
    private boolean isLikedByCurrentUser;
    private int likeCount;
    private int commentCount;
}
