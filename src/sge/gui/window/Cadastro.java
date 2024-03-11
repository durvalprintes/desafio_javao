package sge.gui.window;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import sge.Sistema;
import sge.cadastro.CadastroCurso;
import sge.cadastro.CadastroTurma;
import sge.cadastro.NovoCadastro;
import sge.cadastro.Operacao;
import sge.domain.Nivel;
import sge.domain.Periodo;
import sge.domain.TipoCadastro;
import sge.exception.SgeException;

public class Cadastro extends Padrao {

  private NovoCadastro<? extends TipoCadastro> cadastro;
  private Map<String, Class<?>> camposComboBox = Map.of(
    Sistema.CAMPO_NIVEL, Nivel.class,
    Sistema.CAMPO_PERIODO,Periodo.class,
    Sistema.CAMPO_CURSO,  TipoCadastro.class,
    Sistema.CAMPO_TURMA, TipoCadastro.class);
  private Map<String, Component> camposFormulario = new HashMap<>();
  private DefaultTableModel dadosTabela;

  public Cadastro(String titulo, NovoCadastro<? extends TipoCadastro> cadastro) {
    super();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setTitle(titulo);

    this.cadastro = cadastro;

    var painelFormulario = new JPanel(new GridBagLayout());
    var gradeFormulario = new GridBagConstraints();
    gradeFormulario.insets = new Insets(5, 5, 5, 5);

    var ordem = new AtomicInteger(0);
    adicionarCampos(painelFormulario, gradeFormulario, ordem);
    adicionarBotoes(painelFormulario, gradeFormulario, ordem);

    var painelDivisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelFormulario, adicionarPainelLista());

    getContentPane().add(painelDivisor);
    setModal(true);
    setVisible(true);
  }

  private void adicionarCampos(JPanel painel, GridBagConstraints grade, AtomicInteger ordem) {
    cadastro.getCampos().forEach((campoChave, campo) -> {
      if (Objects.nonNull(campo)) {
        grade.gridx = 0;
        grade.gridy = ordem.getAndIncrement();
        var comando = camposComboBox.get(campoChave) == null ? "Informe" : "Selecione";
        painel.add(new JLabel(STR."\{comando} \{campo} :"), grade);
        grade.gridy = ordem.getAndIncrement();
        Component campoFormulario = criarComponenteCampo(campoChave);
        painel.add(campoFormulario, grade);
        camposFormulario.put(campoChave, campoFormulario);
      }
    });
  }

  private Component criarComponenteCampo(String campoChave) {
    var classeCampo = camposComboBox.get(campoChave);
    if (classeCampo != null) {
      if (classeCampo.isEnum()) {
        return criarComboBox(Stream.of(classeCampo.getEnumConstants()).map(Objects::toString).toArray());
      } else if (classeCampo.isAssignableFrom(TipoCadastro.class)) {
        if (campoChave.equals(Sistema.CAMPO_CURSO)) {
          return criarComboBox(new CadastroCurso().listar().stream().map(TipoCadastro::getIdentificador).toArray());
        } else if (campoChave.equals(Sistema.CAMPO_TURMA)) {
          return criarComboBox(new CadastroTurma().listar().stream().map(TipoCadastro::getIdentificador).toArray());
        }
      }
      throw new SgeException(STR."Campo de formulario não definido para \{campoChave}!");
    }
    return Sistema.CAMPO_MASCARA.containsKey(campoChave) ?
      criarCampoComMascara(Sistema.CAMPO_MASCARA.get(campoChave)) :
      new JTextField(30);
  }

  private JFormattedTextField criarCampoComMascara(String mascara) {
    try {
      var formato = new MaskFormatter(mascara);
      formato.setPlaceholderCharacter('_');
      var campo = new JFormattedTextField(formato);
      campo.setColumns(mascara.length());
      return campo;
    } catch (ParseException e) {
      throw new SgeException("Erro ao obter a máscara!");
    }
  }

  private JComboBox<?> criarComboBox(Object[] valores) {
    return new JComboBox<>(valores);
  }

  private void adicionarBotoes(JPanel painel, GridBagConstraints grade, AtomicInteger ordem) {
    var painelBotoes = new JPanel(new GridLayout(1, 0, 5, 0));
    Stream.of(
      criarBotao("Salvar", () -> salvarFormulario()),
      criarBotao("Limpar", () -> limparFormulario()),
      criarBotao("Cancelar", () -> dispose())
    ).forEach(painelBotoes::add);
    grade.gridx = 0;
    grade.gridy = ordem.get();
    grade.gridwidth = 2;
    painel.add(painelBotoes, grade);
  }

  private JButton criarBotao(String nome, Runnable operacaoCadastro) {
    var botao = new JButton(nome);
    botao.addActionListener(_ -> operacaoCadastro.run());
    return botao;
  }

  private JPanel adicionarPainelLista() {
    dadosTabela =  new DefaultTableModel(cadastro.getCampos().keySet().toArray(), 0);
    listarCadastro();
    var painel = new JPanel(new BorderLayout());
    var tabela = new JTable(dadosTabela);
    tabela.setPreferredScrollableViewportSize(new Dimension(300, 100));
    tabela.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cadastro.setIndice(tabela.getSelectedRow());
        preencherFormulario();
        cadastro.setOperacao(Operacao.EDITAR);
      }
    });
    tabela.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
          cadastro.setIndice(tabela.getSelectedRow());
          excluirFormulario();
        }
      }
    });
    var rolagem = new JScrollPane(tabela);
    painel.add(rolagem, BorderLayout.CENTER);

    return painel;
  }

  private void preencherFormulario() {
    try {
      var selecionado = cadastro.procurarIndice();
      var campos = cadastro.setCampos(selecionado);
      camposFormulario.forEach((campoChave, campoFormulario) -> {
        if (campoFormulario instanceof JTextField campo) {
          campo.setText(campos.get(campoChave));
        } else if (campoFormulario instanceof JComboBox campo) {
          if (campoChave.equals(Sistema.CAMPO_CURSO)) {
            for (int opcao = 0; opcao < campo.getItemCount(); opcao++) {
              if (campo.getItemAt(opcao).toString().contains(campos.get(Sistema.CAMPO_CURSO))) {
                campo.setSelectedIndex(opcao);
                break;
              }
            }
          } else {
            campo.setSelectedItem(campos.get(campoChave));
          }
        }
      });
    } catch (Exception e) {
      exibirMensagem(STR."Erro na listagem. \{e.getMessage()}", 0);
    }
  }

  private void limparFormulario() {
    cadastro.setIndice(0);
    cadastro.setOperacao(Operacao.ADICIONAR);
    cadastro.getCampos().forEach((campoChave, _) -> {
      if (camposFormulario.get(campoChave) instanceof JTextField campo) {
        campo.setText("");
      } else if (camposFormulario.get(campoChave) instanceof JComboBox campo) {
        campo.setSelectedItem(0);
      }
    });
  }

  private void salvarFormulario() {
    try {
      cadastro.getCampos().entrySet().forEach(campo -> {
        if (camposFormulario.get(campo.getKey()) instanceof JFormattedTextField campoFormulario) {
          campo.setValue(campoFormulario.getText().toString().replaceAll("[^\\d/]", ""));
        } else if (camposFormulario.get(campo.getKey()) instanceof JTextField campoFormulario) {
            campo.setValue(campoFormulario.getText().toUpperCase());
        } else if (camposFormulario.get(campo.getKey()) instanceof JComboBox campoFormulario) {
          if (camposComboBox.get(campo.getKey()).isEnum()) {
            campo.setValue(campoFormulario.getSelectedItem().toString());
          } else {
            cadastro.setIndice(campoFormulario.getSelectedIndex());
            campo.setValue(cadastro.procurarIndice().getValores()[0]);
          }
        }
      });
      salvarCadastro();
      exibirMensagem("Cadastro realizado com sucesso!", 1);
    } catch (Exception e) {
      exibirMensagem(STR."Erro no cadastro. Entrada incorreta. \{e.getMessage()}", 0);
    }
  }

  private void listarCadastro() {
    try {
      dadosTabela.setRowCount(0);
      var lista = cadastro.listar();
      if (lista.isEmpty()) {
        exibirMensagem("Nenhum cadastro encontrado.", 2);
        return;
      }
      lista.forEach(this::atualizarTabela);
    } catch (Exception e) {
      exibirMensagem(STR."Erro na listagem. \{e.getMessage()}", 0);
    }
  }

  private void atualizarTabela(TipoCadastro cadastro) {
    dadosTabela.addRow(cadastro.getValores());
  }

  private void excluirFormulario() {
    try {
      var excluir = JOptionPane.showConfirmDialog(Cadastro.this, "Deseja excluir o cadastro?",
        Sistema.TITULO, JOptionPane.YES_NO_OPTION);
      if (excluir == JOptionPane.YES_OPTION) {
        cadastro.setOperacao(Operacao.EXCLUIR);
        salvarCadastro();
      }
    } catch (Exception e) {
      exibirMensagem(STR."Erro na exclusão. \{e.getMessage()}", 0);
    }
  }

  private void salvarCadastro() {
    cadastro.validar();
    cadastro.salvar();
    limparFormulario();
    listarCadastro();
  }

  private void exibirMensagem(String mensagem, int tipoMensagem) {
    var optionPane = new JOptionPane(mensagem, tipoMensagem);
    var popup = optionPane.createDialog(Cadastro.this, Sistema.TITULO);
    popup.pack();
    var preferredSize = popup.getPreferredSize();
    var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - preferredSize.width) / 2;
    int y = (screenSize.height - preferredSize.height) / 2;
    //TODO: POPUP AS VEZES NÃO CENTRALIZADO NA TELA
    popup.setLocation(x, y);
    popup.setModal(true);
    popup.setVisible(true);
    popup.dispose();
  }

}