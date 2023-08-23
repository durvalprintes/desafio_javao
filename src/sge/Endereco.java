package sge;

public class Endereco {

  private String cep;
  private String logradouro;
  private String numero;

  public Endereco(String cep, String logradouro, String numero) {
    this.cep = cep;
    this.logradouro = logradouro;
    this.numero = numero;
  }

  public String getCep() {
    return cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public String imprime() {
    return String.format("%s, %s, %s", cep, logradouro, numero);
  }

}
