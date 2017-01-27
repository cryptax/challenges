public class ChallengeSoluce {
    public static byte [] c = new byte[16];
    public static byte [] d = new byte[80];

    static {
    c[0] = -20;
    c[1] = 52;
    c[2] = 39;
    c[3] = 29;
    c[4] = 15;
    c[5] = -105;
    c[6] = 64;
    c[7] = 78;
    c[8] = -11;
    c[9] = -23;
    c[10] = 92;
    c[11] = -94;
    c[12] = -2;
    c[13] = 13;
    c[14] = -124;
    c[15] = 33;

    d[0] = -81;
    d[1] = 91;
    d[2] = 73;
    d[3] = 122;
    d[4] = 125;
    d[5] = -10;
    d[6] = 52;
    d[7] = 61;
    d[8] = -44;
    d[9] = -55;
    d[10] = 24;
    d[11] = -51;
    d[12] = -112;
    d[13] = 121;
    d[14] = -92;
    d[15] = 83;
    d[16] = -119;
    d[17] = 25;
    d[18] = 82;
    d[19] = 110;
    d[20] = 106;
    d[21] = -73;
    d[22] = 1;
    d[23] = 11;
    d[24] = -90;
    d[25] = -55;
    d[26] = 31;
    d[27] = -10;
    d[28] = -84;
    d[29] = 45;
    d[30] = -25;
    d[31] = 78;
    d[32] = -103;
    d[33] = 90;
    d[34] = 83;
    d[35] = 120;
    d[36] = 125;
    d[37] = -28;
    d[38] = 96;
    d[39] = 117;
    d[40] = -36;
    d[41] = -55;
    d[42] = 15;
    d[43] = -57;
    d[44] = -99;
    d[45] = 127;
    d[46] = -31;
    d[47] = 85;
    d[48] = -52;
    d[49] = 119;
    d[50] = 72;
    d[51] = 121;
    d[52] = 106;
    d[53] = -73;
    d[54] = 41;
    d[55] = 61;
    d[56] = -49;
    d[57] = -55;
    d[58] = 110;
    d[59] = -49;
    d[60] = -107;
    d[61] = 107;
    d[62] = -23;
    d[63] = 73;
    d[64] = -34;
    d[65] = 70;
    d[66] = 23;
    d[67] = 117;
    d[68] = 100;
    d[69] = -10;
    d[70] = 43;
    d[71] = 43;
    d[72] = -86;
    d[73] = -124;
    d[74] = 109;
    d[75] = -112;
    d[76] = -51;
    d[77] = 57;
    d[78] = -79;
    d[79] = 23;
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

    public static void main(String args[]) {
	System.out.println("Challenge solution");
	byte [] value = ChallengeSoluce.doXor(c,d);
	System.out.println("Result: "+new String(value));
    }

}
