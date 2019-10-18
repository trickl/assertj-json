package com.trickl.assertj.util.diff;

import com.trickl.assertj.core.presentation.FieldComparisonFailureFormatter;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.assertj.core.util.diff.Chunk;
import org.assertj.core.util.diff.Delta;

/**
 * Initially copied from https://code.google.com/p/java-diff-utils/.
 *
 * <p>Describes the change-delta between original and revised texts.
 *
 * @param <T> The type of the compared elements in the 'lines'.
 */
public class JsonRootDelta<T> extends Delta<T> {

  @Getter private final String expected;

  @Getter private final String actual;

  @Getter private final String message;

  @Getter private final TYPE type = TYPE.CHANGE;

  /**
  * Create a delta for a JSON document.
  * @param expected the expected element
  * @param actual the actual element
  * @param message the optional descriptive message
  */
  public JsonRootDelta(String expected, String actual, String message) {
    super(new Chunk(0, Collections.EMPTY_LIST), new Chunk(0, Collections.EMPTY_LIST));
    this.expected = expected;
    this.actual = actual;
    this.message = message;
  }

  @Override
  public void applyTo(List<T> target) throws IllegalStateException {}

  @Override
  public void verify(List<T> target) throws IllegalStateException {}

  @Override
  public String toString() {
    FieldComparisonFailureFormatter formatter = new FieldComparisonFailureFormatter();
    if (message != null && message.length() > 0) {
      return message;
    }
    return formatter.formatRootFailureMessage(expected, actual);
  }
}
