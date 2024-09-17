package org.recordy.server.common.util.data.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record ComMsgHeader(
        @JacksonXmlProperty(localName = "RequestMsgID")
        String requestMsgID,

        @JacksonXmlProperty(localName = "ResponseTime")
        String responseTime,

        @JacksonXmlProperty(localName = "ResponseMsgID")
        String responseMsgID,

        @JacksonXmlProperty(localName = "SuccessYN")
        String successYN,

        @JacksonXmlProperty(localName = "ReturnCode")
        String returnCode,

        @JacksonXmlProperty(localName = "ErrMsg")
        String errMsg
) {}

