package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.AddFeedItemDTO;
import com.tericcabrel.authapi.dtos.UpdateFeedItemDTO;
import com.tericcabrel.authapi.entities.FeedItem;
import com.tericcabrel.authapi.repositories.FeedRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }


    public Page<FeedItem> listFeedItems(Pageable pageable) {
        Page<FeedItem> feedItems = feedRepository.findAllWithMediaUrls(pageable);
        return feedItems;
    }


    // Add a new FeedItem
    public FeedItem addFeedItem(AddFeedItemDTO addFeedItemDTO, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token missing or invalid");
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractUserId(token);

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token: userId not found");
        }

        // Create new FeedItem from DTO
        FeedItem feedItem = new FeedItem();
        feedItem.setCaption(addFeedItemDTO.getCaption());
        feedItem.setMediaUrls(addFeedItemDTO.getMediaUrls());
        feedItem.setUserId(userId);
        feedItem.setUserName(jwtService.extractUserName(token)); // You need to implement this method
        feedItem.setCreatedAt(new Date());
        feedItem.setUpdatedAt(new Date());

        return feedRepository.save(feedItem);
    }

    // Update an existing FeedItem
    public FeedItem updateFeedItem(int id, UpdateFeedItemDTO updateFeedItemDTO) {
        Optional<FeedItem> existingFeedItemOpt = feedRepository.findById(id);
        if (existingFeedItemOpt.isPresent()) {
            FeedItem existingFeedItem = existingFeedItemOpt.get();

            if (updateFeedItemDTO.getCaption() != null) {
                existingFeedItem.setCaption(updateFeedItemDTO.getCaption());
            }

            if (updateFeedItemDTO.getMediaUrls() != null) {
                existingFeedItem.setMediaUrls(updateFeedItemDTO.getMediaUrls());
            }

            if (updateFeedItemDTO.getUserProfileImage() != null) {
                existingFeedItem.setUserProfileImage(updateFeedItemDTO.getUserProfileImage());
            }

            existingFeedItem.setLikeCount(updateFeedItemDTO.getLikeCount());
            existingFeedItem.setCommentCount(updateFeedItemDTO.getCommentCount());
            existingFeedItem.setLikedByCurrentUser(updateFeedItemDTO.isLikedByCurrentUser());
            existingFeedItem.setArchived(updateFeedItemDTO.isArchived());
            existingFeedItem.setUpdatedAt(new Date());

            return feedRepository.save(existingFeedItem);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feed item not found");
        }
    }



    // Delete a FeedItem by ID
    public void deleteFeedItem(int id) {
        if (feedRepository.existsById(id)) {
            feedRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feed item not found");
        }
    }


    // Search FeedItems with keyword and pagination
    public Page<FeedItem> searchFeedItems(String keyword, Pageable pageable) {
        return feedRepository.searchFeedItems(keyword, pageable);
    }

    // Find FeedItems by date range with pagination
    public Page<FeedItem> findFeedItemsByDateRange(Date startDate, Date endDate, Pageable pageable) {
        return feedRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    // Find top 10 FeedItems ordered by like count
    public List<FeedItem> findTop10FeedItems() {
        return feedRepository.findTop10ByOrderByLikeCountDesc();
    }

    // Find FeedItems with media URLs
    public List<FeedItem> findFeedItemsWithMediaUrls(Sort sort) {
        return feedRepository.findByMediaUrlsIsNotNull(sort);
    }
}
