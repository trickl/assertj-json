package com.trickl.assertj.core.presentation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.FieldComparisonFailure;

public class FieldComparisonFailureFormatter {

  public String formatUnexpected(FieldComparisonFailure failure) {
    return formatUnexpected(failure.getActual());
  }
    
  protected String formatUnexpected(Object actual) {
    return "Unexpected: " + describe(actual);
  }
   
  public String formatMissing(FieldComparisonFailure failure) {
    return formatMissing(failure.getExpected());
  }
     
  protected String formatMissing(Object expected) {
    return "Expected: " + describe(expected) + " but none found";
  }

  public String formatRootFailureMessage(Object expected, Object actual) {
    return formatFailureMessage(null, expected, actual);
  }

  public String formatFailureMessage(FieldComparisonFailure failure) {
    return formatFailureMessage(
        failure.getField(), failure.getActual(), failure.getExpected());
  }

  protected String formatFailureMessage(String field, Object expected, Object actual) {
    StringBuilder messageBuilder = new StringBuilder();
    if (field != null && field.length() > 0) {
      messageBuilder.append(quote(field));
      messageBuilder.append(" - ");
    }
    messageBuilder.append("Expected: ");
    messageBuilder.append(describe(expected));
    messageBuilder.append(" ");
    messageBuilder.append("got: ");
    messageBuilder.append(describe(actual));
    return messageBuilder.toString();
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
