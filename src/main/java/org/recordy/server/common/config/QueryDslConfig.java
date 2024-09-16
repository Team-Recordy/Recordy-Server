package org.recordy.server.common.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.SpatialOps;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    private static class RecordyJPQLTemplates extends JPQLTemplates {

        public RecordyJPQLTemplates() {
            add(SpatialOps.BUFFER, "ST_Buffer({0}, {1})");
            add(SpatialOps.CONTAINS, "ST_Contains({0}, {1})");
        }
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(new RecordyJPQLTemplates(), entityManager);
    }
}
