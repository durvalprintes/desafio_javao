package sge.cadastro;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import sge.Sistema;
import sge.domain.Curso;
import sge.domain.Nivel;
import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.util.ArquivoUtil;

public class CadastroCurso extends NovoCadastro<Curso> {

  public CadastroCurso() {
    arquivo = ArquivoUtil.getTipoArquivo(Sistema.CAMINHO_ARQUIVO_CURSO, Curso.class);
  }

  @Override
  protected void inicializarCampos(Map<String, String> campos) {
    campos.put(Sistema.CAMPO_CODIGO, "o código");
    campos.put("nome", "o nome");
    campos.put(Sistema.CAMPO_CARGA_HORARIA, "a carga horária (HORAS)");
    campos.put(Sistema.CAMPO_NIVEL, "o nível");
  }

  @Override
  public void validar() throws SgeException {
    if (!operacao.equals(Operacao.EXCLUIR)) {
      if (!Objects
        .requireNonNull(campos.get(Sistema.CAMPO_CARGA_HORARIA), "Carga horária é obrigatória.")
        .matches(Sistema.NUMERO_VALIDO) ||
        Integer.parseInt(campos.get(Sistema.CAMPO_CARGA_HORARIA)) < 1) {
        throw new SgeException("Carga horária é inválida.");
      }
  
      var nivelEnum = Arrays
        .stream(Nivel.values())
        .filter(nivel ->
          nivel.name().equals(Objects.requireNonNull(campos.get(Sistema.CAMPO_NIVEL), "Nivel é obrigatório.")))
        .findAny()
        .orElseThrow(() -> new SgeException("Nivel é inválido."));
  
      if (this.verificarLista()
        .stream()
        .anyMatch(curso ->
          curso.codigo().equals(
            Objects.requireNonNull(campos.get(Sistema.CAMPO_CODIGO), "Código é obrigatório.")))) {
        throw new SgeException("Código de curso já cadastrado.");
      }
  
      cadastro = new Curso(campos.get(Sistema.CAMPO_CODIGO),
        Objects.requireNonNull(campos.get("nome"), "Nome é obrigatório."),
        Integer.parseInt(campos.get(Sistema.CAMPO_CARGA_HORARIA)),
        nivelEnum);
    } else {
      var cadastro = this.procurarIndice();
      if (new CadastroTurma().listar()
        .stream()
        .anyMatch(turma ->
          turma.curso().equals(cadastro.codigo()))) {
        throw new SgeException("Curso com turmas cadastradas.");
      }
    }
  }
  
  @Override
  public Map<String, String> setCampos(TipoCadastro cadastro) throws SgeException {
    if (cadastro instanceof Curso(String codigo, String nome, int cargaHoraria, Nivel nivel)) {
      campos.put(Sistema.CAMPO_CODIGO, codigo);
      campos.put("nome", nome);
      campos.put(Sistema.CAMPO_CARGA_HORARIA, String.valueOf(cargaHoraria));
      campos.put(Sistema.CAMPO_NIVEL, nivel.name());
      return campos;
    }
    throw new SgeException("Tipo de cadastro incorreto");
  }

}
