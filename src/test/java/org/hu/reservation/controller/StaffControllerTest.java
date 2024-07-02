package org.hu.reservation.controller;

import org.hu.reservation.SecurityTestConfig;
import org.hu.reservation.dto.Error;
import org.hu.reservation.dto.StaffId;
import org.hu.reservation.entity.StaffInfo;
import org.hu.reservation.service.StaffInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StaffController.class)
@Import(SecurityTestConfig.class)
public class StaffControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @MockBean
    private StaffInfoService staffInfoService;

    @Value("${security.reservation.api-key}")
    private String apiKey;

    @Test
    void testAddNewStaffMemberSuccess() throws Exception {
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setName("John Doe");
        staffInfo.setEmail("john.doe@example.com");
        staffInfo.setPassword("password");
        staffInfo.setRoles("ROLE_USER");

        when(staffInfoService.addStaffMemeber(any(StaffInfo.class))).thenReturn(1);

        ResultActions result = mockMvc.perform(post("/api/staff")
                .header("X-Reservation-Api-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password\", \"roles\": \"ROLE_USER\"}"));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.staffId").value(1));
    }

    @Test
    void testAddNewStaffMemberForbidden() throws Exception {
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setName("John Doe");
        staffInfo.setEmail("john.doe@example.com");
        staffInfo.setPassword("password");
        staffInfo.setRoles("ROLE_USER");

        ResultActions result = mockMvc.perform(post("/api/staff")
                .header("X-Reservation-Api-Key", "invalid-api-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password\", \"roles\": \"ROLE_USER\"}"));

        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Operation forbidden"));
    }

    @Test
    void testAddNewStaffMemberServerError() throws Exception {
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setName("John Doe");
        staffInfo.setEmail("john.doe@example.com");
        staffInfo.setPassword("password");
        staffInfo.setRoles("ROLE_USER");

        when(staffInfoService.addStaffMemeber(any(StaffInfo.class))).thenThrow(new RuntimeException("Database error"));

        ResultActions result = mockMvc.perform(post("/api/staff")
                .header("X-Reservation-Api-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password\", \"roles\": \"ROLE_USER\"}"));

        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Server unavailable"))
                .andExpect(jsonPath("$.detailedMessage").value("Database error"));
    }
}
