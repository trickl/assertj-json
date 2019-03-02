package com.trickl.assertj.core.api.json;

import static org.skyscreamer.jsonassert.JSONCompareMode.NON_EXTENSIBLE;

import com.trickl.assertj.core.internal.Json;
import com.trickl.assertj.core.util.diff.WriteOnFailureComparator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.util.VisibleForTesting;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/**
 * Base class for all implementations of assertions for json.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a
 *     href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to
 *     simplify fluent API implementation</a>&quot; for more details.
 */
public abstract class AbstractJsonAssert<S extends AbstractJsonAssert<S>>
    extends AbstractAssert<S, JsonContainer> {

  @VisibleForTesting Json json = Json.instance();
  
  private boolean extensible = false;
  
  private boolean strictOrder = true;
  
  private boolean writeActualOnFailure = false;
  
  private Path actualPathOnFailure = null;
  
  private boolean writeExpectedOnFailure = false;
  
  private Path expectedPathOnFailure = null;

  public AbstractJsonAssert(JsonContainer actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public S isSameJsonAs(File expected) throws IOException {
    return isSameJsonAs(expected.toPath());
  }

  public S isSameJsonAs(Path expected) throws IOException {
    String contents = new String(Files.readAllBytes(expected));
    return isSameJsonAs(contents);
  }

  public S isSameJsonAs(String expected) {
    return isSameJsonAs(new JsonContainer(expected));
  }

  /**
   * Check if the supplied JSON is the same as expected.
   * @param expected The expected JSON
   * @return the assertion
   */
  public S isSameJsonAs(JsonContainer expected) {
    JSONComparator comparator = new DefaultComparator(
        JSONCompareMode.STRICT
            .withExtensible(extensible)
            .withStrictOrdering(strictOrder));
    
    if (writeActualOnFailure || writeExpectedOnFailure) {
      comparator = new WriteOnFailureComparator(
          info,
          comparator, 
          writeActualOnFailure,
          actualPathOnFailure,
          writeExpectedOnFailure,
          expectedPathOnFailure);
    }
    
    json.assertSameJsonAs(info, actual, expected, comparator);
    return myself;
  }

  public S allowingExtraUnexpectedFields() {
    extensible = true;
    return myself;
  }

  public S allowingAnyArrayOrdering() {
    strictOrder = false;
    return myself;
  }
  
  /**
   * Write the actual output to a temporary file on failure.
   * @return Updated assertion object
   */
  public S writeActualToFileOnFailure() {
    return writeActualToFileOnFailure(null);
  }
  
  /**
   * Write the actual output to a file on failure.
   * @param path The file path, if null, a temporary file is used
   * @return Updated assertion object
   */
  public S writeActualToFileOnFailure(Path path) {
    writeActualOnFailure = true;
    actualPathOnFailure = path;
    return myself;
  }
  
  /**
   * Write the expected output to a temporary file on failure.
   * @return Updated assertion object
   */
  public S writeExpectedToFileOnFailure() {
    return writeExpectedToFileOnFailure(null);
  }
  
  /**
   * Write the expected output to a file on failure.
   * @param path The file path, if null, a temporary file is used
   * @return Updated assertion object
   */
  public S writeExpectedToFileOnFailure(Path path) {
    writeExpectedOnFailure = true;
    expectedPathOnFailure = path;
    return myself;
  }   
}
