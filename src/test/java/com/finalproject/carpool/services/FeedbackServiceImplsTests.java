package com.finalproject.carpool.services;

import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.repositories.FeedbackRepository;
import com.finalproject.carpool.services.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static com.finalproject.carpool.TestHelpers.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FeedbackServiceImplsTests {

    @Mock
    FeedbackRepository mockFeedbackRepository;

    @InjectMocks
    FeedbackServiceImpl feedbackService;

    @Test
    void get_Should_CallPostRepository() {
        // Arrange
        Mockito.when(mockFeedbackRepository.getAllFeedbacks())
                .thenReturn(null);

        // Act
        feedbackService.getAllFeedbacks();

        // Assert
        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .getAllFeedbacks();
    }

    @Test
    public void get_Should_ReturnFeedback_When_FeedbackIdExist() {
        // Arrange
        Feedback mockFeedback = createMockFeedback();

        Mockito.when(mockFeedbackRepository.get(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        // Act
        Feedback result = feedbackService.get(mockFeedback.getId());

        // Assert
        Assertions.assertEquals(mockFeedback, result);
    }

    @Test
    void update_Should_CallRepository_When_UserIsAuthor() {
        // Arrange
        Feedback mockFeedback = createMockFeedback();
        User mockUserCreator = mockFeedback.getAuthorId();
        Travel mockTravel = createMockTravel();

        Mockito.when(mockFeedbackRepository.get(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        // Act
        feedbackService.update(mockFeedback, mockUserCreator, mockTravel);

        // Assert
        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .updateFeedback(mockFeedback);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingFeedback() {
        // Arrange
        Feedback mockFeedback = createMockFeedback();
        User mockUserCreator = mockFeedback.getAuthorId();
        Travel mockTravel = createMockTravel();

        Mockito.when(mockFeedbackRepository.get(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        // Act
        feedbackService.update(mockFeedback, mockUserCreator, mockTravel);

        // Assert
        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .updateFeedback(mockFeedback);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotAuthor() {
        // Arrange
        Feedback mockFeedback = createMockFeedback();

        Mockito.when(mockFeedbackRepository.get(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        // Act, Assert
        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> feedbackService.update(mockFeedback, Mockito.mock(User.class), Mockito.mock(Travel.class)));

    }

    @Test
    void delete_Should_CallRepository_When_UserIsAuthor() {
        // Arrange
        Feedback mockFeedback = createMockFeedback();
        User mockUserCreator = mockFeedback.getAuthorId();

        Mockito.when(mockFeedbackRepository.get(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        // Act
        feedbackService.delete(1, mockUserCreator);

        // Assert
        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .deleteFeedback(1);
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Feedback mockFeedback = createMockFeedback();
        mockFeedback.setId(1);


        Mockito.when(mockFeedbackRepository.get(1))
                .thenReturn(mockFeedback);

        // Act
        feedbackService.delete(1, mockUserAdmin);

        // Assert
        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .deleteFeedback(1);
    }

    @Test
    void delete_Should_ThrowException_When_UserIsNotAdminOrAuthor() {
        // Arrange
        Feedback mockFeedback = createMockFeedback();
        Travel mockTravel = createMockTravel();

        Mockito.when(mockFeedbackRepository.get(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        User mockUser = Mockito.mock(User.class);

        // Act, Assert
        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> feedbackService.delete(1, mockUser));
    }
}
