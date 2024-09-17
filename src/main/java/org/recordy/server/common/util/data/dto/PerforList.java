package org.recordy.server.common.util.data.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record PerforList(
        @JacksonXmlProperty(localName = "seq")
        int seq,

        @JacksonXmlProperty(localName = "title")
        String title,

        @JacksonXmlProperty(localName = "startDate")
        String startDate,

        @JacksonXmlProperty(localName = "endDate")
        String endDate,

        @JacksonXmlProperty(localName = "place")
        String place,

        @JacksonXmlProperty(localName = "realmName")
        String realmName,

        @JacksonXmlProperty(localName = "area")
        String area,

        @JacksonXmlProperty(localName = "thumbnail")
        String thumbnail,

        @JacksonXmlProperty(localName = "gpsX")
        String gpsX,

        @JacksonXmlProperty(localName = "gpsY")
        String gpsY
) {}

