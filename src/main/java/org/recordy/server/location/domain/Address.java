package org.recordy.server.location.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Embeddable
@Getter
public class Address {

    private String formatted;
    private String sido;
    private String gugun;
}
