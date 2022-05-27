package app.homsai.engine.media.infrastructure.storage;

import app.homsai.engine.media.domain.exceptions.StorageException;
import app.homsai.engine.media.domain.exceptions.StorageFileNotFoundException;
import app.homsai.engine.media.domain.services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    @Value("${media.path}")
    private String path;

    private Path rootLocation;

    @Override
    public void store(MultipartFile file, String fileName) {
        rootLocation = Paths.get(path);
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            if (file.isEmpty()) {
                throw new StorageException(
                        "Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void store(BufferedImage file, String fileName, String extension) {
        rootLocation = Paths.get(path);
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            ImageIO.write(file, extension, this.rootLocation.resolve(fileName).toFile());
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        rootLocation = Paths.get(path);
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        rootLocation = Paths.get(path);
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        rootLocation = Paths.get(path);
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        rootLocation = Paths.get(path);
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void deleteFile(String filename) {
        rootLocation = Paths.get(path);
        try {
            FileSystemUtils.deleteRecursively(new File(rootLocation.toFile() + "/" + filename));
            FileSystemUtils
                    .deleteRecursively(new File(rootLocation.toFile() + "/" + filename + "-FHD"));
            FileSystemUtils
                    .deleteRecursively(new File(rootLocation.toFile() + "/" + filename + "-HD"));
            FileSystemUtils
                    .deleteRecursively(new File(rootLocation.toFile() + "/" + filename + "-thumb"));
        } catch (Exception e) {
            throw new StorageException("Failed to delete files");
        }
    }

    @Override
    public void init() {
        rootLocation = Paths.get(path);
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
