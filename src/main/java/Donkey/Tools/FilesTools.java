package Donkey.Tools;

import Donkey.MainClass;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FilesTools {

    private static FilesTools INSTANCE = new FilesTools();
    public static FilesTools getINSTANCE(){return INSTANCE;}



    public String getFileContent(String filePath){
        ClassLoader classLoader = this.getClass().getClassLoader();

        InputStream stream = classLoader.getResourceAsStream(filePath);

        try
        {
            if (stream == null)
            {
                throw new FileNotFoundException("Cannot find file " + filePath);
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))) {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }

            return stringBuilder.toString();
        }
        catch (Exception e) {
            LogManager.getLogger().catching(e);

        }

        return null;
    }
}
