package io.playce.migrator.analysis.process.async;

import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.MigratorPath;
import io.playce.migrator.dao.entity.DependenciesInfo;
import io.playce.migrator.dao.repository.DependenciesInfoRepository;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class DependenciesInfoAnalyzer {
    private final PlayceMigratorConfig playceMigratorConfig;
    private final DependenciesInfoRepository dependenciesInfoRepository;

    @Async("workerExecutor")
    public void analyzeDependencies(Long applicationId, String applicationOriginFileName, String library) {
        Path libraryPath = Path.of(playceMigratorConfig.getWorkDir(), String.valueOf(applicationId), applicationOriginFileName, MigratorPath.unzip.name(), library);
        String fullPath = libraryPath.toString();
        InputStream inputStream = FileUtil.getInputStream(fullPath);
        String sha1Value;
        try {
            sha1Value = DigestUtils.sha1Hex(inputStream);
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM376F, e);
        }
        DependenciesInfo dependenciesInfo = new DependenciesInfo();
        dependenciesInfo.setSha1Value(sha1Value);
        dependenciesInfo.setFoundAtPath(library);
        dependenciesInfo.setApplicationId(applicationId);

        String description = getDescription(libraryPath);
        if(description != null && description.length() > 65535) {
            description = description.substring(0, 65530) + " ...";
        }
        dependenciesInfo.setDescription(description);

        dependenciesInfoRepository.save(dependenciesInfo);
    }

    private String getDescription(Path libraryPath) {
        String result = null;
        try(ZipFile zipFile = new ZipFile(libraryPath.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if(zipEntry.getName().toUpperCase().endsWith("MANIFEST.MF")) {
                    result = loadDescription(zipFile.getInputStream(zipEntry));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String loadDescription(InputStream inputStream) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while((line = br.readLine()) != null) {
//            if(line.contains("Description")) {
//                sb.append(line).append('\n');
//            }
//        }
//        return sb.toString();
        return new String(inputStream.readAllBytes());
    }
}
