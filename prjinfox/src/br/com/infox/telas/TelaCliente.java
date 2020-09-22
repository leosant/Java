package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import net.proteanit.sql.DbUtils;

public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        painelEsconde.setVisible(false);
    }

    private void adicionar() {
        pesquisar_cliente();
        String sql = "insert into tbclientes(nomecli,endcli,fone,emailcli) value(?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliFone.getText());
            pst.setString(4, txtCliEmail.getText());
            //Validação dos campos obrigatórios
            if (txtCliNome.getText().isEmpty() || txtCliFone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "<html>Preencha todos os <b>campos obrigatórios !</b></html>", "Alerta", JOptionPane.INFORMATION_MESSAGE);             
            } else {
                //
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "<html>Cliente adicionado com sucesso !</html>", "Concluído", JOptionPane.INFORMATION_MESSAGE);
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliFone.setText(null);
                    txtCliEmail.setText(null);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método para  pesquisar clientes pelo nome com filtro
    private void pesquisar_cliente() {
        String sql = "select idcli as Id, nomecli as Nome, endcli as Endereço, fone as Contato, emailcli as Email from tbclientes where nomecli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            /*Passando o conteúdo da caixa de pesquisa
            atenção ao "%" que é a continuação da String sql*/
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //Linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
            //Pretendo aprimorar na linha abaixo o fim da edição do Jtable
            //
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //Método para setar os campos do formulário com o conteúdo da tabela
    private void setar_campos() {
        //DEPOIS CORRIGIR FALHA DE CADASTRO, ONDE DESABILITA O ADICIONAR SE CLICADO NA TABELA
        int setar = tblClientes.getSelectedRow();
        lblCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEnd.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliFone.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliEmail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        btnCliAdicionar.setEnabled(false);
    }
    
    //Método verificar se a usuário registrado no banco de dados
    /* CÓDIGO PARA CORREÇÃO DE DADOS DUPLICADOS
    private void duplicidade(){
        //
        String sql = "select * from tbclientes where nomecli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1,txtCliNome.getText() + "%");
            ResultSet resultado =null;
            resultado = pst.setString(1, txtCliNome.getText());
            
        } catch (Exception e) {
        }
    }*/
    
    //Método abaixo altera os dados do cliente cadastrado no banco de dados
    private void alterar() {
        String sql = "update tbclientes set nomecli =?,endcli =?, fone=?,emailcli=? where idcli=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliFone.getText());
            pst.setString(4, txtCliEmail.getText());
            pst.setString(5, lblCliId.getText());

            if (txtCliNome.getText().isEmpty() || txtCliFone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor preencha todos os campos obrigatórios !", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int alterado = pst.executeUpdate();
                if (alterado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso !", "Concluído", JOptionPane.INFORMATION_MESSAGE);
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliFone.setText(null);
                    txtCliEmail.setText(null);
                    lblCliId.setText(null);
                    btnCliAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método remover os clientes do BD
    private void remover() {
        //Depois verificar a possibilidade de remoção do cliente sem a necessidade de usar a tabela de consulta
        if (!lblCliId.getText().contains("vazio")) {
            int confirma = JOptionPane.showConfirmDialog(null, "<html><h4>Tem certeza que deseja excluir o cliente  ?</h4></html>", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso !", "Concluído", JOptionPane.INFORMATION_MESSAGE);
                String sql = "delete from tbclientes where idcli=?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, lblCliId.getText());
                    pst.executeUpdate();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                txtCliNome.setText(null);
                txtCliEnd.setText(null);
                txtCliFone.setText(null);
                txtCliEmail.setText(null);
                lblCliId.setText("vazio");
                btnCliAdicionar.setVisible(true);
            }
        }else{
            JOptionPane.showMessageDialog(null,"<html>Utilize o campo de <b>busca</b> para excluír o cliente !</html>", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nome = new javax.swing.JLabel();
        endereco = new javax.swing.JLabel();
        contato = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        cObrigatorios = new javax.swing.JLabel();
        busca = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        txtCliNome = new javax.swing.JTextField();
        txtCliEnd = new javax.swing.JTextField();
        txtCliFone = new javax.swing.JTextField();
        txtCliEmail = new javax.swing.JTextField();
        btnCliAdicionar = new javax.swing.JButton();
        btnCliAlterar = new javax.swing.JButton();
        btnCliApagar = new javax.swing.JButton();
        painelEsconde = new javax.swing.JPanel();
        lblCliId = new javax.swing.JLabel();
        tbtClientes = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Clientes");
        setPreferredSize(new java.awt.Dimension(640, 480));

        nome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nome.setText("*Nome:");

        endereco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        endereco.setText("Endereço:");

        contato.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        contato.setText("*Contato:");

        email.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        email.setText("E-mail:");

        cObrigatorios.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        cObrigatorios.setText("*Campos Obrigatórios");

        busca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisaricone.png"))); // NOI18N

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        btnCliAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnCliAdicionar.setToolTipText("Adicionar");
        btnCliAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAdicionarActionPerformed(evt);
            }
        });

        btnCliAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update2.png"))); // NOI18N
        btnCliAlterar.setToolTipText("Alterar");
        btnCliAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAlterarActionPerformed(evt);
            }
        });

        btnCliApagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnCliApagar.setToolTipText("Apagar");
        btnCliApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliApagarActionPerformed(evt);
            }
        });

        lblCliId.setText("vazio");

        javax.swing.GroupLayout painelEscondeLayout = new javax.swing.GroupLayout(painelEsconde);
        painelEsconde.setLayout(painelEscondeLayout);
        painelEscondeLayout.setHorizontalGroup(
            painelEscondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelEscondeLayout.createSequentialGroup()
                .addComponent(lblCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        painelEscondeLayout.setVerticalGroup(
            painelEscondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCliId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Nome", "Endereço", "Contato", "E-mail"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        tbtClientes.setViewportView(tblClientes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(busca))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(tbtClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(nome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(painelEsconde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(endereco)
                        .addGap(4, 4, 4)
                        .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(contato)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(cObrigatorios))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCliAdicionar)
                .addGap(66, 66, 66)
                .addComponent(btnCliAlterar)
                .addGap(43, 43, 43)
                .addComponent(btnCliApagar)
                .addGap(108, 108, 108))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(busca))
                .addGap(11, 11, 11)
                .addComponent(tbtClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nome)
                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(painelEsconde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(endereco)
                    .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contato)
                    .addComponent(txtCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(email)
                    .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(cObrigatorios)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCliAdicionar)
                    .addComponent(btnCliAlterar)
                    .addComponent(btnCliApagar))
                .addGap(36, 36, 36))
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCliAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAdicionarActionPerformed
        //Chamando o método adicionar clientes
        adicionar();
    }//GEN-LAST:event_btnCliAdicionarActionPerformed
//Evento abaixo é do tipo "Enquanto for digitando"
    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        //Chamando o método pesquisar clientes
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased
    //Evento que será usado para setar a tabela clicando na tabela
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        //Chamando o método para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnCliAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAlterarActionPerformed
        //Chamando o método alterar clientes
        alterar();
    }//GEN-LAST:event_btnCliAlterarActionPerformed

    private void btnCliApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliApagarActionPerformed
        // Chamando o método de remover clientes
        remover();
    }//GEN-LAST:event_btnCliApagarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCliAdicionar;
    private javax.swing.JButton btnCliAlterar;
    private javax.swing.JButton btnCliApagar;
    private javax.swing.JLabel busca;
    private javax.swing.JLabel cObrigatorios;
    private javax.swing.JLabel contato;
    private javax.swing.JLabel email;
    private javax.swing.JLabel endereco;
    private javax.swing.JLabel lblCliId;
    private javax.swing.JLabel nome;
    private javax.swing.JPanel painelEsconde;
    private javax.swing.JTable tblClientes;
    private javax.swing.JScrollPane tbtClientes;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEnd;
    private javax.swing.JTextField txtCliFone;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    // End of variables declaration//GEN-END:variables
}
