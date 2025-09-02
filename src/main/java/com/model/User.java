package com.model;

import java.time.LocalDate;

public class User {
  private int id;
  private String name;
  private String login;
  private String password;
  private AccessLevel accessLevel;
  private LocalDate creationDate;
  private boolean status;

  public User(int id, String name, String login, String password, AccessLevel accessLevel, LocalDate creationDate, boolean status) {
    this.id = id;
    this.accessLevel = accessLevel;
    this.creationDate = creationDate;
    this.login = login;
    this.name = name;
    this.password = password;
    this.status = status;
  }

  public User() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
