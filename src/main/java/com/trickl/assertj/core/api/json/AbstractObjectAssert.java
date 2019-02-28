package com.trickl.assertj.core.api.json;

import static com.trickl.assertj.core.api.Assertions.json;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.AbstractAssert;

/**
 * Base class for all json related assertions on objects.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a
 *     href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to
 *     simplify fluent API implementation</a>&quot; for more details.
 */
public abstract class AbstractObjectAssert<S extends AbstractObjectAssert<S>>
    extends AbstractAssert<S, Object> {

  private ObjectMapper objectMapper = new ObjectMapper();

  public AbstractObjectAssert(Object actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Check the json serialization of the object matches the expected output.
   * @return A new assertion object
   * @throws IOException If any file errors occur
   */
  public S jsonSerializationAsExpected() throws IOException {
    String jsonString = serialize(actual);
    com.trickl.assertj.core.api.Assertions.assertThat(json(jsonString))
        .allowingAnyArrayOrdering()
        .writeActualToFileOnFailure()
        .isSameJsonAs(json(classAsResourcePathConvention(actual.getClass(), ".json")));
    return myself;
  }

  /**
   * Check the json deserialization of the object matches the expected output.
   * @return A new assertion object
   * @throws IOException If any file errors occur
   */
  public S jsonDeserializationAsExpected() throws IOException {
    assertThat(
            deserialize(
                classAsResourceUrlConvention(actual.getClass(), ".json"), actual.getClass()))
        .isEqualTo(actual);
    return myself;
  }

  public S usingObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return myself;
  }

  private URL classAsResourceUrlConvention(Class clazz, String extension) {
    String resourceName = clazz.getSimpleName() + extension;
    return clazz.getResource(resourceName);
  }

  private Path classAsResourcePathConvention(Class clazz, String extension) {
    String resourcePath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
    String projectDir = resourcePath.substring(0, resourcePath.indexOf("target"));
    return Paths.get(
        projectDir,
        "src/test/resources/",
        clazz.getPackage().getName().replaceAll("\\.", "/"),
        clazz.getSimpleName() + extension);
  }

  private <T> T deserialize(URL value, Class<T> clazz) throws JsonProcessingException, IOException {
    return objectMapper.readValue(value, (Class<T>) clazz);
  }

  private String serialize(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}
