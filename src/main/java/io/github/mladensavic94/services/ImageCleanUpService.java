package io.github.mladensavic94.services;

import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ImageCleanUpService {

    private static final Logger LOGGER = Logger.getLogger(ImageCleanUpService.class.getName());

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;
    @ConfigProperty(name = "cleanup.file.age")
    long durationInHours;


    @Scheduled(cron = "{cleanup.cron}")
    void cleanupOldImages() {
        LOGGER.info("Starting cleanup job");
        var root = new File(contentFolder);
        if (root.exists()) {
            File[] filesToDelete = root.listFiles(pathname -> {
                LocalDateTime localDateTime = Instant.ofEpochMilli(pathname.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                Duration between = Duration.between(localDateTime, LocalDateTime.now());
                var old = between.toHours() > durationInHours;
                return pathname.isFile() && old;
            });
            if (filesToDelete != null && filesToDelete.length > 0){
                Optional<Boolean> reduce = Arrays.stream(filesToDelete).map(File::delete).reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2);
                if (reduce.isPresent() && reduce.get())
                    LOGGER.info("All old images deleted");
                else
                    LOGGER.error("Some files from list are not deleted " + Arrays.stream(filesToDelete).map(File::getName).collect(Collectors.joining(", ")));
            }else {
                LOGGER.info("Nothing to delete!");
            }
        }
    }

}
