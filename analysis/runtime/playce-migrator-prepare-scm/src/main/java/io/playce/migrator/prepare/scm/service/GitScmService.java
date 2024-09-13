package io.playce.migrator.prepare.scm.service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.IScmCredentialService;
import io.playce.migrator.prepare.IScmPrepareService;
import io.playce.migrator.util.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Service("GIT")
@Slf4j
public class GitScmService implements IScmCredentialService, IScmPrepareService {

    @Override
    public String validateCredential(String url, String id, String pw) {
        try {
            Map<String, Ref> stringRefMap = new LsRemoteCommand(null)
                    .setRemote(url)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(id, pw))
                    .callAsMap();
            return stringRefMap.get("HEAD").getTarget().getName().replace("refs/heads/", "");
        } catch (GitAPIException e) {
            throw new PlayceMigratorException(ErrorCode.PM802S, e);
        }
    }

    @Override
    public String validateCredential(String url, String prvKey) {
        try {
            Map<String, Ref> stringRefMap = new LsRemoteCommand(null)
                    .setRemote(url)
                    .setTransportConfigCallback(new SshTransportConfigCallback(prvKey))
                    .callAsMap();
            return stringRefMap.get("HEAD").getTarget().getName().replace("refs/heads/", "");
        } catch (GitAPIException e) {
            throw new PlayceMigratorException(ErrorCode.PM802S, e);
        }
    }

    @Override
    public void cloneRepository(String url, String branch, String id, String pw, Path targetDir) {
        PathUtils.removeTargetDir(targetDir);
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setBranch(branch)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(id, pw))
                    .setDirectory(targetDir.toFile())
                    .call()
                    .close();
        } catch (GitAPIException e) {
            throw new PlayceMigratorException(ErrorCode.PM803S, e);
        }
    }

    @Override
    public void cloneRepository(String url, String branch, String prvKey, Path targetDir) {
        PathUtils.removeTargetDir(targetDir);
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setBranch(branch)
                    .setTransportConfigCallback(new SshTransportConfigCallback(prvKey))
                    .setDirectory(targetDir.toFile())
                    .call()
                    .close();
        } catch (GitAPIException e) {
            throw new PlayceMigratorException(ErrorCode.PM803S, e);
        }
    }

    @RequiredArgsConstructor
    private static class SshTransportConfigCallback implements TransportConfigCallback {
        private final String prvKey;

        private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("kex", "diffie-hellman-group-exchange-sha1,diffie-hellman-group14-sha1,diffie-hellman-group1-sha1");
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch jSch = super.createDefaultJSch(fs);
                File tempFile = getTempFile();
                jSch.addIdentity(tempFile.getAbsolutePath());
                return jSch;
            }
        };

        private File getTempFile() {
            File tempFile;
            try {
                tempFile = File.createTempFile("tmpPrvFile", "");
                FileWriter fileWriter = new FileWriter(tempFile);
                fileWriter.write(prvKey);
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return tempFile;
        }

        @Override
        public void configure(Transport transport) {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        }

    }
}
