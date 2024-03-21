package com.finalproject.carpool.controllers;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.FeedbackMapper;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.FeedbackRequest;
import com.finalproject.carpool.services.FeedbackService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static com.finalproject.carpool.TestHelpers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackRestControllerTests {
    @MockBean
    FeedbackService mockFeedbackService;

    @MockBean
    FeedbackMapper mockFeedbackMapper;

    @MockBean
    AuthenticationHelper mockAuthenticationHelper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void create_Should_ReturnStatusUnauthorized_When_AuthorizationIsMissing() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act, Assert
        String body = toJson(createFeedbackRequest());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/feedbacks/{travelId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void create_Should_ReturnStatusBadRequest_When_BodyIsInvalid() throws Exception {
        // Arrange
        FeedbackRequest request = createFeedbackRequest();
        request.setText(null);

        // Act, Assert
        String body = toJson(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/feedbacks/{travelId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void update_Should_ReturnStatusBadRequest_When_BodyIsInvalid() throws Exception {
        // Arrange
        FeedbackRequest request = createFeedbackRequest();
        request.setRating(0);

        // Act, Assert
        String body = toJson(request);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/feedbacks/{feedbackId}/{travelId}", 1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void update_Should_ReturnStatusUnauthorized_When_UserIsNotAuthorizedToEdit() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Feedback mockFeedback = createMockFeedback();
        Travel mockTravel = createMockTravel();

        Mockito.when(mockFeedbackMapper.fromRequest(Mockito.anyInt(), Mockito.any()))
                .thenReturn(mockFeedback);

        Mockito.doThrow(UnauthorizedOperationException.class)
                .when(mockFeedbackService)
                .update(mockFeedback, mockUser, mockTravel);

        // Act, Assert
        String body = toJson(createFeedbackRequest());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/feedbacks/{feedbackId}/{travelId}", 1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void delete_Should_ReturnStatusOk_When_CorrectRequest() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/feedbacks/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_When_AuthorizationIsMissing() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/feedbacks/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void delete_Should_ReturnStatusNotFound_When_FeedbackDoesNotExist() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class)
                .when(mockFeedbackService)
                .delete(1, mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/feedbacks/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_When_UserIsNotAuthorizedToDelete() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Mockito.doThrow(UnauthorizedOperationException.class)
                .when(mockFeedbackService)
                .delete(1, mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/feedbacks/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}