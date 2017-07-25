package com.suntrans.lanzhouwh;

import com.suntrans.lanzhouwh.utils.Encryp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String masterPassword = "a";
        String originalText = "0123456789";
        byte[] text = new byte[]{'0','1','2','3','4','5','6','7','8','9'};
        byte[] password = new byte[]{'a'};
        try {
            String encryptingCode = Encryp.encrypt(masterPassword,originalText);
//          System.out.println("加密结果为 " + encryptingCode);
            System.out.println("加密结果为: "+encryptingCode);
            String decryptingCode = Encryp.decrypt(masterPassword, encryptingCode);
            System.out.println("解密结果为 " + decryptingCode);
//            Log.i("解密结果",decryptingCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}