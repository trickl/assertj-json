package com.trickl.assertj.core.api.json;

import com.trickl.assertj.core.internal.Json;
import org.assertj.core.api.BaseTestTemplate;
import static org.mockito.Mockito.mock;

/**
 * Tests for <code>{@link JsonAssert}</code>.
 */
public abstract class JsonAssertBaseTest extends BaseTestTemplate<JsonAssert, JsonContainer> {
  
  protected Json json;

  @Override
  protected JsonAssert create_assertions() {
    return new JsonAssert(new JsonContainer("abc"));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    json = mock(Json.class);
    assertions.json = json;
  }

  protected Json getJson(JsonAssert someAssertions) {
    return someAssertions.json;
  }
}
