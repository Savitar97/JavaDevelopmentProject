package com.epam.training.ticketservice.ui.config.prompt;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CustomPromptProviderTest {
    CustomPromptProvider underTest;

    @Test
    public void testShouldReturnCorrectPromp(){
        //Given
        underTest=new CustomPromptProvider();
        //When
        AttributedString attributedString = underTest.getPrompt();
        //Then
        Assertions.assertEquals(attributedString, new AttributedString("Ticket service>", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)));
    }

}