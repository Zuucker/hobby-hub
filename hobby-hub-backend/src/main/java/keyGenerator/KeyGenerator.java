/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package keyGenerator;

import java.util.Random;
import java.security.SecureRandom;

/**
 *
 * @author Zuucker
 */
public class KeyGenerator {

    public String generateKey(int length) {
        String key = "";

        Random random = new SecureRandom();

        for (int i = 0; i < length; i++) {

            key += (char) ('A' + random.nextInt(26));
        }

        return key;
    }

}
