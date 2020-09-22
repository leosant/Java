package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author LeoUser - leonardosantos45548@gmail.com
 */
public class TelaOS extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    boolean flagHintImp = false;
    /* Linha abaixo cria uma variavel para armazenar um texto de acordo 
      com o radiobutton selecionado*/
    private String tipo;

    public TelaOS() {
        initComponents();
        conexao = ModuloConexao.conector();
        //Linha abaixo deixa o campo OS setado no inicio
        lblOs.setText("0");
        //Delimita o espaço somente de visualizar para horizontal crescendo com scrollbar para vertical
        txtOsDef.setLineWrap(true);
        txtOsServ.setLineWrap(true);
        this.populaJCombobox();
    }

//Método realiza pesquisa no Banco de dados onde a condição verifica quantas Os possui em registro do cliente
    private void pesquisar_clientes() {
        String sql = "select CLIJOIN.idcli as ID,nomecli as Nome,fone as Contato,OSADD.os as OS from tbclientes as CLIJOIN inner join tbos as OSADD on(CLIJOIN.idcli = OSADD.idcli) where nomecli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            /*Passando o conteúdo da caixa de pesquisa
            atenção ao "%" que é a continuação da String sql*/
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            if (rs.next()) {
                //Linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
                tblClientes.setModel(DbUtils.resultSetToTableModel(rs));

            } else {
                String sqlSecundaria = "select idcli as Id, nomecli as Nome, fone as Contato from tbclientes where nomecli like ?";
                try {
                    pst = conexao.prepareStatement(sqlSecundaria);
                    /*Passando o conteúdo da caixa de pesquisa
                    atenção ao "%" que é a continuação da String sql*/
                    pst.setString(1, txtCliPesquisar.getText() + "%");
                    //Linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
                    rs = pst.executeQuery();
                    tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        lblCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
    }

    //Metodo serve para armazenar apenas o String necessário no BD, a situacao evita que passa as tags de HTML
    private String situacao(String situacao) {
        if (situacao.contains("Pendente")) {
            return "Pendente";
        }
        if (situacao.contains("Em Manutenção")) {
            return "Em Manutenção";
        }
        if (situacao.contains("Aguardando Aprovação")) {
            return "Aguardando Aprovação";
        }
        if (situacao.contains("Aguardando Peças")) {
            return "Aguardando Peças";
        }
        if (situacao.contains("Desistência do Produto")) {
            return "Desistência do Produto";
        }
        if (situacao.contains("Retorno")) {
            return "Retorno";
        }
        if (situacao.contains("Negado")) {
            return "Negado";
        }
        if (situacao.contains("Concluído")) {
            return "Concluído";
        }
        if (situacao.contains("Entregue")) {
            return "Entregue";
        }
        return null;
    }

    //Método para cadastrar os
    private void emitir_os() {
        flagHintImp = false;
        String sql = "insert into tbos(tipo, situacao, equipamento, defeito, servico, tecnico, valor, idcli) values (?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, situacao(cboOsSit.getSelectedItem().toString()));
            pst.setString(3, txtOsEquip.getText());
            pst.setString(4, txtOsServ.getText());
            pst.setString(5, txtOsServ.getText());
            pst.setString(6, cboOsTec.getSelectedItem().toString());
            //.replace substituir a vírgula pelo ponto
            pst.setString(7, txtOsValor.getText().replace(",", "."));
            pst.setString(8, lblCliId.getText());

            //Linha abaixo serve como validação dos campos obrigatórios
            if ((lblCliId.getText().isEmpty()) || (txtOsEquip.getText().isEmpty()) || (txtOsServ.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "<html>Por favor, preencha todos os campos <b>* obrigatórios !</b></html>", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "<html>Ordem de Serviço Emitida !</html>", "Concluído", JOptionPane.INFORMATION_MESSAGE);                   //Limpa os campos após registrar as OS
                    cboOsSit.setSelectedItem("<html><font size = 3 color = olive><b>Pendente</b></font></html>");
                    lblCliId.setText(Integer.toString(0));
                    txtOsEquip.setText(null);
                    txtOsServ.setText(null);
                    txtOsServ.setText(null);
                    //cboOsTec.setText(null);
                    lblOs.setText("0");
                    txtOsValor.setText("0,00");
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    //Método para pesquisar os
    private void pesquisar_os() {
        //Linha abaixo cria uma caixa de entrada do tipo Joption pane
        String num_os = JOptionPane.showInputDialog("Número da OS: ");
        String sql = "select * from tbos where os =" + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                lblOs.setText(rs.getString(1));
                lblData.setText(rs.getString(2));
                //Setando os Radio Button
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("OS")) {
                    rbtOs.setSelected(true);
                    tipo = "OS";
                } else {
                    rbtOrc.setSelected(true);
                    //Creio que isso já acontece, ver a possibilidade de refatorar - Leo
                    tipo = "Orçamento";
                }
                cboOsSit.setSelectedItem(rs.getString(4));
                txtOsEquip.setText(rs.getString(5));
                txtOsServ.setText(rs.getString(6));
                txtOsServ.setText(rs.getString(7));
                cboOsTec.setSelectedItem(rs.getString(8));
                txtOsValor.setText(rs.getString(9));
                lblCliId.setText(rs.getString(10));
                btnOsAdicionar.setEnabled(false);
                txtCliPesquisar.setEnabled(false);
                tblClientes.setVisible(false);
                //Variavel valida a impressão
                flagHintImp = true;
            } else if (num_os == null) {
                /*Linha abaixo serve de apoio a lógica
                System.out.println("cancelei" + num_os);*/

            } else {
                JOptionPane.showMessageDialog(null, "<html><b>Ordem de serviço</b> não encontrada ! </html>", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                flagHintImp = false;
            }
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "<html>Ordem de Serviço Inválida !</html>", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            /* Linha abaixo serve de apoio para encontrar a exerção
            System.out.println(e);*/
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }

    }

    //Método para alterar OS
    private void alterar_os() {
        flagHintImp = false;
        String sql = "update tbos set tipo = ?, situacao = ?, equipamento = ?, defeito = ?, servico = ?, tecnico = ?, valor = ? where os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, situacao(cboOsSit.getSelectedItem().toString()));
            pst.setString(3, txtOsEquip.getText());
            pst.setString(4, txtOsServ.getText());
            pst.setString(5, txtOsServ.getText());
            pst.setString(6, cboOsTec.getSelectedItem().toString());
            //.replace substituir a vírgula pelo ponto
            pst.setString(7, txtOsValor.getText().replace(",", "."));
            pst.setString(8, lblOs.getText());
            //Linha abaixo serve como validação dos campos obrigatórios
            if ((lblCliId.getText().isEmpty()) || (txtOsEquip.getText().isEmpty()) || (txtOsServ.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "<html>Por favor, preencha todos os campos <b>* obrigatórios !</b></html>", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "<html>Ordem de Serviço alterada com sucesso !</html>", "Concluído", JOptionPane.INFORMATION_MESSAGE);
                    //Limpa os campos após registrar as OS
                    cboOsSit.setSelectedItem("<html><font size = 3 color = olive><b>Pendente</b></font></html>");
                    //Seto a variavel como zero para resolver o error de javalang NullPointer, pois aqui estou alocando 0 na memória
                    lblCliId.setText(Integer.toString(0));
                    txtOsEquip.setText(null);
                    txtOsServ.setText(null);
                    txtOsServ.setText(null);
                    //cboOsTec.setText(null);
                    txtOsValor.setText("0,00");
                    lblOs.setText("0");
                    lblData.setText(null);
                    //Habilitando objetos
                    btnOsAdicionar.setEnabled(true);
                    txtCliPesquisar.setEnabled(true);
                    tblClientes.setVisible(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método para excluir Os
    private void excluir_os() {
        flagHintImp = false;
        if (lblOs.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "<html><b>Consulte</b> a ordem de serviço que deseja excluir !</html>", "Atenção", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int confirma = JOptionPane.showConfirmDialog(null, "<html>Tem certeza que deseja <b>excluir</b> a ordem de serviço ?</html>", "Atenção", JOptionPane.YES_NO_OPTION);
            //Estrutura analisa se a condição recebe yes
            if (confirma == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "<html>Ordem de serviço excluída com sucesso !</html>", "Concluída", JOptionPane.INFORMATION_MESSAGE);
                String sql = "delete from tbos where os = ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, lblOs.getText());
                    pst.executeUpdate();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                //Limpando os campos
                lblOs.setText("0");
                lblCliId.setText(Integer.toString(0));
                lblData.setText(null);
                cboOsSit.setSelectedItem("<html><font size = 3 color = olive><b>Pendente</b></font></html>");
                // Não será necessário - cboOsTec.setSelectedItem("Administrador");
                txtOsValor.setText("0,00");
                txtOsEquip.setText(null);
                txtOsServ.setText(null);
                txtOsServ.setText(null);
                btnOsAdicionar.setEnabled(true);
                txtCliPesquisar.setEnabled(true);
                tblClientes.setVisible(true);
            }
        }
    }

    //Método busca preencher o campo de tecnicos através dos usuários cadastrados no banco de dados
    private void populaJCombobox() {
        String sql = "select * from tbusuarios";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboOsTec.addItem(rs.getString("usuario"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Método para imprimir OS
    private void imprimir_os() {
        try {
            // Usando a classe HashMap para criar um filtro            
            HashMap filtro = new HashMap();
            filtro.put("os", Integer.parseInt(lblOs.getText()));
            // Usando a classe JasperPrint para preparar a impressão de um relatório
            JasperPrint print = JasperFillManager.fillReport("C:\\reports\\os.jasper", filtro, conexao);
            //Linha abaixo exibe o relatório através da classe JasperViewer
            if (flagHintImp == true) {
                JasperViewer.viewReport(print, false);
            } else {
                JOptionPane.showMessageDialog(null, "<html><b>Ordem de serviço</b> em branco ou incompleta !</html>", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rbtOrc = new javax.swing.JRadioButton();
        rbtOs = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        lblOs = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboOsSit = new javax.swing.JComboBox<>();
        cboOsTec = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        lblCliId = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtOsEquip = new javax.swing.JTextField();
        txtOsValor = new javax.swing.JTextField();
        btnOsAdicionar = new javax.swing.JButton();
        btnOsPesquisar = new javax.swing.JButton();
        btnOsExcluir = new javax.swing.JButton();
        btnOsAlterar = new javax.swing.JButton();
        btnOsImprimir = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtOsServ = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtOsDef = new javax.swing.JTextArea();

        jLabel3.setText("jLabel3");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ordem de Serviço");
        setPreferredSize(new java.awt.Dimension(640, 480));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(250, 150));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Nº OS:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Data:");

        buttonGroup1.add(rbtOrc);
        rbtOrc.setText("Orçamento");
        rbtOrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrcActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOs);
        rbtOs.setText("Ordem de Serviço");
        rbtOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOsActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Tipo do Serviço");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtOrc)
                        .addGap(18, 18, 18)
                        .addComponent(rbtOs))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblOs)
                                .addGap(32, 32, 32)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblData)))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lblOs, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(lblData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOs)
                    .addComponent(rbtOrc))
                .addGap(16, 16, 16))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Situação");

        cboOsSit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboOsSit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<html><font size = 3 color = olive><b>Pendente</b></font></html>", "<html><font size = 3 color = orange><b> Em Manutenção</b></font></html>", "<html><font size = 3 color = blue><b>Aguardando Aprovação</b></font></html>", "<html><font size = 3 color = navy blue><b>Aguardando Peças</b></font></html>", "<html><font size = 3 color = black><b>Desistência do Produto</b></font></html>", "<html><font size = 3 color = red ><b>Retorno</b></font></html>", "<html><font size = 3 color = purple><b>Negado</b></font></html>", "<html><font size = 3 color = green><b>Concluído</b></font</html>", "<html><font size = 3 color = maroon><b>Entregue</b></font></html>" }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisaricone.png"))); // NOI18N

        jLabel7.setText("*Id ");

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Contato", "OS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblClientes);
        if (tblClientes.getColumnModel().getColumnCount() > 0) {
            tblClientes.getColumnModel().getColumn(0).setResizable(false);
            tblClientes.getColumnModel().getColumn(2).setResizable(false);
            tblClientes.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtCliPesquisar)
                        .addComponent(jLabel7)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(lblCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("*Equipamento");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("*Defeito");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Serviço");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("*Técnico");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Valor Total");

        txtOsValor.setText(" 0,00");
        txtOsValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOsValorKeyTyped(evt);
            }
        });

        btnOsAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/adddoc.png"))); // NOI18N
        btnOsAdicionar.setToolTipText("Adicionar");
        btnOsAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAdicionarActionPerformed(evt);
            }
        });

        btnOsPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/read.png"))); // NOI18N
        btnOsPesquisar.setToolTipText("Pesquisar OS");
        btnOsPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsPesquisarActionPerformed(evt);
            }
        });

        btnOsExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnOsExcluir.setToolTipText("Apagar");
        btnOsExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsExcluirActionPerformed(evt);
            }
        });

        btnOsAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update2.png"))); // NOI18N
        btnOsAlterar.setToolTipText("Alterar");
        btnOsAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAlterarActionPerformed(evt);
            }
        });

        btnOsImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/impressora (1).png"))); // NOI18N
        btnOsImprimir.setToolTipText("Imprimir OS");
        btnOsImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsImprimir.setDoubleBuffered(true);
        btnOsImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsImprimirActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel13.setText("*Campos Obrigatórios");

        jLabel14.setText("R$");

        txtOsServ.setColumns(20);
        txtOsServ.setRows(5);
        jScrollPane4.setViewportView(txtOsServ);

        txtOsDef.setColumns(20);
        txtOsDef.setRows(5);
        jScrollPane6.setViewportView(txtOsDef);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(4, 4, 4)
                                        .addComponent(cboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(4, 4, 4)
                                .addComponent(txtOsEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(2, 2, 2)
                                .addComponent(cboOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(89, 89, 89)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addGap(4, 4, 4)
                                .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel10))
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(btnOsAdicionar)
                        .addGap(17, 17, 17)
                        .addComponent(btnOsPesquisar)
                        .addGap(17, 17, 17)
                        .addComponent(btnOsAlterar)
                        .addGap(17, 17, 17)
                        .addComponent(btnOsExcluir)
                        .addGap(17, 17, 17)
                        .addComponent(btnOsImprimir)))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel12)))
                            .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel5))
                            .addComponent(cboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel8))
                    .addComponent(txtOsEquip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel9))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel10))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOsAdicionar)
                    .addComponent(btnOsPesquisar)
                    .addComponent(btnOsAlterar)
                    .addComponent(btnOsExcluir)
                    .addComponent(btnOsImprimir))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // Chamando o método pesquisar_clientes
        pesquisar_clientes();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // Chamando o método setar_campos
        //Estrutura retira a possibilidade de edição da table sem necessidade
        boolean editando = tblClientes.isEditing();
        if (editando == false) {
            JOptionPane.showMessageDialog(null, "Selecionado", "Concluído", JOptionPane.INFORMATION_MESSAGE);
        }
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void rbtOrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrcActionPerformed
        // Atribuindo um texto a variavel tipo se selecionado
        tipo = "Orçamento";
    }//GEN-LAST:event_rbtOrcActionPerformed

    private void rbtOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOsActionPerformed
        // Atribuindo um texto a variavel tipo se selecionado
        tipo = "OS";
    }//GEN-LAST:event_rbtOsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // Ao abrir o form marcar o radio button orçamento
        rbtOrc.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnOsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAdicionarActionPerformed
        // Chamando o método emitir_os
        emitir_os();
    }//GEN-LAST:event_btnOsAdicionarActionPerformed

    private void btnOsPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsPesquisarActionPerformed
        //Chamando o método pesquisar OS
        pesquisar_os();
    }//GEN-LAST:event_btnOsPesquisarActionPerformed

    private void btnOsAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAlterarActionPerformed
        // Chamando o método alterar os
        alterar_os();
    }//GEN-LAST:event_btnOsAlterarActionPerformed

    private void btnOsExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsExcluirActionPerformed
        // Chamando o método exclui os
        excluir_os();
    }//GEN-LAST:event_btnOsExcluirActionPerformed

    private void btnOsImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsImprimirActionPerformed
        // Chamando o método para imprimir os
        imprimir_os();
    }//GEN-LAST:event_btnOsImprimirActionPerformed

    private void txtOsValorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsValorKeyTyped
        // Evento impede a utilização de caracteres que não forem números
        //Depois quero atribuir limites depois da vírgula
        String caracteres = ",0987654321";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtOsValorKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOsAdicionar;
    private javax.swing.JButton btnOsAlterar;
    private javax.swing.JButton btnOsExcluir;
    private javax.swing.JButton btnOsImprimir;
    private javax.swing.JButton btnOsPesquisar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboOsSit;
    private javax.swing.JComboBox<Object> cboOsTec;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCliId;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblOs;
    private javax.swing.JRadioButton rbtOrc;
    private javax.swing.JRadioButton rbtOs;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextArea txtOsDef;
    private javax.swing.JTextField txtOsEquip;
    private javax.swing.JTextArea txtOsServ;
    private javax.swing.JTextField txtOsValor;
    // End of variables declaration//GEN-END:variables
}
