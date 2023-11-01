package sge.domain;

public record Endereco (String cep, String logradouro, String numero) {

  public String imprime() {
    return String.format("CEP %s, %s, %s", cep, logradouro, numero);
  }

}
