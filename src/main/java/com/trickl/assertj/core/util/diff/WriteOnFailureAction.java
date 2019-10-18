package com.trickl.assertj.core.util.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.util.Files;
import org.assertj.core.util.diff.Delta;

@RequiredArgsConstructor
@Log
public class WriteOnFailureAction implements PostComparisonAction {
  private final WritableAssertionInfo assertionInfo;
  private final boolean writeActual;
  private final Path actualOutputPath;
  private final boolean writeExpected;
  private final Path expectedOutputPath;

  /**
   * Process a comparison result.
   *
   * @param result The comparison result.
   * @param expected The expected json.
   * @param actual The actual json.
   */
  public void apply(List<Delta<String>> result, String actual, String expected) {
    if (result.isEmpty()) {
      return;
    }

    if (writeActual) {
      File file = write(actualOutputPath, actual);
      assertionInfo.description(
          "Expected output %s, Actual output written to %s",
          expectedOutputPath, file.getAbsolutePath());
    }
    if (writeExpected) {
      File file = write(expectedOutputPath, expected);
      assertionInfo.description("Expected output written to %s", file.getAbsolutePath());
    }
  }

  private File write(Path path, String output) {
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

    return outputFile;
  }
}
