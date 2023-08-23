package sge;

public class Estudante {

  private String nome;
  private int idade;
  private String telefone;
  private String responsavel;
  private Endereco endereco;

  public Estudante(String nome, int idade, String telefone, String responsavel, Endereco endereco) {
    this.nome = nome;
    this.idade = idade;
    this.telefone = telefone;
    this.responsavel = responsavel;
    this.endereco = endereco;
  }

  public String getNome() {
    return nome;
  }

  public int getIdade() {
    return idade;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public String imprime() {
    return String.format("""
      Nome: %s
      idade: %s
      telefone: %s
      responsavel: %s
      endereco: %s""", nome, idade, telefone, responsavel, endereco.imprime());
  }

}
