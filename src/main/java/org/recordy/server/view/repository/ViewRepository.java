package org.recordy.server.view.repository;

import org.recordy.server.view.domain.View;

public interface ViewRepository {

    // command
    View save(View view);

    // query
}
