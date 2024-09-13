package io.playce.migrator.domain.application.component.upload;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EjbJarFileValidator implements IFileValidator {

    @Override
    public boolean isSupport(File file) {
        return file.getName().endsWith(".jar");
    }

    @Override
    public boolean validate(File file) {
        return true;
//        try (ZipFile zipFile = new ZipFile(file)) {
//            Enumeration<? extends ZipEntry> entries = zipFile.entries();
//            while (entries.hasMoreElements()) {
//                ZipEntry zipEntry = entries.nextElement();
//                String name = zipEntry.getName();
//                if (name.equals("META-INF/ejb-jar.xml")) {
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
    }
}
