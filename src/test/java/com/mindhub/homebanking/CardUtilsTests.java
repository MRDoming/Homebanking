/*package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class CardUtilsTests {

    String cardNumber = CardUtils.getCardNumber();
    int cvv = CardUtils.getCvv();
    int cvv2 = CardUtils.getCvv();


    @Test
    public void cardNumberIsCreated(){
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberIsNotNull(){
        assertThat(cardNumber, is(notNullValue()));
    }

    @Test
    public void cardNumberContainsString(){
        assertThat(cardNumber, containsString("-"));
    }

    @Test
    public void cvvIsCreated(){
        assertThat(cvv, is(not(0)));
    }

    @Test
    public void cvvIsNotCvv2(){
        assertThat(cvv, is(not(cvv2)));
    }

}*/
