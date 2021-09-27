package rozaryonov.shipping.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrytedPasswordUtils {

    // Encryte Password with BCryptPasswordEncoder
    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    // Encryte Password with PasswordEncoder
    public static String encrytePassword2(String password) {
        return PasswordEncoder.getHash(password);
    }

    
    

    public static void main(String[] args) {
        String password = "123456"; 
        // $2a$10$ciIP5FMAuaea39dkcYUvDu6TY5qxnn64k5iZ65KPPG0L0htUJWdP. //123
        // $2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq //123456
        String encrytedPassword = encrytePassword(password);

        System.out.println("Encryted Password: |" + encrytedPassword +"|" );
    }
}
