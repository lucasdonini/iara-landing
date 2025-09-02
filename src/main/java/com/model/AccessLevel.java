package com.model;

public enum AccessLevel {
  USER(0),
  ADMIN(1),
  SUPER_ADMIN(2);

  private final int level;

  AccessLevel(int level) {
    this.level = level;
  }

  public int level() {
    return level;
  }
}
