package com.model;

public class Factory {
  private int id;
  private String factoryName;
  private String cnpj;
  private boolean status;
  private String email;
  private String enterpriseName;
  private String sector;

  public Factory(int id, String factoryName, String cnpj, boolean status, String email, String enterpriseName, String sector) {
    this.id = id;
    this.factoryName = factoryName;
    this.cnpj = cnpj;
    this.status = status;
    this.email = email;
    this.enterpriseName = enterpriseName;
    this.sector = sector;
  }

  public Factory() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFactoryName() {
    return factoryName;
  }

  public void setFactoryName(String name) {
    this.factoryName = name;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEnterpriseName() {
    return enterpriseName;
  }

  public void setEnterpriseName(String factory_name) {
    this.enterpriseName = factory_name;
  }

  public String getSector() {
    return sector;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }
}
