package org.recordy.server.common.util.data.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record MsgBody(
        @JacksonXmlProperty(localName = "totalCount")
        int totalCount,

        @JacksonXmlProperty(localName = "cPage")
        int cPage,

        @JacksonXmlProperty(localName = "rows")
        int rows,

        @JacksonXmlProperty(localName = "realmCode")
        int realmCode,

        @JacksonXmlProperty(localName = "sido")
        String sido,

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "perforList")
        List<PerforList> perforList
) {}

