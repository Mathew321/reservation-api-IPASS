package org.hu.reservation.controller;

import org.hu.reservation.dto.AvailableTablesResponse;
import org.hu.reservation.entity.Reservation;
import org.hu.reservation.service.ReservationService;
import org.hu.reservation.SecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityTestConfig.class)
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @MockBean
    private ReservationService reservationService;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetReservations() throws Exception {
        LocalDate reservationDate = LocalDate.of(2024, 6, 25);
        Reservation reservation = new Reservation();
        reservation.setReservationToken("token");
        reservation.setEmail("email@example.com");
        reservation.setNumberOfPersons(4);
        reservation.setReservationTime(reservationDate.atStartOfDay());
        List<Reservation> reservations = Collections.singletonList(reservation);

        Mockito.when(reservationService.getReservationsByDate(reservationDate)).thenReturn(reservations);

        mockMvc.perform(get("/api/admin/reservations")
                .param("reservationDate", "2024-06-25")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationToken", is("token")))
                .andExpect(jsonPath("$[0].email", is("email@example.com")))
                .andExpect(jsonPath("$[0].numberOfPersons", is(4)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetAvailableTables() throws Exception {
        LocalDate date = LocalDate.of(2024, 9, 25);
        AvailableTablesResponse response = new AvailableTablesResponse(8, 4);

        Mockito.when(reservationService.getAvailableTables(date)).thenReturn(response);

        mockMvc.perform(get("/api/admin/reservations/tables")
                .param("date", "2024-09-25")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freeTablesMorning", is(8)))
                .andExpect(jsonPath("$.freeTablesEvening", is(4)));
    }

}
