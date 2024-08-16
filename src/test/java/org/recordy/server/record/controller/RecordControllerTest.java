package org.recordy.server.record.controller;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @RepeatedTest(1)
    void testGetAllRecords() throws Exception {
        mockMvc.perform(get("/api/v1/records/all"))
                .andExpect(status().isOk());
    }

}
