package io.playce.migrator.domain.application.component.upload;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//@Component
public class WarFileValidator implements IFileValidator{
    @Override
    public boolean isSupport(File file) {
        return file.getName().endsWith(".war");
    }

    @Override
    public boolean validate(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                if (name.equals("WEB-INF/web.xml")) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
