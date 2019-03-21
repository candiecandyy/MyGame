package program;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by lyh on 2017/12/22.
 */
public class CyptoUtil {

    public static String encode(String str) {
        byte[] b = Base64.encodeBase64(str.getBytes());
        return new String(b);
    }

    public static String decode(String str){
        byte[] b = Base64.decodeBase64(str.getBytes());
        return new String(b);
    }
}
