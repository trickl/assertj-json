package com.trickl.assertj.core.api.json;

import static com.trickl.assertj.core.api.JsonAssertions.assertThat;
import static com.trickl.assertj.core.api.JsonAssertions.json;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/**
 * Tests for <code>{@link JsonAssert#isSameJsonAs(java.io.File)}</code>.
 * 
 * @author Yvonne Wang
 */
public class JsonAssert_isSameJsonAs_Test extends JsonAssertBaseTest {

  private static JsonContainer expected;

  @BeforeClass
  public static void beforeOnce() {
    expected = new JsonContainer("xyz");
  }

  @Override
  protected JsonAssert invoke_api_method() {
    return assertions.isSameJsonAs(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(json).assertSameJsonAs(eq(getInfo(assertions)),
        eq(getActual(assertions)),
        eq(expected),
        any(JSONComparator.class));
  }
      
  @Test
  public void should_pass_if_allowing_extra_field_and_array_ordering() {    
     assertThat(json("{\"age\":43, \"friend_ids\":[16, 23, 52]}"))
         .allowingExtraUnexpectedFields()
         .allowingAnyArrayOrdering()
         .isSameJsonAs("{\"friend_ids\":[52, 23, 16]}");     
  }
  
  @Test
  public void should_assert_if_not_allow_extra_field() {    
     assertThatThrownBy(() -> assertThat(json("{\"age\":43, \"friend_ids\":[16, 23, 52]}"))
         .allowingAnyArrayOrdering()
         .isSameJsonAs("{\"friend_ids\":[52, 23, 16]}"))
        .isInstanceOf(AssertionError.class)
        .hasMessage("\nInputStream does not have same content as String:\n" +
            "\n" +
            "Unexpected: 'age'");
  }
    
  @Test
  public void should_assert_if_strict_array_order() {    
     assertThatThrownBy(() -> assertThat(json("{\"age\":43, \"friend_ids\":[16, 23, 52]}"))
         .allowingExtraUnexpectedFields()
         .isSameJsonAs("{\"friend_ids\":[52, 23, 16]}"))
        .isInstanceOf(AssertionError.class)
        .hasMessage("\nInputStream does not have same content as String:\n" +
            "\n" +
            "\"friend_ids[0]\" - Expected: '16' got: '52'\n" +
            "\"friend_ids[2]\" - Expected: '52' got: '16'");
  }
  
  @Test
  public void should_assert_if_nested_array_different() {    
     assertThatThrownBy(() -> assertThat(json("{\"nested\": {\"friend_ids\":[1, 2, 3]}}"))
         .isSameJsonAs("{\"nested\": {\"friend_ids\":[1]}}"))
        .isInstanceOf(AssertionError.class)
        .hasMessage("\nInputStream does not have same content as String:\n" +
            "\n" +
            "nested.friend_ids[]: Expected 1 values but got 3");
  }
  
  @Test
  public void should_assert_if_root_string_different() {    
     assertThatThrownBy(() -> assertThat(json("\"test1\""))
         .isSameJsonAs("\"test2\""))
        .isInstanceOf(AssertionError.class)
        .hasMessage("\nInputStream does not have same content as String:\n\n");
  }
}
