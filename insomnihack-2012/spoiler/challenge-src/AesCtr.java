import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Security;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCtr {
     private static String bytesToHexString(byte[] bytes) {
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

    public static void dumpHexTab(byte[] tab) {
	System.out.println("byte [] tab = new byte [] {");
	for (int i=0; i<tab.length; i++) {
	    if (i > 0 && (i % 4 == 0)) 
		System.out.println("\n");
	    System.out.print("(byte)"+tab[i]);
	    if (i<tab.length -1) 
		System.out.print(", ");
	}
	System.out.println("};");
    }

    public static byte [] doXor(byte [] a, byte [] b) {
	byte [] c = new byte[b.length];
	int i, j;
	for (i=0,j=0; j<b.length; i++,j++) {
	    if (i >= a.length) i = 0;
	    c[j] = (byte) (a[i] ^ b[j]);
	}
	return c;
    }

    public static void sha256(byte [] input) {
	try {
	    MessageDigest digest;
	    digest = MessageDigest.getInstance("SHA-256");
	    digest.reset();
	    byte [] hash = digest.digest(input);
	    System.out.println("Digest: "+bytesToHexString(hash));
	    System.out.println("Hash: ");
	    dumpHexTab(hash);
	    System.out.println("Hash end");
	    
	} catch (Exception exp) {
	    System.out.println("sha256 failed: "+exp.toString());
	}
    }

    

    public static void main(String[] args) throws Exception {
      try {
	  byte [] block1 = new byte[16];
	  byte [] block2 = "HELLO FROM FORTI".getBytes();
	  byte [] block3 = "Congrats! Dont re-use AES CTR counters ;) Secret Code is: 2mkfmh2r0hkake_m123456".getBytes();
	  byte [] keyBytes = new byte[] { 
	      (byte)0xa0, (byte)0xb1, (byte)0xc2, (byte)0x33, (byte)0x74, (byte)0xf5, (byte)0xd6, (byte)0x87, 
	      (byte)0xa8, (byte)0x19,(byte) 0x0a, (byte)0x0b, (byte)0x1c, (byte)0x0d, (byte)0x3e, (byte)0x2f };
	  byte[] ivBytes = new byte[] { 
	      (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, 
	      (byte)0x00, (byte)0x00,(byte) 0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01 };
	  
	  SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	  IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	  Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	  byte [] ciphertext1 = new byte [16];
	  byte [] ciphertext2 = new byte [16];
	  byte [] ciphertext3 = new byte [block3.length];

	  // encryption pass
	  cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	  ciphertext2 = cipher.doFinal(block2);
	  cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	  ciphertext1 = cipher.doFinal(block1);

	  for (int i=0; i<block3.length; i+=16) {
	      cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	      System.out.println("i="+i+" length="+block3.length);
	      cipher.doFinal(block3, i, 16, ciphertext3, i);
	  }

	  // 
	  System.out.println("Block 1: "+new String(block1));
	  dumpHexTab(block1);
	  System.out.println("Encrypted block 1:");
	  dumpHexTab(ciphertext1);

	  System.out.println("Block 2: "+new String(block2));
	  dumpHexTab(block2);
	  System.out.println("Encrypted block 2:");
	  dumpHexTab(ciphertext2);

	  System.out.println("Block 3: "+new String(block3));
	  dumpHexTab(block3);
	  System.out.println("Encrypted block 3:");
	  dumpHexTab(ciphertext3);
	  System.out.println("size of ciphertext3: "+ciphertext3.length);

	  byte [] c = AesCtr.doXor(ciphertext1, ciphertext2);
	  System.out.println("XOR compute: "+new String(c));
	  dumpHexTab(c);

	  byte [] d = AesCtr.doXor(ciphertext1, ciphertext3);
	  System.out.println("XOR compute: "+new String(d));
	  dumpHexTab(d);

	  sha256("2mkfmh2r0hkake_m123456".getBytes());

	  byte [] p3 = new byte[ciphertext3.length];
	  for (int i=0; i<ciphertext3.length; i+=16) {
	      cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
	      cipher.doFinal(ciphertext3, i, 16, p3, i);
	  }
	  System.out.println("Decrypted: "+new String(p3));

      }
      catch(Exception exp) {
	  System.out.println("Exception: "+exp.toString());
      }
      

  }
}
