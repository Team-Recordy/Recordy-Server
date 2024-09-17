package org.recordy.server.common.util.data.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record Response(
        @JacksonXmlProperty(localName = "comMsgHeader")
        ComMsgHeader comMsgHeader,

        @JacksonXmlProperty(localName = "msgBody")
        MsgBody msgBody
) {}

