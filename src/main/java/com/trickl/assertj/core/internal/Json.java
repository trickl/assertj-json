package com.trickl.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.List;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/** Reusable assertions for directories <code>{@link File}</code>s. */
public class Json {

  private static final String UNABLE_TO_COMPARE_FILE_CONTENTS =
      "Unable to compare contents of files:<%s> and:<%s>";
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
   * Asserts that the given directories have same content.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" JSON file.
   * @param actualCharset {@link Charset} of the "actual" file.
   * @param expected the "expected" JSON file.
   * @param expectedCharset {@link Charset} of the "actual" file.
   * @param comparator how to compare JSON
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws IllegalArgumentException if {@code expected} is not an existing directory.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} is not an existing directory.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError if the given directories do not have same content.
   */
  public void assertSameJsonAs(
      AssertionInfo info,
      File actual,
      Charset actualCharset,
      File expected,
      Charset expectedCharset,
      JSONComparator comparator) {
    verifyIsFile(expected);
    assertIsFile(info, actual);
    try {
      List<Delta<String>> diffs =
          jsonDiff.diff(actual, actualCharset, expected, expectedCharset, comparator);
      if (diffs.isEmpty()) {
        return;
      }
      throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (IOException e) {
      throw new UncheckedIOException(format(UNABLE_TO_COMPARE_FILE_CONTENTS, actual, expected), e);
    }
  }

  private void verifyIsFile(File expected) {
    checkNotNull(expected, "The file to compare to should not be null");
    checkArgument(expected.isFile(), "Expected file:<'%s'> should be an existing file", expected);
  }

  /**
   * Asserts that the given file is an existing file.
   *
   * @param info contains information about the assertion.
   * @param actual the given file.
   * @throws AssertionError if the given file is {@code null}.
   * @throws AssertionError if the given file is not an existing file.
   */
  public void assertIsFile(AssertionInfo info, File actual) {
    assertNotNull(info, actual);
    if (actual.isFile()) {
      return;
    }
    throw failures.failure(info, shouldBeFile(actual));
  }

  private static void assertNotNull(AssertionInfo info, File actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
