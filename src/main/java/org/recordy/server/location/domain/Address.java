package org.recordy.server.location.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Embeddable
public class Address {

    private String formatted;
    private String sido;
    private String gugun;
}
