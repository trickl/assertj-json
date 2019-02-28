package com.trickl.assertj.core.api;

import com.google.gson.JsonElement;
import com.trickl.assertj.core.api.json.JsonAssert;
import com.trickl.assertj.core.api.json.JsonContainer;
import com.trickl.assertj.core.api.json.ObjectAssert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** The entry point for all JSON assertions. */
public class Assertions {

  public static JsonAssert assertThat(JsonContainer json) {
    return new JsonAssert(json);
  }

  public static JsonAssert assertThat(JsonElement json) {
    return new JsonAssert(new JsonContainer(json.toString()));
  }
  
  public static ObjectAssert assertThat(Object object) {
    return new ObjectAssert(object);
  }
  
  public static JsonContainer json(String str) {
    return new JsonContainer(str);
  }

  public static JsonContainer json(Path path) throws IOException {
    String contents = new String(Files.readAllBytes(path));
    return json(contents);
  }

  public static JsonContainer json(File file) throws IOException {
    return json(file.toPath());
  }

  /** Creates a new <code>{@link Assertions}</code>. */
  protected Assertions() {
    // empty
  }
}
