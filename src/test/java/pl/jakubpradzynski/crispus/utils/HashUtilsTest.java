package pl.jakubpradzynski.crispus.utils;

import org.junit.Assert;
import org.junit.Test;
import pl.jakubpradzynski.crispus.exceptions.HashGenerationException;

public class HashUtilsTest {

    @Test
    public void generateCorrectMD5HashTest() throws HashGenerationException {
        String message = "test";
        String correctHash = "098f6bcd4621d373cade4e832627b4f6";
        String generatedHash = HashUtils.generateMD5(message);
        Assert.assertTrue(correctHash.equals(generatedHash));
    }

    @Test
    public void generateCorrectSHA1HashTest() throws HashGenerationException {
        String message = "test";
        String correctHash = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";
        String generatedHash = HashUtils.generateSHA1(message);
        Assert.assertTrue(correctHash.equals(generatedHash));
    }

    @Test
    public void generateCorrectSHA256HashTest() throws HashGenerationException {
        String message = "test";
        String correctHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String generatedHash = HashUtils.generateSHA256(message);
        Assert.assertTrue(correctHash.equals(generatedHash));
    }

}
