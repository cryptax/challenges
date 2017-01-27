import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;

public class SecretEncrypt {
    // AnakinSkywalker
    public static byte [] KEY = { (byte)0x41, (byte)0x6e, (byte)0x61, (byte)0x6b,
			(byte)0x69, (byte)0x6e, (byte)0x53, (byte)0x6b,
			(byte)0x79, (byte)0x77, (byte)0x61, (byte)0x6c,
			(byte)0x6b, (byte)0x65, (byte)0x72, (byte)0x0a };

    public static byte [] IV = { 0, 1, 2, 78, 0xa, (byte)0xff, (byte)0xa2, 0x18, 0x45, (byte)0x84, (byte)0xb4, (byte)0xd1, (byte)0x4a, (byte)0x60, (byte)0x12, 0x9 };


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

    public static void print(byte [] input) {
	if (input == null) return;

	System.out.print( "byte [] ciphertext = { ");
	for (int i=0; i<input.length; i++) {
	    System.out.print( "(byte) 0x"+ Integer.toHexString(input[i] & 0xff) );
	    if (i == input.length -1) {
		System.out.println(" };");
	    } else {
		System.out.print(", ");
		if ((i+1) % 4 == 0) {
		    System.out.println("");
		}
	    }
	}
    }

    
    public static byte[] hexStringToByteArray(String s) {
	int len = s.length() -1;
	byte[] data = new byte[(len / 2) + 1];
	for (int i = 0; i < len; i += 2) {
	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	}
	return data;
    }

    public static String encodeKeys(byte [] key, byte [] iv) {
	StringBuffer s = new StringBuffer();
	for (int i=0; i<key.length; i++) {
	    s.append( SecretEncrypt.hexArray[0xff & (key[i] ^ 0xa7) ] );
	    s.append( SecretEncrypt.hexArray[0xff & (iv[i] ^ 0xa7)] );
	}
	return s.toString();
    }

    // start = 0 for key
    // start = 1 for iv
    public static byte [] decodeKeys(String s, int start) {
	byte [] all = hexStringToByteArray(s);
	byte [] key = new byte [all.length / 2];
	for (int i=start,j=0; i< all.length; i+=2, j++) {
	    key[j]=(byte)(all[i] ^ 0xa7);
	}
	return key;
    }

    public static byte [] readFile(String filename) throws Exception {
	FileInputStream is = new FileInputStream(filename);
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	byte[] b = new byte[1024];
	int bytesRead;
	while ((bytesRead = is.read(b)) != -1) {
	    bos.write(b, 0, bytesRead);
	}
	byte[] bytes = bos.toByteArray();
	return bytes;
    }

    public static byte [] sha256(String input) throws Exception {
	MessageDigest digest;
	digest = MessageDigest.getInstance("SHA-256");
	digest.reset();
	byte [] computedHash = digest.digest(input.getBytes());
	return computedHash;
    }

	
    public static void encrypt_file(String keyv, String clearfile, String encryptedfile) throws Exception {

	System.out.println("Encrypting "+clearfile);
	SecretKeySpec skeySpec = new SecretKeySpec(SecretEncrypt.decodeKeys(keyv, 0), "AES");
	IvParameterSpec ivspec = new IvParameterSpec(SecretEncrypt.decodeKeys(keyv, 1) );
	
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

	FileInputStream is = new FileInputStream(clearfile);
	FileOutputStream os = new FileOutputStream(encryptedfile);
	
	byte[] b = new byte[1024];
	int bytesRead = is.read(b);
	int totalRead = 0;
	while (bytesRead != -1) {
	    totalRead += bytesRead;
	    System.out.println("We read: current chunk="+bytesRead+" total="+totalRead);
	    byte [] c = cipher.update(b, 0, bytesRead);
	    if (c != null) {
		System.out.println("We are writting: "+c.length);
		os.write(c);
		os.flush();
	    }
	    bytesRead = is.read(b);
	}
	byte [] end = cipher.doFinal();
	if (end != null) {
	    System.out.println("We are writting final bytes: "+end.length);
	    SecretEncrypt.print(end);
	    os.write(end);
	    os.flush();
	}
	os.close();
	is.close();
    }

    public static void decrypt_file(String keyv, String encryptedfile, String clearfile) throws Exception {
	System.out.println("Decrypting "+encryptedfile);
	SecretKeySpec skeySpec = new SecretKeySpec(SecretEncrypt.decodeKeys(keyv, 0), "AES");
	IvParameterSpec ivspec = new IvParameterSpec(SecretEncrypt.decodeKeys(keyv, 1) );
	
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

	FileInputStream is = new FileInputStream(encryptedfile);
	FileOutputStream os = new FileOutputStream(clearfile);
	
	byte[] b = new byte[1024];
	int bytesRead = is.read(b);
	int totalRead = 0;
	while (bytesRead != -1) {
	    totalRead += bytesRead;
	    System.out.println("Decrypt: we have read "+bytesRead+" bytes. Total="+totalRead);
	    
	    byte [] c = cipher.update(b, 0, bytesRead);
	    if (c != null) {
		os.write(c);
		os.flush();
	    }
	    bytesRead = is.read(b);
	}
	byte [] end = cipher.doFinal();
	if (end != null) {
	    System.out.println("We are writing final bytes: "+end.length);
	    os.write(end);
	    os.flush();
	}
	os.close();
	is.close();
    }
	
    
    public static byte [] encrypt_decrypt(byte [] input) throws Exception {
	SecretKeySpec skeySpec = new SecretKeySpec(SecretEncrypt.KEY, "AES");
	IvParameterSpec ivspec = new IvParameterSpec(SecretEncrypt.IV);
	
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
	
	// encrypt the message
	byte[] encrypted = cipher.doFinal(input);
	SecretEncrypt.print(encrypted);
	
	// reinitialize the cipher for decryption
	cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
	
	// decrypt the message
	byte[] decrypted = cipher.doFinal(encrypted);
	System.out.println("Plaintext: " + new String(decrypted) + "\n");

	return encrypted;

    }

    public static void likeAndroid() throws Exception {
	String mixed = "E6A7C9A6C6A5CCE9CEADC958F405CCBFDEE2D023C613CB76CCEDC2C7D5B5ADAE";
	InputStream is = new FileInputStream("encrypted-ascii");
	OutputStream os = new FileOutputStream("decrypted-out");      

	// decode keys
	byte [] all = SecretEncrypt.hexStringToByteArray(mixed);
	int cutlen = all.length / 2;
	byte [] key = new byte [cutlen];
	byte [] iv = new byte [cutlen];

	for (int i=0,j=0; i<all.length-1; i+=2, j++) {
	    key[j]=(byte)(all[i] ^ 0xa7);
	    iv[j]=(byte)(all[i+1] ^ 0xa7);
	}
	SecretEncrypt.print(key);
	SecretEncrypt.print(iv);

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
	    //byte [] hash = sha256("MayTheF0rceB3W1thU");
	    //System.out.println("hash: ");
	    //SecretEncrypt.print(hash);

	    //SecretEncrypt.print(hexStringToByteArray("301c4cd0097640bdbfe766b55924c0d5c5cc28b9f2bdab510e4eb7c442ca0c66"));
	    /*System.out.println("key: ");
	    SecretEncrypt.print(SecretEncrypt.KEY);

	    System.out.println("iv: ");
	    SecretEncrypt.print(SecretEncrypt.IV);
	    */

	    String s = SecretEncrypt.encodeKeys(SecretEncrypt.KEY, SecretEncrypt.IV);
	    System.out.println("Mixed: "+s);
	    /*
	    
	    byte [] all = hexStringToByteArray(s);
	    byte [] k = new byte[all.length /2];
	    byte [] iv = new byte[all.length /2];
	    for (int i=0; i<all.length/2; i+=2) {
		System.out.println("i="+i);
		k[i]=(byte)(all[i] ^ 0xa7);
		iv[i]=(byte)(all[i+1] ^ 0xa7);
	    }
	    System.out.println("key: ");
	    SecretEncrypt.print(k);
	    System.out.println("iv: ");
	    SecretEncrypt.print(iv); 
	    */
	    
	    /*
	    byte [] k = SecretEncrypt.decodeKeys(s, 0);
	    System.out.println("key: ");
	    SecretEncrypt.print(k);

	    byte [] iv = SecretEncrypt.decodeKeys(s, 1);
	    System.out.println("iv: ");
	    SecretEncrypt.print(iv); 
	    */

	    //SecretEncrypt.encrypt_file(s, "./asciiart.txt", "./encrypted-ascii");
	    //SecretEncrypt.decrypt_file(s, "./encrypted-ascii", "./decrypted-ascii.txt"); 

	    //byte [] message = readFile("./asciiart.txt");
	    //encrypt_decrypt(message);

	    SecretEncrypt.likeAndroid();
	    
	}
	catch(Exception exp) {
	    System.out.println("exp: "+exp.toString());
	    exp.printStackTrace();
	}
    }
	
	
}
