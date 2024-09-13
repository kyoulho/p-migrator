package io.playce.migrator.migration.process.service;

import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GenerateRequest;
import io.playce.migrator.migration.generator.GeneratorName;
import io.playce.migrator.migration.generator.IResultGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultGeneratorService {
    private final Map<String, IResultGenerator> generatorMap;
    private final Map<ApplicationType, List<IResultGenerator>> chainMap = new EnumMap<>(ApplicationType.class);

    @PostConstruct
    private void initChain() {
        chainMap.put(ApplicationType.ZIP, initZip());
        chainMap.put(ApplicationType.SCM, initZip());
        chainMap.put(ApplicationType.JAR, initBinary());
        chainMap.put(ApplicationType.WAR, initBinary());
        chainMap.put(ApplicationType.EAR, initBinary());
    }

    private List<IResultGenerator> initZip() {
        List<IResultGenerator> chainForZip = new ArrayList<>();
        chainForZip.add(generatorMap.get(GeneratorName.ZIP_GEN.name()));
        addComponent(GeneratorName.BUILD_GEN, chainForZip);
        addComponent(GeneratorName.DOCKERFILE_GEN, chainForZip);
        addComponent(GeneratorName.DOCKER_IMAGE_GEN, chainForZip);
        return chainForZip;
    }

    private void addComponent(GeneratorName gen, List<IResultGenerator> list) {
        IResultGenerator generator = generatorMap.get(gen.name());
        if(generator != null) {
            list.add(generator);
        }
    }

    private List<IResultGenerator> initBinary() {
        List<IResultGenerator> chainForBinary = new ArrayList<>();
        chainForBinary.add(generatorMap.get(GeneratorName.ZIP_GEN.name()));
        addComponent(GeneratorName.DOCKERFILE_GEN, chainForBinary);
        addComponent(GeneratorName.DOCKER_IMAGE_GEN, chainForBinary);
        return chainForBinary;
    }

    public Map<String, Path> generator(GenerateRequest request) {
        ApplicationType applicationType = request.getApplicationType();
        Map<String, Path> resultPathMap = new HashMap<>();

        try {
            for (IResultGenerator i : chainMap.get(applicationType)) {
                if (i.isSupported(request)) {
                    GeneratorName generatorName = i.getName();
                    log.info("Migration Result Generator Run [{}]", generatorName);
                    Path resultPath = i.generate(request);
                    log.debug("Finish generate [{}] - [{}]", generatorName, resultPath);
                    request.setBeforeResultPath(resultPath);
                    resultPathMap.put(generatorName.name(), resultPath);
                }
            }
        } catch (PlayceMigratorException e) {
            if(!resultPathMap.isEmpty()) {
                log.debug("migration result: {}", resultPathMap);
            }
            throw e;
        }
        return resultPathMap;
    }
}
