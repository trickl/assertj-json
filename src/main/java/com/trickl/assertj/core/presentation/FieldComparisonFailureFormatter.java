package com.trickl.assertj.core.presentation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.FieldComparisonFailure;

public class FieldComparisonFailureFormatter {

  public String formatUnexpected(FieldComparisonFailure failure) {
    return formatUnexpected(failure.getActual());
  }
    
  private String formatUnexpected(Object actual) {
    return "Unexpected: " + describe(actual);
  }
   
  public String formatMissing(FieldComparisonFailure failure) {
    return formatMissing(failure.getExpected());
  }
     
  private String formatMissing(Object expected) {
    return "Expected: " + describe(expected) + " but none found";
  }

  public String formatFailureMessage(FieldComparisonFailure failure) {
    return FieldComparisonFailureFormatter.this.formatFailureMessage(
        failure.getField(), failure.getActual(), failure.getExpected());
  }

  private String formatFailureMessage(String field, Object expected, Object actual) {
    return quote(field) + " - Expected: " + describe(expected) + " got: " + describe(actual);
  }
  
  private static String quote(String value) {
    return "\"" + value + "\"";
  }

  private static String describe(Object value) {
    if (value instanceof JSONArray) {
      return "a JSON array";
    } else if (value instanceof JSONObject) {
      return "a JSON object";
    } else {
      return "'" + value.toString() + "'";
    }
  }
}
