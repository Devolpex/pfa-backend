package com.pfa.pfabackend.file;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

@Service
public class GoogleDriveService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    private static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "credential.json");
        return filePath.toString();
    }

    public Map<String, String> uploadImageToDrive(File file, String mimeType) throws GeneralSecurityException, IOException {
        try {
            String folderId = "1oUMlDgUJmIOujqhkLgd9ezOM9GvfU1-0";

            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();

            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent(mimeType, file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/thumbnail?id=" + uploadedFile.getId() + "&sz=w1000";
            System.out.println("IMAGE URL: " + imageUrl);
            file.delete();
            Map<String, String> imageInfos = Map.of("imageUrl", imageUrl, "fileId", uploadedFile.getId());
            return imageInfos;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Map<String, String> error = Map.of("error", e.getMessage());
            return error;
        }
    }

    public String deleteImageFromDrive(String fileId) throws GeneralSecurityException, IOException {
        try {
            Drive drive = createDriveService();
            drive.files().delete(fileId).execute();
            return "File deleted successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


    private Drive createDriveService() throws GeneralSecurityException, IOException {

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();

    }

}
