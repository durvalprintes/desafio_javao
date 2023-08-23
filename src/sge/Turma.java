package sge;

import java.util.ArrayList;
import java.util.List;

public class Turma {

  private String codigo;
  private String nome;
  private int capidade;
  private List<Estudante> estudantes = new ArrayList<>();

  public Turma(String codigo, String nome, int capidade) {
    this.codigo = codigo;
    this.nome = nome;
    this.capidade = capidade;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getNome() {
    return nome;
  }

  public int getCapidade() {
    return capidade;
  }

  public List<Estudante> getEstudantes() {
    return estudantes;
  }

  public String imprime() {
    return String.format("Turma %s (%s) possui capacidade para %s estudantes e com %d vagas disponiveis.", 
      nome, codigo, capidade, capidade - estudantes.size());
  }

}
