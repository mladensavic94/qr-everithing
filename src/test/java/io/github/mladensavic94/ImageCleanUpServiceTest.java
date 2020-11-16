package io.github.mladensavic94;

import io.github.mladensavic94.services.ImageCleanUpService;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.inject.Inject;
import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@QuarkusTest
class ImageCleanUpServiceTest {

    @ConfigProperty(name = "content.folder.address")
    String contentFolder;
    @Inject
    ImageCleanUpService service;

    @Test
    void deleteImagesOlderThanDay() throws Exception {
        var oldFile = new File(contentFolder + "oldFile.png");
        if(!oldFile.exists())
            oldFile.createNewFile();
        oldFile.setLastModified(new Date().toInstant().minus(3, ChronoUnit.DAYS).toEpochMilli());
        var newFile = new File(contentFolder + "newFile.png");
        if(!newFile.exists())
            newFile.createNewFile();

        service.cleanupOldImages();

        Assert.assertTrue(newFile.exists());
        Assert.assertFalse(oldFile.exists());
    }
}
