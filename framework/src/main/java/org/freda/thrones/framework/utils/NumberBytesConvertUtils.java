package org.freda.thrones.framework.utils;

public class NumberBytesConvertUtils {

    /**
     * int to bytes len 4.
     * @param i int
     * @return bytes.
     */
    public static byte[] intToBytes4(int i)
    {
        byte[] myBytes = new byte[4];
        myBytes[3] = (byte) (0xFF & i);
        myBytes[2] = (byte) ((0xFF00 & i) >> 8);
        myBytes[1] = (byte) ((0xFF0000 & i) >> 16);
        myBytes[0] = (byte) ((0xFF000000 & i) >> 24);
        return myBytes;
    }


    /**
     * bytes len 4 to int
     *
     * @param myBytes bytes
     * @return int.
     */
    public static int bytes4ToInt(byte[] myBytes)
    {
        return ((0xFF & myBytes[0]) << 24 | (0xFF & myBytes[1]) << 16
                | (0xFF & myBytes[2]) << 8 | 0xFF & myBytes[3]);
    }

    /**
     * long to bytes len 8
     *
     * @param values
     * @return
     */
    public static byte[] longToBytes8(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    /**
     * bytes len 8 to long
     *
     * @param buffer
     * @return
     */
    public static long bytes8ToLong(byte[] buffer) {
        long  values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8; values|= (buffer[i] & 0xff);
        }
        return values;
    }

}
