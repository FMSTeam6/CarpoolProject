package com.finalproject.carpool.services;

import com.finalproject.carpool.repositories.FeedbackRepository;
import com.finalproject.carpool.services.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FeedbackServiceImplsTests {

    @Mock
    FeedbackRepository mockFeedbackRepository;

    @InjectMocks
    FeedbackServiceImpl feedbackService;
}
