package com.fortiguard.insomnihack2012.challenge;

import android.util.Log;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Compute
{
  private static final byte[] c1;
  private static final byte[] c2;
  private static final byte[] ivBytes;
  public static byte[] keyBytes;
  private static final byte[] secretHash;

  static
  {
    byte[] arrayOfByte1 = new byte[16];
    arrayOfByte1[1] = 1;
    arrayOfByte1[2] = 2;
    arrayOfByte1[3] = 3;
    arrayOfByte1[5] = 1;
    arrayOfByte1[6] = 2;
    arrayOfByte1[7] = 3;
    arrayOfByte1[9] = 1;
    arrayOfByte1[10] = 2;
    arrayOfByte1[11] = 3;
    arrayOfByte1[13] = 1;
    arrayOfByte1[14] = 2;
    arrayOfByte1[15] = 3;
    keyBytes = arrayOfByte1;
    secretHash = new byte[] { 97, 82, 88, 126, 222, 138, 38, 245, 63, 211, 145, 176, 85, 212, 222, 80, 30, 232, 178, 73, 127, 231, 79, 143, 214, 159, 44, 114, 226, 243, 227, 122 };
    c1 = new byte[] { 236, 52, 39, 29, 15, 151, 64, 78, 245, 233, 92, 162, 254, 13, 132, 33 };
    c2 = new byte[] { 175, 91, 73, 122, 125, 246, 52, 61, 212, 201, 24, 205, 144, 121, 164, 83, 137, 25, 82, 110, 106, 183, 1, 11, 166, 201, 31, 246, 172, 45, 231, 78, 153, 90, 83, 120, 125, 228, 96, 117, 220, 201, 15, 199, 157, 127, 225, 85, 204, 119, 72, 121, 106, 183, 41, 61, 207, 201, 110, 207, 149, 107, 233, 73, 222, 70, 23, 117, 100, 246, 43, 43, 170, 132, 109, 144, 205, 57, 177, 23 };
    byte[] arrayOfByte2 = new byte[16];
    arrayOfByte2[1] = 1;
    arrayOfByte2[2] = 2;
    arrayOfByte2[3] = 3;
    arrayOfByte2[5] = 1;
    arrayOfByte2[6] = 2;
    arrayOfByte2[7] = 3;
    arrayOfByte2[15] = 1;
    ivBytes = arrayOfByte2;
  }

  public static boolean checkSecret(String paramString)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
      localMessageDigest.reset();
      byte[] arrayOfByte1 = paramString.getBytes();
      byte[] arrayOfByte2 = localMessageDigest.digest(arrayOfByte1);
      boolean bool = Arrays.equals(secretHash, arrayOfByte2);
      if (bool)
      {
        i = 1;
        return i;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder("checkSecret: ");
        String str1 = localException.toString();
        String str2 = str1;
        int j = Log.w("InsomniDroid", str2);
        int i = 0;
      }
    }
  }

  public static void compute(byte[] paramArrayOfByte)
  {
    try
    {
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte, "AES");
      byte[] arrayOfByte1 = ivBytes;
      IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte1);
      Cipher localCipher = Cipher.getInstance("AES/CTR/NoPadding");
      localCipher.init(2, localSecretKeySpec, localIvParameterSpec);
      byte[] arrayOfByte2 = c1;
      byte[] arrayOfByte3 = localCipher.doFinal(arrayOfByte2);
      byte[] arrayOfByte4 = new byte[c2.length];
      int i = 0;
      while (true)
      {
        int j = c2.length;
        if (i >= j)
          return;
        localCipher.init(2, localSecretKeySpec, localIvParameterSpec);
        byte[] arrayOfByte5 = c2;
        int k = i;
        int m = localCipher.doFinal(arrayOfByte5, i, 16, arrayOfByte4, k);
        i += 16;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder("compute: ");
        String str1 = localException.toString();
        String str2 = str1;
        int n = Log.w("InsomniDroid", str2);
      }
    }
  }
}

/* Location:           /home/axelle/prog/challenges/insomnihack-2012/insomnidroid.apk-40ab64f9076a3992dfea853cd052d3b8299a1ea8/insomnidroid.apk.dex2jar.jar
 * Qualified Name:     com.fortiguard.insomnihack2012.challenge.Compute
 * JD-Core Version:    0.6.0
 */