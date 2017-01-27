import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;

public class DecryptBlob {
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

    
    public static byte[] hexStringToByteArray(String s) {
	int len = s.length() -1;
	byte[] data = new byte[(len / 2) + 1];
	for (int i = 0; i < len; i += 2) {
	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	}
	return data;
    }



    public static void likeAndroid() throws Exception {
	String mixed = "E6A7C9A6C6A5CCE9CEADC958F405CCBFDEE2D023C613CB76CCEDC2C7D5B5ADAE";
	InputStream is = new FileInputStream("blob.bin");
	OutputStream os = new FileOutputStream("decrypted-out");      

	// decode keys
	byte [] all = DecryptBlob.hexStringToByteArray(mixed);
	int cutlen = all.length / 2;
	byte [] key = new byte [cutlen];
	byte [] iv = new byte [cutlen];

	for (int i=0,j=0; i<all.length-1; i+=2, j++) {
	    key[j]=(byte)(all[i] ^ 0xa7);
	    iv[j]=(byte)(all[i+1] ^ 0xa7);
	}

	// decrypt file
	SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	IvParameterSpec ivspec = new IvParameterSpec(iv);
		
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

	byte[] b = new byte[1024];
	int bytesRead = is.read(b);
	while (bytesRead != -1) {
	    byte [] c = cipher.update(b, 0, bytesRead);
	    if (c != null) {
		os.write(c);
		os.flush();
	    }
	    bytesRead = is.read(b);
	}
	byte [] end = cipher.doFinal();
	if (end != null) {
	    os.write(end);
	    os.flush();
	}
	os.close();
	is.close();
	System.out.println("likeAndroid(): DONE");
    }

    public static void main(String args[]) {
	try {
	    DecryptBlob.likeAndroid();
	    
	}
	catch(Exception exp) {
	    System.out.println("exp: "+exp.toString());
	    exp.printStackTrace();
	}
    }
	
	
}
