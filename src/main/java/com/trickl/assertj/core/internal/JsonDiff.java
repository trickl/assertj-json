package com.trickl.assertj.core.internal;

import com.trickl.assertj.util.diff.JsonFieldDelta;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;
import org.json.JSONException;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/** Compares the contents of two JSON objects. */
@VisibleForTesting
public class JsonDiff {

  /**
   * Compares JSON object provided to the expected JSON object using provided comparator, and
   * returns the results of the comparison.
   *
   * @param actual actual JSON file
   * @param actualCharset {@link Charset} of the "actual" file.
   * @param expected expected JSON file
   * @param expectedCharset {@link Charset} of the "expected" file.
   * @param comparator comparator to use
   * @return A list of differences
   * @throws IOException IF there's an error parsing the JSON
   */
  @VisibleForTesting
  public List<Delta<String>> diff(
      File actual,
      Charset actualCharset,
      File expected,
      Charset expectedCharset,
      JSONComparator comparator)
      throws IOException {
    return diff(actual.toPath(), actualCharset, expected.toPath(), expectedCharset, comparator);
  }

  /**
   * Compares JSON object provided to the expected JSON object using provided comparator, and
   * returns the results of the comparison.
   *
   * @param actual actual JSON file path
   * @param actualCharset {@link Charset} of the "actual" file.
   * @param expected expected JSON file path
   * @param expectedCharset {@link Charset} of the "expected" file.
   * @param comparator comparator to use
   * @return A list of differences
   * @throws IOException IF there's an error parsing the JSON
   */
  @VisibleForTesting
  public List<Delta<String>> diff(
      Path actual,
      Charset actualCharset,
      Path expected,
      Charset expectedCharset,
      JSONComparator comparator)
      throws IOException {
    byte[] actualBytes = Files.readAllBytes(actual);
    byte[] expectedBytes = Files.readAllBytes(expected);
    return diff(
        new String(actualBytes, actualCharset),
        new String(expectedBytes, expectedCharset),
        comparator);
  }

  /**
   * Compares JSON object provided to the expected JSON object using provided comparator, and
   * returns the results of the comparison.
   *
   * @param actual actual JSON object
   * @param expected expected JSON object
   * @param comparator comparator to use
   * @return A list of differences
   * @throws IOException IF there's an error parsing the JSON
   */
  @VisibleForTesting
  public List<Delta<String>> diff(String actual, String expected, JSONComparator comparator)
      throws IOException {
    try {
      JSONCompareResult result = JSONCompare.compareJSON(expected, actual, comparator);
      return asDiff(result);
    } catch (JSONException ex) {
      throw new IOException(ex);
    }
  }

  private List<Delta<String>> asDiff(JSONCompareResult result) {
    if (result.passed()) {
      return Collections.EMPTY_LIST;
    }

    List<Delta<String>> diffs = new LinkedList<>();
    for (FieldComparisonFailure missingField : result.getFieldMissing()) {
      diffs.add(new JsonFieldDelta(missingField, Delta.TYPE.DELETE));
    }
        
    for (FieldComparisonFailure changedField : result.getFieldFailures()) {
      diffs.add(new JsonFieldDelta(changedField, Delta.TYPE.CHANGE));
    }

    for (FieldComparisonFailure unexpectedField : result.getFieldUnexpected()) {
      diffs.add(new JsonFieldDelta(unexpectedField, Delta.TYPE.INSERT));
    }

    return diffs;
  }
}
