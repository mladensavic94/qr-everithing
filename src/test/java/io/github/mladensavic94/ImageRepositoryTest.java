package io.github.mladensavic94;

import io.github.mladensavic94.domain.ImageScale;
import io.github.mladensavic94.domain.QREverythingException;
import io.github.mladensavic94.repositories.ImageRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mladen Savic (mladensavic94@gmail.com)
 */
@QuarkusTest
class ImageRepositoryTest {

    String contentFolderPath = "src/test/resources/images/";
    ImageRepository repository = new ImageRepository();

    @Test
    void testResolveDimensionsFromScale() throws Exception {
        Method method = ImageRepository.class.getDeclaredMethod("resolveDimensions", ImageScale.class, int.class);
        method.setAccessible(true);
        int result = (int) method.invoke(repository, ImageScale.SMALL, 100);

        assertThat(200, equalTo(result));
    }

    @Test
    void testResolveDimensionsByDefaultValue() throws Exception {
        Method method = ImageRepository.class.getDeclaredMethod("resolveDimensions", ImageScale.class, int.class);
        method.setAccessible(true);
        int result = (int) method.invoke(repository, ImageScale.CUSTOM, 100);

        assertThat(100, equalTo(result));
    }

    @Test
    void testImageScaling() throws Exception {
        Field contentFolder = ImageRepository.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(repository, contentFolderPath);

        Method method = ImageRepository.class.getDeclaredMethod("scaleImage", String.class, File.class, int.class);
        method.setAccessible(true);
        File returnFile = new File(contentFolderPath + "name2.png");
        File image = (File) method.invoke(repository, "name.png", returnFile, 100);

        assertThat(image, is(notNullValue()));
        assertThat(image.length(), equalTo(returnFile.length()));
        assertThat(image.length(), not(equalTo(new File(contentFolderPath + "name.png").length())));
    }

    @Test
    void testImageScalingWillFail() throws Exception {
        Field contentFolder = ImageRepository.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(repository, contentFolderPath);

        Method method = ImageRepository.class.getDeclaredMethod("scaleImage", String.class, File.class, int.class);
        method.setAccessible(true);
        File returnFile = new File(contentFolderPath + "name2.png");
        assertThrows(InvocationTargetException.class,
                () -> method.invoke(repository, "non_existingImage.png", returnFile, 100));
    }

    @Test
    void testWillRetrieveExistingImage() throws Exception{
        Field contentFolder = ImageRepository.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(repository, contentFolderPath);
        File file = repository.retrieveImage("name.png", ImageScale.MEDIUM);

        assertThat("name_medium.png", equalTo(file.getName()));
        assertThat(new File(contentFolderPath + "name_medium.png").lastModified(), equalTo(file.lastModified()));
    }

    @Test
    void testWillRetrieveExistingImageSmall() throws Exception{
        Field contentFolder = ImageRepository.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(repository, contentFolderPath);
        File file = repository.retrieveImage("name.png", ImageScale.SMALL);

        assertThat("name.png", equalTo(file.getName()));
        assertThat(new File(contentFolderPath + "name.png").lastModified(), equalTo(file.lastModified()));
    }

    @Test
    void testWillRetrieveExistingImageCustom() throws Exception{
        Field contentFolder = ImageRepository.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(repository, contentFolderPath);

        assertThrows(QREverythingException.class, () -> repository.retrieveImage("name.png", ImageScale.CUSTOM));
    }

    @Test
    void testWillCreateNewScaledImage() throws Exception{
        Field contentFolder = ImageRepository.class.getDeclaredField("contentFolder");
        contentFolder.setAccessible(true);
        contentFolder.set(repository, contentFolderPath);
        File file = repository.retrieveImage("name.png", ImageScale.LARGE);

        assertThat("name_large.png", equalTo(file.getName()));
        File newImage = new File(contentFolderPath + "name_large.png");
        assertThat(newImage.lastModified(), equalTo(file.lastModified()));

        newImage.delete();
    }
}
