package org.recordy.server.place.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash("place")
public class PlaceCacheEntity {

    @Id
    private Long id;
    @Indexed
    private String platformId;


    public static PlaceCacheEntity from(Place place) {
        return new PlaceCacheEntity(
                place.getId(),
                place.getPlatformId()
        );
    }
}
