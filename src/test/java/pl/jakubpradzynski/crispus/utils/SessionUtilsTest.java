package pl.jakubpradzynski.crispus.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpSession;

public class SessionUtilsTest {

    @Test
    public void isUserSessionActiveTest() {
        HttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("username", "test");
        Assert.assertTrue(SessionUtils.isUserSessionActive(httpSession));
    }

    @Test(expected = SessionExpiredException.class)
    public void isUserSessionActiveNegativeTest() {
        HttpSession httpSession = new MockHttpSession();
        Assert.assertTrue(SessionUtils.isUserSessionActive(httpSession));
    }

}
