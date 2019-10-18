package com.trickl.assertj.core.internal;

import com.trickl.assertj.core.api.json.JsonContainer;
import com.trickl.assertj.core.util.diff.PostComparisonAction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.error.ShouldHaveSameContent;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/** Reusable assertions for directories <code>{@link File}</code>s. */
public class Json {

  private static final Json INSTANCE = new Json();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Json instance() {
    return INSTANCE;
  }

  @VisibleForTesting JsonDiff jsonDiff = new JsonDiff();
  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting
  Json() {}

  /**
   * Asserts that the given JSON structures have same content.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" JSON.
   * @param expected the "expected" JSON.
   * @param comparator how to compare JSON
   * @param postComparisonAction additional actions to perform after comparison
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws IllegalArgumentException if {@code expected} is not an existing directory.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} is not an existing directory.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError if the given directories do not have same content.
   */
  public void assertSameJsonAs(
      AssertionInfo info,
      JsonContainer actual,
      JsonContainer expected,
      JSONComparator comparator,
      PostComparisonAction postComparisonAction) {
    assertNotNull(info, actual);
    assertNotNull(info, actual);
    try {
      List<Delta<String>> diffs = jsonDiff.diff(actual, expected, comparator);
      if (diffs.isEmpty()) {
        return;
      }
      if (postComparisonAction != null) {
        postComparisonAction.apply(diffs, actual.getJson(), expected.getJson());
      }
      throw failures.failure(
          info,
          ShouldHaveSameContent.shouldHaveSameContent(
              new ByteArrayInputStream(actual.getJson().getBytes()), expected.getJson(), diffs));
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to compare JSON", e);
    }
  }

  private static void assertNotNull(AssertionInfo info, JsonContainer actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
