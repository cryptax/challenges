import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCounter {
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

    public static void main(String args[]) {
	try {
	    int BLOCK = 10;
	    byte [] plaintext = new byte[16*BLOCK];
	    byte [] keyBytes = new byte[] { 
	      (byte)0xa0, (byte)0xb1, (byte)0xc2, (byte)0x33, (byte)0x74, (byte)0xf5, (byte)0xd6, (byte)0x87, 
	      (byte)0xa8, (byte)0x19,(byte) 0x0a, (byte)0x0b, (byte)0x1c, (byte)0x0d, (byte)0x3e, (byte)0x2f };
	    byte [] ivBytes = new byte[16];
	    byte [] ciphertext = new byte[16*BLOCK];
	    
	    for (int i=0; i< ivBytes.length; i++) 
		ivBytes[i] = (byte) 0;

	    for (int j=0; j< plaintext.length; j++) 
		plaintext[j] = (byte) j;

	    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

	    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	    for (int i=0; i< BLOCK; i++) {
		System.out.println("i="+i+" IV: "+bytesToHexString(cipher.getIV()));
		cipher.update(plaintext, i*16, 16, ciphertext, i*16);
		System.out.println("ciphertext: "+bytesToHexString(ciphertext));
		System.out.println("");
	    }

	}
	catch(Exception exp) {
	    System.out.println("Exception: "+exp.toString());
	}
    }
}
