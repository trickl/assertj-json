package com.trickl.assertj.json.api;

import java.io.File;
import org.assertj.core.api.AbstractFileAssert;

/*
 * Assertions for directories {@link File}s.
 */
public class JsonAssert extends AbstractFileAssert<JsonAssert> {
  public JsonAssert(File actual) {
    super(actual, JsonAssert.class);
  }
}
