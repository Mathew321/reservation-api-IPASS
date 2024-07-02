package org.hu.reservation.controller;

import org.hu.reservation.SecurityTestConfig;
import org.hu.reservation.dto.Error;
import org.hu.reservation.dto.ReservationRequest;
import org.hu.reservation.dto.Response;
import org.hu.reservation.dto.Token;
import org.hu.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@Import(SecurityTestConfig.class)
public class ReservationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @MockBean
    private ReservationService reservationService;

    private static final LocalDateTime localDateTime = ZonedDateTime.parse("2024-09-25T18:00:00Z").toLocalDateTime();

    @Test
    void testCreateReservationSuccess() throws Exception {
        ReservationRequest request = new ReservationRequest(localDateTime, 12, "name@mail.com", "06-00-00-00-01", "d0ee6bb0-a147-4f42-9b70-1aafb55eb21e");
        Token token = new Token("d0ee6bb0-a147-4f42-9b70-1aafb55eb21e");

        Mockito.when(reservationService.makeReservation(any(ReservationRequest.class)))
                .thenReturn(new Response<>(null, token));

        ResultActions result = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"2024-09-25T18:00:00Z\", \"persons\": 12, \"email\": \"name@mail.com\", \"phone\": \"06-00-00-00-01\", \"reservationToken\": \"d0ee6bb0-a147-4f42-9b70-1aafb55eb21e\"}"));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("d0ee6bb0-a147-4f42-9b70-1aafb55eb21e"));
    }

    @Test
    void testCreateReservationNotFound() throws Exception {
        ReservationRequest request = new ReservationRequest(localDateTime, 12, "name@mail.com", "06-00-00-00-01", "d0ee6bb0-a147-4f42-9b70-1aafb55eb21e");
        Error error = new Error("Booking is not available");

        Mockito.when(reservationService.makeReservation(any(ReservationRequest.class)))
                .thenReturn(new Response<>(error, null));

        ResultActions result = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"2024-09-25T18:00:00Z\", \"persons\": 12, \"email\": \"name@mail.com\", \"phone\": \"06-00-00-00-01\", \"reservationToken\": \"d0ee6bb0-a147-4f42-9b70-1aafb55eb21e\"}"));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Booking is not available"));
    }

    @Test
    void testCreateReservationServerError() throws Exception {
        ReservationRequest request = new ReservationRequest(localDateTime, 12, "name@mail.com", "06-00-00-00-01", "d0ee6bb0-a147-4f42-9b70-1aafb55eb21e");

        Mockito.when(reservationService.makeReservation(any(ReservationRequest.class)))
                .thenThrow(new RuntimeException("Server error"));

        ResultActions result = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"2024-09-25T18:00:00Z\", \"persons\": 12, \"email\": \"name@mail.com\", \"phone\": \"06-00-00-00-01\", \"reservationToken\": \"d0ee6bb0-a147-4f42-9b70-1aafb55eb21e\"}"));

        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Server unavailable"));
    }

    @Test
    void testDeleteReservationSuccess() throws Exception {
        Mockito.when(reservationService.deleteReservation(anyString()))
                .thenReturn(new Response<>(null, null));

        ResultActions result = mockMvc.perform(delete("/api/reservations")
                .param("reservationToken", "d0ee6bb0-a147-4f42-9b70-1aafb55eb21e"));

        result.andExpect(status().isNoContent());
    }

    @Test
    void testDeleteReservationBadRequest() throws Exception {
        Error error = new Error("Invalid token");

        Mockito.when(reservationService.deleteReservation(anyString()))
                .thenReturn(new Response<>(error, null));

        ResultActions result = mockMvc.perform(delete("/api/reservations")
                .param("reservationToken", "invalid-token"));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid token"));
    }

    @Test
    void testDeleteReservationServerError() throws Exception {
        Mockito.when(reservationService.deleteReservation(anyString()))
                .thenThrow(new RuntimeException("Server error"));

        ResultActions result = mockMvc.perform(delete("/api/reservations")
                .param("reservationToken", "d0ee6bb0-a147-4f42-9b70-1aafb55eb21e"));

        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Server unavailable"));
    }
}
