package com.trickl.assertj.util.diff;

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
public class JsonMessageDelta<T> extends Delta<T> {

  @Getter private final String message;
  
  @Getter private final TYPE type = TYPE.CHANGE;
  
  /**
   * Create a delta for a JSON document.
   * @param message details of the difference
   */
  public JsonMessageDelta(String message) {
    super(new Chunk(0, Collections.EMPTY_LIST), new Chunk(0, Collections.EMPTY_LIST));
    this.message = message;
  }  

  @Override
  public void applyTo(List<T> target) throws IllegalStateException {}

  @Override
  public void verify(List<T> target) throws IllegalStateException {}

  @Override
  public String toString() {
    return message;
  }
}
