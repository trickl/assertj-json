package com.trickl.assertj.core.internal;

import static com.trickl.assertj.core.api.JsonAssertions.json;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONCompareMode.NON_EXTENSIBLE;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.assertj.core.util.Files;
import org.assertj.core.util.TextFileWriter;
import org.assertj.core.util.diff.Delta;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

/**
 * This is used to test JsonDiff for correctness
 *
 * @see JsonDiff
 */
public class JsonDiff_diff_File_Test {

  private static JsonDiff diff;
  private static TextFileWriter writer;

  @BeforeClass
  public static void setUpOnce() {
    diff = new JsonDiff();
    writer = TextFileWriter.instance();
  }

  private File actual;
  private File expected;

  @Before
  public void setUp() {
    actual = Files.newTemporaryFile();
    actual.deleteOnExit();
    expected = Files.newTemporaryFile();
    expected.deleteOnExit();
  }

  @Test
  public void should_return_empty_diff_list_if_files_have_equal_content() throws IOException {
    writer.write(actual, "{\"id\": 3}");
    writer.write(expected, "{\"id\": 3}");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).isEmpty();
  }

  @Test
  public void should_return_empty_diff_on_identical_primitives() throws IOException {
    writer.write(actual, "\"original\"");
    writer.write(expected, "\"original\"");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).isEmpty();
  }

  @Test
  public void should_return_diffs_if_files_have_different_fields() throws IOException {
    writer.write(actual, "{\"id\": 3}");
    writer.write(expected, "{\"id\": 5}");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0))
        .hasToString(
            format("\"id\" - Expected: '3' got: '5'"));
  }


  @Test
  public void should_return_diffs_on_different_primitives() throws IOException {
    writer.write(actual, "\"actual\"");
    writer.write(expected, "\"expected\"");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0))
        .hasToString(
            format("Expected: '\"expected\"' got: '\"actual\"'"));
  }

  @Test
  public void should_return_diffs_if_files_have_missing_fields() throws IOException {
    writer.write(actual, "{\"id\": 3}");
    writer.write(expected, "{\"id\": 3, \"id2\": 9}");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0))
        .hasToString(
            format("Expected: 'id2' but none found"));
  }

  @Test
  public void should_return_diffs_if_files_have_unexpected_fields() throws IOException {
    writer.write(actual, "{\"id\": 3, \"id2\": 9}");
    writer.write(expected, "{\"id\": 3}");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0))
        .hasToString(
            format("Unexpected: 'id2'"));
  }

  @Test
  public void should_return_multiple_diffs_if_files_contain_multiple_differences()
      throws IOException {
    writer.write(actual, "{\"id\": 3, \"id2\": 7}");
    writer.write(expected, "{\"id\": 5, \"id2\": 9}");
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).hasSize(2);
    assertThat(diffs.get(0))
        .hasToString(
            format("\"id\" - Expected: '3' got: '5'"));
    assertThat(diffs.get(1))
        .hasToString(
            format("\"id2\" - Expected: '7' got: '9'"));
  }

  @Test
  public void should_be_able_to_detect_mixed_differences() throws IOException {
    // @format:off
    writer.write(actual, "{\"id\": 3, \"id2\": 7, \"id3\": 10}");
    writer.write(expected, "{\"id\": 5, \"id2\": 9, \"id4\": 10}");
    // @format:on
    List<Delta<String>> diffs =
        diff.diff(
            json(actual), json(expected), new DefaultComparator(NON_EXTENSIBLE));
    assertThat(diffs).hasSize(4);
    assertThat(diffs.get(0))
        .hasToString(
            format("Expected: 'id4' but none found"));
    assertThat(diffs.get(1))
        .hasToString(
            format("\"id\" - Expected: '3' got: '5'"));
    assertThat(diffs.get(2))
        .hasToString(
            format("\"id2\" - Expected: '7' got: '9'"));
    assertThat(diffs.get(3))
        .hasToString(
            format("Unexpected: 'id3'"));
  }
}
