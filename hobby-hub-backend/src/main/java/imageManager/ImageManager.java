/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imageManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import org.springframework.util.Base64Utils;

/**
 *
 * @author Zuucker
 */
public class ImageManager {

    private final String savePath = "D:\\Repos\\hobby-hub\\DB\\images\\";

    public ImageManager() {
    }

    public void saveBase64toDisk(String image, int id) {
        try {
            String base64Image = image.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            FileOutputStream fileOutPutStream = new FileOutputStream(savePath + String.valueOf(id) + ".jpg");

            fileOutPutStream.write(imageBytes);
            fileOutPutStream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String readImagetoBase64(int id) {
        try {
            byte[] imageBytes = Files.readAllBytes(Path.of(savePath + String.valueOf(id) + ".jpg"));

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            return base64Image;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "-1";
    }
}
