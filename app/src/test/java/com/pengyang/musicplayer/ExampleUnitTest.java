package com.pengyang.musicplayer;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        try {
            String decoder = URLDecoder.decode("\u5982\u6cb3", "UTF-8");
            System.out.print(decoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}