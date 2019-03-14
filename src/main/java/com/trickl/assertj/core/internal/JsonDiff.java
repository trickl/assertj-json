package com.trickl.assertj.core.internal;

import com.trickl.assertj.core.api.json.JsonContainer;
import com.trickl.assertj.util.diff.JsonFieldDelta;
import com.trickl.assertj.util.diff.JsonMessageDelta;
import java.io.IOException;
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
   * @param actual actual JSON object
   * @param expected expected JSON object
   * @param comparator comparator to use
   * @return A list of differences
   * @throws IOException IF there's an error parsing the JSON
   */
  @VisibleForTesting
  public List<Delta<String>> diff(
      JsonContainer actual, JsonContainer expected, JSONComparator comparator) throws IOException {
    try {
      JSONCompareResult result =
          JSONCompare.compareJSON(expected.getJson(), actual.getJson(), comparator);
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
    
    if (diffs.isEmpty()) {
      diffs.add(new JsonMessageDelta(result.getMessage()));      
    }

    return diffs;
  }
}
