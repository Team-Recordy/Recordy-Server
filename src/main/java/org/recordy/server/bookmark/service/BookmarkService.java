package org.recordy.server.bookmark.service;

public interface BookmarkService {

    // command
    boolean bookmark(long userId, long recordId);

    // query
    Long countBookmarks(long userId);
}
