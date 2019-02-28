package com.trickl.assertj.core.util.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.assertj.core.util.Files;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

@RequiredArgsConstructor
@Log
public class WriteOnFailureComparator implements JSONComparator {
  private final JSONComparator comparator;
  private final boolean writeActual;
  private final Path actualOutputPath;
  private final boolean writeExpected;
  private final Path expectedOutputPath;
  private final int indentation = 2;

  @Override
  public JSONCompareResult compareJSON(JSONObject expected, JSONObject actual)
      throws JSONException {
    JSONCompareResult result = comparator.compareJSON(expected, actual);

    writeOnFailure(result, expected.toString(), actual.toString(indentation));

    return result;
  }

  @Override
  public JSONCompareResult compareJSON(JSONArray expected, JSONArray actual) throws JSONException {
    JSONCompareResult result = comparator.compareJSON(expected, actual);
    writeOnFailure(result, expected.toString(), actual.toString(indentation));
    return result;
  }

  @Override
  public void compareJSON(
      String str, JSONObject expected, JSONObject actual, JSONCompareResult result)
      throws JSONException {
    comparator.compareJSON(str, expected, actual, result);
    writeOnFailure(result, expected.toString(), actual.toString(indentation));
  }

  @Override
  public void compareValues(String str, Object expected, Object actual, JSONCompareResult result)
      throws JSONException {
    comparator.compareValues(str, expected, actual, result);
    writeOnFailure(result, expected.toString(), actual.toString());
  }

  @Override
  public void compareJSONArray(
      String str, JSONArray expected, JSONArray actual, JSONCompareResult result)
      throws JSONException {
    comparator.compareJSONArray(str, expected, actual, result);
    writeOnFailure(result, expected.toString(indentation), actual.toString(indentation));
  }

  private void writeOnFailure(JSONCompareResult result, String expected, String actual) {
    if (result.passed()) {
      return;
    }

    if (writeActual) {
      write(actualOutputPath, actual);
    }
    if (writeExpected) {
      write(expectedOutputPath, expected);
    }
  }

  private void write(Path path, String output) {
    File outputFile;
    if (path == null) {
      outputFile = Files.newTemporaryFile();
    } else {
      outputFile = path.toFile();
      outputFile.mkdirs();
    }

    try (Writer writer = new FileWriter(outputFile)) {  
      writer.write(output);
    } catch (IOException ex) {
      log.log(Level.WARNING, "Unable to write to file {0}", outputFile.getAbsolutePath());
    }
  }
}
