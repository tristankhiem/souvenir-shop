package com.sgu.agency.common.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class FileReaderUtil {
    private static final String IMAGE_SAVE_FOLDER = "image/";
    private static final String DEFAULT_IMAGE_ABSOLUTE_PATH = IMAGE_SAVE_FOLDER + "default.png";
    private static final ClassPathResource DEFAULT_IMAGE_CLASS_PATH_RESOURCE = new ClassPathResource(DEFAULT_IMAGE_ABSOLUTE_PATH);

    private static final String IMAGE_BASE64_TYPE = "data:image/png;base64,";

    /**
     * Get the image content in base64 with the associated image absolute path in PNG extension.
     *
     * @return The base64 image content in byte array
     * @throws IOException The exception is returned when the image is not read correctly from the disk.
     * @author bang.ngo
     **/
    @SneakyThrows(IOException.class)
    public byte[] readFileFromDisk(String imageAbsolutePath) {
        byte[] fileContent;
        if (Objects.isNull(imageAbsolutePath)) {
            fileContent = FileUtils.readFileToByteArray(DEFAULT_IMAGE_CLASS_PATH_RESOURCE.getFile());
        } else {
            try {
                File imageSavingFile = new File(IMAGE_SAVE_FOLDER + imageAbsolutePath);
                fileContent = FileUtils.readFileToByteArray(imageSavingFile);
            } catch (FileNotFoundException e) {
                fileContent = FileUtils.readFileToByteArray(DEFAULT_IMAGE_CLASS_PATH_RESOURCE.getFile());
            }
        }
        return fileContent;
    }

    /**
     * Get the converted base64 image content from the file content in byte array. (Data Conversion)
     *
     * @return The image base64 type name with the associated base64 image data.
     * @author bang.ngo
     **/
    public String getConvertedBase64ImageContentFromImageByteContent(byte[] fileContent) {
        return IMAGE_BASE64_TYPE + Base64.getEncoder().encodeToString(fileContent);
    }



    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
