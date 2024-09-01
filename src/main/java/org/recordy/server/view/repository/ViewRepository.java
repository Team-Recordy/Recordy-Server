package org.recordy.server.view.repository;

import org.recordy.server.view.domain.View;

import java.util.List;

public interface ViewRepository {

    // command
    View save(View view);
    void deleteByUserId(long userId);

    // query
    List<View> findAllByUserId(long userId);
}
