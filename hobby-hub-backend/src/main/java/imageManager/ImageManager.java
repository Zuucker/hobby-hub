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

/**
 *
 * @author Zuucker
 */
public class ImageManager {

    private final String savePath = "D:\\Repos\\hobby-hub\\front-end\\public\\media\\avatars\\";
    private final String mediaPath = "D:\\Repos\\hobby-hub\\front-end\\public\\media\\";
    private final String shortPath = "media/avatars/";

    public ImageManager() {
    }

    public void saveBase64toDisk(String image, int id) {
        try {
            String base64Image;
            if (image.contains(",")) {
                base64Image = image.split(",")[1];
            } else {
                base64Image = image;
            }
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            FileOutputStream fileOutPutStream = new FileOutputStream(savePath + String.valueOf(id) + ".jpg");

            fileOutPutStream.write(imageBytes);
            fileOutPutStream.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveBase64toDisk(String image, String name) {
        try {
            String base64Image;
            if (image.contains(",")) {
                base64Image = image.split(",")[1];
            } else {
                base64Image = image;
            }
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            FileOutputStream fileOutPutStream = new FileOutputStream(mediaPath + name + ".jpg");

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

            if (!base64Image.contains("base64")) {
                String newImg = "data:image/png;base64," + base64Image;
                base64Image = newImg;
            }

            return base64Image;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "-1";
    }

    public String readPath(int id) {
        return this.shortPath + String.valueOf(id) + ".jpg";
    }
}
