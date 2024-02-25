package com.finalproject.carpool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;
import com.finalproject.carpool.models.requests.FeedbackRequest;

import java.time.LocalDateTime;

public class TestHelpers {
    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("TestSubject");
        mockUser.setPassword("TestPassword");
        mockUser.setFirstName("TestFirstName");
        mockUser.setLastName("TestLastName");
        mockUser.setEmail("test@test.com");
        return mockUser;
    }

    public static Travel createMockTravel() {
        Travel mockTravel = new Travel();
        mockTravel.setId(1);
        mockTravel.setEmptySeats(3);
        mockTravel.setDriverId(createMockUser());
        mockTravel.setPricePerPerson(5.0);
        mockTravel.setStartingLocation("TestStartingLocation");
        mockTravel.setEndLocation("TestEndLocation");
        mockTravel.setDateOfDeparture(LocalDateTime.now());
        return mockTravel;
    }

    public static Feedback createMockFeedback() {
        Feedback mockFeedback = new Feedback();
        mockFeedback.setId(1);
        mockFeedback.setAuthorId(createMockUser());
        mockFeedback.setRecipientId(createMockUser());
        mockFeedback.setText("This is a text with a testing purpose");
        mockFeedback.setRating(5);
        return mockFeedback;
    }

    public static SearchUser createMockSearchUser() {
        LocalDateTime now = LocalDateTime.now();
        return new SearchUser(
                "username",
                "email",
                "phoneNumber",
                "sortBy",
                "sortOrder"
        );
    }

    public static FeedbackRequest createFeedbackRequest() {
        FeedbackRequest request = new FeedbackRequest();
        request.setText("This is a content with a testing purpose and nothing more");
        request.setRating(5);
        return request;
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
