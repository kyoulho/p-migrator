package io.playce.migrator.util;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.LibType;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class FileUtil {
    private static final Object lock = new Object();

    /**
     * save File.
     */
    public static Path saveFile(MultipartFile multipartFile, String fileName, Path dir) {
        try {
            File directory = dir.toFile();
            if (!directory.exists()) {
                FileUtils.forceMkdir(directory);
            }

            Path file = Path.of(dir.toString(), fileName);
            File target = file.toFile();
            multipartFile.transferTo(target);
            return target.toPath();
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM304F, "MultipartFile write failed.", e);
        }
    }

    public static List<String> getLibraryNamesFromZipfile(File file) {
        List<String> result = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                if (!LibType.isLibrary(name)) continue;

//                int index = name.lastIndexOf(File.separator);
//                if (index > -1) {
//                    result.add(name.substring(index + 1));
//                }
                result.add(name);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static List<String> getLibraryNamesFromSourceFile(Path sourceDir) {
        List<String> result = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(sourceDir)) {
            return stream.filter(Files::isRegularFile)
                    .filter(f -> LibType.isLibrary(f.getFileName().toString()))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean removeFile(File sub) {
        try {
            synchronized (lock) {
                if (sub.exists()) {
                    FileUtils.forceDelete(sub);
                }
            }
            return true;
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM307F, "An error occurred while deleting the file.", e);
        }
    }

    public static boolean removeFile(Path path) {
        return removeFile(path.toFile());
    }

    public static void createDirectoryIfnotExist(Path path) {
        File dir = path.toFile();
        if (!dir.exists()) {
            try {
                FileUtils.forceMkdir(dir);
                log.trace("created directory: {}", dir);
            } catch (IOException e) {
                throw new PlayceMigratorException(ErrorCode.PM372F, "An error occurred while create directory.", e);
            }
        }
    }

    public static Resource getFile(String filePath) {
        try {
            return new UrlResource("file:" + filePath);
        } catch (MalformedURLException e) {
            throw new PlayceMigratorException(ErrorCode.PM303F, "An error occurred while download : " + filePath, e);
        }
    }

    public static String getName(String filePath) {
        return FilenameUtils.getName(filePath);
    }

    public static InputStream getInputStream(String filePath) {
        try {
            return getFile(filePath).getInputStream();
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM371F, "An error occurred reading the file : " + filePath, e);
        }
    }
}
