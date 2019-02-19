package com.trickl.assertj.core.api.json;

import static org.skyscreamer.jsonassert.JSONCompareMode.NON_EXTENSIBLE;

import com.google.gson.JsonElement;
import com.trickl.assertj.core.internal.Json;
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

  @VisibleForTesting JSONComparator defaultComparator = new DefaultComparator(NON_EXTENSIBLE);
  
  private boolean extensible = false;
  
  private boolean strictOrder = true;

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

  public S isSameJsonAs(JsonElement expected) {
    return isSameJsonAs(expected.toString());
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
}
