package com.tericcabrel.authapi.controllers;

import com.tericcabrel.authapi.dtos.AddFeedItemDTO;
import com.tericcabrel.authapi.dtos.UpdateFeedItemDTO;
import com.tericcabrel.authapi.entities.FeedItem;
import com.tericcabrel.authapi.services.FeedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public ResponseEntity<Page<FeedItem>> listFeedItems( @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FeedItem> feedItems = feedService.listFeedItems(pageable);
        return ResponseEntity.ok(feedItems);
    }

    // Search feed items with pagination
    @GetMapping("/search")
    public ResponseEntity<Page<FeedItem>> searchFeedItems(
            @RequestParam("keyword") String keyword,
            Pageable pageable) {
        Page<FeedItem> feedItems = feedService.searchFeedItems(keyword, pageable);
        return ResponseEntity.ok(feedItems);
    }

    // Find feed items by date range with pagination
    @GetMapping("/date-range")
    public ResponseEntity<Page<FeedItem>> findFeedItemsByDateRange(
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            Pageable pageable) {
        Page<FeedItem> feedItems = feedService.findFeedItemsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(feedItems);
    }

    // Find top 10 feed items by like count
    @GetMapping("/top")
    public ResponseEntity<List<FeedItem>> findTop10FeedItems() {
        List<FeedItem> feedItems = feedService.findTop10FeedItems();
        return ResponseEntity.ok(feedItems);
    }

    // Find feed items with media URLs, sorted by the provided sort parameter
    @GetMapping("/with-media")
    public ResponseEntity<List<FeedItem>> findFeedItemsWithMediaUrls(Sort sort) {
        List<FeedItem> feedItems = feedService.findFeedItemsWithMediaUrls(sort);
        return ResponseEntity.ok(feedItems);
    }

    // Add a new FeedItem using AddFeedItemDTO
    @PostMapping("/add")
    public ResponseEntity<FeedItem> addFeedItem(
            @Valid @RequestBody AddFeedItemDTO addFeedItemDTO,
            HttpServletRequest request) {

        FeedItem savedFeedItem = feedService.addFeedItem(addFeedItemDTO, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedItem);
    }

    // Update an existing FeedItem using AddFeedItemDTO
    @PutMapping("/update/{id}")
    public ResponseEntity<FeedItem> updateFeedItem(
            @PathVariable("id") int id,
            @Valid @RequestBody UpdateFeedItemDTO updateFeedItemDTO,
            HttpServletRequest request) {
        FeedItem updatedItem = feedService.updateFeedItem(id, updateFeedItemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    // Delete a FeedItem by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFeedItem(@PathVariable("id") int id) {
        feedService.deleteFeedItem(id);
        return ResponseEntity.noContent().build();
    }
}
