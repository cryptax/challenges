package com.fortiguard.challenge.hashdays2012.challengeapp;

import java.security.MessageDigest;
import android.util.Log;
import java.util.Arrays;
import java.io.*;
import android.content.res.AssetManager;
import android.content.Context;
import android.os.Environment;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Validate {
    private static final String [] hashes = {
	"622a751d6d12b46ad74049cf50f2578b871ca9e9447a98b06c21a44604cab0b4", // echo -n "MayTheF0rceB3W1thU" | sha256sum
	"301c4cd0097640bdbfe766b55924c0d5c5cc28b9f2bdab510e4eb7c442ca0c66", // echo -n "AnakinSkywalker" | sha256sum
	"d09e1fe7c97238c68e4be7b3cd64230c638dde1d08c656a1c9eaae30e49c4caf", // echo -n "Fortiguard" | sha256sum
	"4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2", // echo -n "root" | sha256sum
    };

    private static final String [] answers = {
	"Congrats from the FortiGuard team :)",
	"Nice try, but that would be too easy",
	"Ha! Ha! FortiGuard grin ;)",
	"Are you implying we are n00bs?",
	"Come on, this is a DEFCON conference!"
    };

    public static String[] hexArray = {
	"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
	"10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
	"20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
	"30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
	"40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
	"50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
	"60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
	"70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
	"80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
	"90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
	"A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
	"B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
	"C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
	"D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
	"E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
	"F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"};

    private static byte [][] bh = new byte[4][32];
    private static boolean computed = false;

    private Context context;

    public Validate(Context appCtx) {
	context = appCtx;
    }


    public static byte[] hexStringToByteArray(String s) {
	int len = s.length() -1;
	byte[] data = new byte[(len / 2) + 1];
	for (int i = 0; i < len; i += 2) {
	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	}
	return data;
    }

    public static void convert2bytes() {
	for (int i=0; i< hashes.length; i++) {
	    bh[i] = hexStringToByteArray(hashes[i]);
	}
	computed = true;
    }

    public static String checkSecret(String input) {
    	try {
    		MessageDigest digest;
		digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		byte [] computedHash = digest.digest(input.getBytes());

		if (! computed) {
		    convert2bytes();
		}

		for (int i=0; i<hashes.length; i++) {
		    if (Arrays.equals(computedHash, bh[i])) {
    			return answers[i];
		    }
		}

    	}
    	catch(Exception exp){
    		Log.w("Hashdays", "checkSecret: "+exp.toString());
    	}
	return answers[4];
    }

    public static boolean isEmulator() {
	return true;
    }

    public void work() {
	String mixed = "E6A7C9A6C6A5CCE9CEADC958F405CCBFDEE2D023C613CB76CCEDC2C7D5B5ADAE";
	String assetname = "blob.bin";
	AssetManager am = context.getAssets();
	byte[] b = new byte[1024];

	byte [] all = Validate.hexStringToByteArray(mixed);
	int cutlen = all.length / 2;
	byte [] key = new byte [cutlen];
	byte [] iv = new byte [cutlen];

	for (int i=0,j=0; i<all.length-1; i+=2,j++) {
	    key[j]=(byte)(all[i] ^ 0xa7);
	    iv[j]=(byte)(all[i+1] ^ 0xa7);
	}

	try {
	    InputStream is = am.open(assetname);
	    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "whatever");
	    OutputStream os = new FileOutputStream(file);      

	    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	    IvParameterSpec ivspec = new IvParameterSpec(iv);
		
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    digest.reset();
	    
	    int bytesRead = is.read(b);
	    while (bytesRead != -1) {
		byte [] c = cipher.update(b, 0, bytesRead);
		digest.update(b, 0, bytesRead);
		if (c != null) {
		    os.write(c);
		    os.flush();
		}
		bytesRead = is.read(b);
	    }

	    byte [] hash = digest.digest();
	    StringBuffer st = new StringBuffer();
	    for (int i=0; i<hash.length; i++) {
		st.append( Validate.hexArray[0xff & (hash[i]) ] );
	    }
	    if (st.toString().compareToIgnoreCase("528f83083b5df4fee78d582ed2463973c585e67ddc711dcde765e2c10b598156") !=0) {
		throw new IOException("Bad SHA-256 for "+assetname);
	    }

	    byte [] end = cipher.doFinal();
	    if (end != null) {
		os.write(end);
		os.flush();
	    }
	    os.close();
	    is.close();
	}
	catch(Exception exp) {
	    Log.e("Hashdays", exp.toString());
	}
    }
    
}
