package com.tericcabrel.authapi.repositories;

import com.tericcabrel.authapi.entities.FeedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeedRepository extends PagingAndSortingRepository<FeedItem, Integer>, CrudRepository<FeedItem, Integer> {

    // Search for FeedItems based on a keyword in the caption or userName with pagination
    @Query("SELECT f FROM FeedItem f JOIN FETCH f.mediaUrls WHERE f.caption LIKE %:keyword% OR f.userName LIKE %:keyword%")
    Page<FeedItem> searchFeedItems(@Param("keyword") String keyword, Pageable pageable);

    // Find FeedItems created between two dates with pagination
    @Query("SELECT f FROM FeedItem f JOIN FETCH f.mediaUrls WHERE f.createdAt BETWEEN :startDate AND :endDate")
    Page<FeedItem> findByCreatedAtBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    // Find the top 10 FeedItems ordered by like count in descending order
    @Query("SELECT f FROM FeedItem f JOIN FETCH f.mediaUrls ORDER BY f.likeCount DESC")
    List<FeedItem> findTop10ByOrderByLikeCountDesc();

    // Find FeedItems that have media URLs with sorting
    @Query("SELECT f FROM FeedItem f JOIN FETCH f.mediaUrls WHERE f.mediaUrls IS NOT NULL")
    List<FeedItem> findByMediaUrlsIsNotNull(Sort sort);

    @Query("SELECT f FROM FeedItem f JOIN FETCH f.mediaUrls")
    Page<FeedItem> findAllWithMediaUrls(Pageable pageable);
}

