package com.trickl.assertj.core.api.json;

import static com.trickl.assertj.core.api.Assertions.json;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
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
  
  private Path jsonSchemaResourcePath = null;
  
  private Path serializationResourcePath = null;
  
  private URL deserializationResourceUrl = null;

  public AbstractObjectAssert(Object actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Check the json serialization of the object matches the expected output.
   * @return A new assertion object
   * @throws IOException If any file errors occur
   */
  public S jsonSerializationAsExpected() throws IOException {
    if (serializationResourcePath == null) {
      serializationResourcePath = classAsResourcePathConvention(
          actual.getClass(), ".json");
    }
      
    String jsonString = serialize(actual);
    com.trickl.assertj.core.api.Assertions.assertThat(json(jsonString))
        .allowingAnyArrayOrdering()
        .writeActualToFileOnFailure()
        .isSameJsonAs(json(serializationResourcePath));
    return myself;
  }

  /**
   * Check the json deserialization of the object matches the expected output.
   * @return A new assertion object
   * @throws IOException If any file errors occur
   */
  public S jsonDeserializationAsExpected() throws IOException {
    if (deserializationResourceUrl == null) {
      deserializationResourceUrl = classAsResourceUrlConvention(
          actual.getClass(), ".example.json"); 
    }  
      
    assertThat(deserialize(deserializationResourceUrl, actual.getClass()))
        .isEqualTo(actual);
    return myself;
  }
    
  /**
   * Check the json schema of the object matches the expected output.
   * @return A new assertion object
   * @throws IOException If any file errors occur
   */
  public S jsonSchemaAsExpected() throws IOException {
    if (jsonSchemaResourcePath == null) {
      jsonSchemaResourcePath = classAsResourcePathConvention(
          actual.getClass(), ".schema.json");
    }
      
    JsonSchema jsonSchema = jsonSchema(actual);
    String jsonString = jsonSchema.toString();
    com.trickl.assertj.core.api.Assertions.assertThat(json(jsonString))
        .allowingAnyArrayOrdering()
        .writeActualToFileOnFailure()
        .isSameJsonAs(json(jsonSchemaResourcePath));
    return myself;
  }
  
  public S usingJsonSchemaResourcePath(Path path) {
    jsonSchemaResourcePath = path;
    return myself;
  }
  
  public S usingSerializationResourcePath(Path path) {
    serializationResourcePath = path;
    return myself;
  }
  
  public S usingDeserializationResourceUrl(URL url) {
    deserializationResourceUrl = url;
    return myself;
  }

  public S usingObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return myself;
  }

  private <T> T deserialize(URL value, Class<T> clazz) throws JsonProcessingException, IOException {
    return objectMapper.readValue(value, (Class<T>) clazz);
  }

  private String serialize(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
  
  private JsonSchema jsonSchema(Object obj) throws JsonProcessingException {
    JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(objectMapper);
    return schemaGen.generateSchema(actual.getClass());
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
}
