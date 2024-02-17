package sge.domain;

public record Endereco (
  String cep,
  String logradouro,
  String numero) implements TipoCadastro {

  @Override
  public String toString() {
    return String.format("CEP %s, %s, %s", cep, logradouro, numero);
  }

  @Override
  public String salvar() {
    return STR."""
      \{cep},\{logradouro},\{numero}""";
  }

}
