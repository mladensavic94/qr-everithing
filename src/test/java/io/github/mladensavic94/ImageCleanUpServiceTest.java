package io.github.mladensavic94;

import io.github.mladensavic94.services.ImageCleanUpService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.inject.Inject;
import java.io.File;
import java.lang.reflect.Field;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@QuarkusTest
class ImageCleanUpServiceTest {

    ImageCleanUpService service = new ImageCleanUpService();
    String contentFolderPath = "src/test/resources/images/cleanup/";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteImagesOlderThan3Days() throws Exception {
        Field contentFolder = ImageCleanUpService.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(service, contentFolderPath);

        var oldFile = new File(contentFolderPath + "oldFile.png");
        oldFile.createNewFile();
        oldFile.setLastModified(new Date().toInstant().minus(3, ChronoUnit.DAYS).toEpochMilli());
        var newFile = new File(contentFolderPath + "newFile.png");
        newFile.createNewFile();

        service.cleanupOldImages();

        assertTrue(newFile.exists());
        assertFalse(oldFile.exists());
    }

    @Test
    void testRootDoesNotExists() throws Exception {
        Field contentFolder = ImageCleanUpService.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(service, contentFolderPath + "/notExists");

        service.cleanupOldImages();

        assertFalse(new File(contentFolderPath + "/notExists").exists());

    }
}
