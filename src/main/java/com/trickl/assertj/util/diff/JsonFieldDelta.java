package com.trickl.assertj.util.diff;

import com.trickl.assertj.core.presentation.FieldComparisonFailureFormatter;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.assertj.core.util.diff.Chunk;
import org.assertj.core.util.diff.Delta;
import org.skyscreamer.jsonassert.FieldComparisonFailure;

/**
 * Initially copied from https://code.google.com/p/java-diff-utils/.
 *
 * <p>Describes the change-delta between original and revised texts.
 *
 * @param <T> The type of the compared elements in the 'lines'.
 */
public class JsonFieldDelta<T> extends Delta<T> {

  @Getter private final FieldComparisonFailure failure;
  
  @Getter private final TYPE type;

  /**
   * Create a delta for a JSON document.
   * @param failure details of the field difference
   * @param type the type of delta
   */
  public JsonFieldDelta(FieldComparisonFailure failure, TYPE type) {
    super(new Chunk(0, Collections.EMPTY_LIST), new Chunk(0, Collections.EMPTY_LIST));
    this.failure = failure;
    this.type = type;
  }

  @Override
  public void applyTo(List<T> target) throws IllegalStateException {}

  @Override
  public void verify(List<T> target) throws IllegalStateException {}

  @Override
  public String toString() {
    FieldComparisonFailureFormatter formatter = new FieldComparisonFailureFormatter();
    if (type == TYPE.INSERT) {
      return formatter.formatUnexpected(failure);
    } else if (type == TYPE.DELETE) {
      return formatter.formatMissing(failure);  
    }
    return formatter.formatFailureMessage(failure);
  }
}
