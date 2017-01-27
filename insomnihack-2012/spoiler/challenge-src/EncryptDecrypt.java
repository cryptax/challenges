import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptDecrypt {
     public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
	try {
	    byte [] nulls = new byte [16];
	    byte [] keyBytes = new byte[] { 
	      (byte)0xa0, (byte)0xb1, (byte)0xc2, (byte)0x33, (byte)0x74, (byte)0xf5, (byte)0xd6, (byte)0x87, 
	      (byte)0xa8, (byte)0x19,(byte) 0x0a, (byte)0x0b, (byte)0x1c, (byte)0x0d, (byte)0x3e, (byte)0x2f };
	    byte[] ivBytes = new byte[] { 
	      (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, 
	      (byte)0x00, (byte)0x00,(byte) 0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01 };

	    for (int i=0; i< nulls.length; i++) 
		nulls[i] = (byte) 0;

	    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

	    byte [] ciphertext1 = new byte [16];
	    byte [] ciphertext2 = new byte [16];

	    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	    ciphertext1 = cipher.doFinal(nulls);
	    System.out.println("Encrypt mode: "+bytesToHexString(ciphertext1));

	    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
	    ciphertext2 = cipher.doFinal(nulls);
	    System.out.println("Decrypt mode: "+bytesToHexString(ciphertext2));

	}
	catch(Exception exp) {
	    System.out.println("Exception: "+exp.toString());
	}

    }
}
