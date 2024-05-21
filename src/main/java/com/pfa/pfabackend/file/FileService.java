package com.pfa.pfabackend.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Service
@RequiredArgsConstructor
public class FileService {

    // Method to check if the base64 string is a valid image
    public static boolean checkIfImage(String base64String) {
        // Check if the base64 string starts with a valid image header
        return StringUtils.startsWithIgnoreCase(base64String, "data:image/");
    }

    // Method to convert base64 string to image file
    public File convertToImage(String base64String, long id) throws IOException {
        // Decode base64 string to byte array
        byte[] imageBytes = Base64.getDecoder().decode(base64String.split(",")[1]);

        // Extract image type from base64 string
        String imageType = base64String.split(";")[0].split("/")[1];
        if (!isValidImageType(imageType)) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        // Generate file name
        String fileName = System.currentTimeMillis() + "_" + id + "." + imageType;

        // Create file object with the specified file name
        File imageFile = new File(fileName);

        // Write byte array to file
        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            outputStream.write(imageBytes);
        }

        return imageFile;
    }

    // Method to validate supported image types
    private boolean isValidImageType(String imageType) {
        return imageType.equals("jpeg") || imageType.equals("jpg") ||
                imageType.equals("png") || imageType.equals("bmp") ||
                imageType.equals("webp");
    }

    public String uploadToFolder(File imageFile, String folderPath) throws IOException {
        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Copy the image file to the specified folder
        FileUtils.copyFileToDirectory(imageFile, folder);
        String path = folderPath + "/" + imageFile.getName();
        return path;
    }

    // Method to get the image type from a base64 string
    public String getImageType(String base64Image) {
        String[] parts = base64Image.split(",");
        String mimeType = parts[0].split(":")[1].split(";")[0];
        return mimeType;
    }

    // Method to check if the image URL is valid
    private static boolean isValidImageUrl(String imageUrl) {
        // Regular expression pattern to match "https://drive.google.com/thumbnail?id="
        // followed by any characters
        String regex = "^https:\\/\\/drive\\.google\\.com\\/thumbnail\\?id=.*$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(imageUrl);

        // Check if the image URL matches the pattern
        return matcher.matches();
    }

}
