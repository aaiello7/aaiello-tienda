package tienda.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageService {
    @Value("${firebase.bucket.name}")
    private String bucketName;
    @Value("${firebase.storage.path}")
    private String storagePath;
    // Aquí se manejaría la inyección del cliente de Storage como un bean
    private final Storage storage;

    public FirebaseStorageService(Storage storage) {
        this.storage = storage;
    }

    //Sube un archivo de imagen al almacenamiento de Firebase.    
    public String uploadImage(MultipartFile localFile, String folder, Long id) throws IOException {
        String originalName = localFile.getOriginalFilename();
        String fileExtension = "";
        if (originalName != null && originalName.contains(".")) {
            fileExtension = originalName.substring(originalName.lastIndexOf("."));
        }

        // Se genera el nombre del archivo con un formato consistente.
        String fileName = "img" + getFormattedNumber(id) + fileExtension;

        File tempFile = convertToFile(localFile);

        try {
            return uploadToFirebase(tempFile, folder, fileName);
        } finally {
            // Asegura que el archivo temporal se elimine siempre.
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    //Convierte un MultipartFile a un archivo temporal en el servidor.
     private File convertToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("upload-", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    //Sube el archivo al almacenamiento de Firebase y genera una URL firmada.     
    private String uploadToFirebase(File file, String folder, String fileName) throws IOException {
        // Definimos el ID del blob y su información
        BlobId blobId = BlobId.of(bucketName, storagePath + "/" + folder + "/" + fileName);
        String mimeType = Files.probeContentType(file.toPath());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(mimeType != null ? mimeType : "media").build();

        // Subimos el archivo. El objeto `storage` ya tiene las credenciales necesarias.
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // El objeto `storage` ya tiene las credenciales del servicio configuradas        
        // Se genera la URL firmada. Ahora con una caducidad de 5 años.
        return storage.signUrl(blobInfo, 1825, TimeUnit.DAYS).toString();
    }

    /**
     * Genera un string numérico con un formato de 14 dígitos, rellenado con
     * ceros a la izquierda.
     */
    private String getFormattedNumber(long id) {
        return String.format("%014d", id);
    }
}
