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
 * <p>Describes the add-delta between original and revised texts.
 *
 * @param <T> The type of the compared elements in the 'lines'.
 */
public class JsonUnexpectedFieldDelta<T> extends Delta<T> {

  @Getter private final FieldComparisonFailure failure;

  public JsonUnexpectedFieldDelta(FieldComparisonFailure failure) {
    super(new Chunk(0, Collections.EMPTY_LIST), new Chunk(0, Collections.EMPTY_LIST));
    this.failure = failure;
  }

  @Override
  public void applyTo(List<T> target) {}

  @Override
  public void verify(List<T> target) throws IllegalStateException {}

  @Override
  public TYPE getType() {
    return Delta.TYPE.INSERT;
  }

  @Override
  public String toString() {
    FieldComparisonFailureFormatter formatter = new FieldComparisonFailureFormatter();
    return formatter.formatUnexpected(failure);
  }
}
