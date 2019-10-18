package com.trickl.assertj.core.util.diff;

import java.util.List;

import org.assertj.core.util.diff.Delta;

@FunctionalInterface
public interface PostComparisonAction {
  void apply(List<Delta<String>> diffs, String actual, String expected);
}