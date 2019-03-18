package Donkey.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Base64 {

    public static String encodeFileToBase64Binary(File file) throws IOException {
        String encodedfile;
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = new String(java.util.Base64.getEncoder().encode(bytes));

        return encodedfile;
    }
}
