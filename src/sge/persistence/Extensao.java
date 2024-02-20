package sge.persistence;

public enum Extensao {

  CSV, JSON;

  @Override
  public String toString() {
      return name().toLowerCase();
  }

}
