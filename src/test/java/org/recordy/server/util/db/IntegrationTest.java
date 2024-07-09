package org.recordy.server.util.db;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class IntegrationTest {
}
