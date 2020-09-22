package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
//import br.com.infox.telas.TelaLoad;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import javax.swing.JOptionPane;
//Linha abaixo importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;

public final class TelaLogin extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int flagBtnHint = 0;
    final TelaLoad carregando = new TelaLoad();

    public TelaLogin() {
        initComponents();
        
//        Thread
//        
//        carregando.setVisible(true);
//        Thread t = new Thread();
//        t.start();

        //Linha abaixo atribui ajustes ao campo txtusuario o retorno apresenta na ação do txtUsuario -Leo
        txtUsuario.setEditable(false);
        txtUsuario.setText("Digite o seu login...");
        txtUsuario.setForeground(Color.gray);

        //Linha abaixo atribui ajustes ao campo txtSenha -Leo
        txtSenha.setEditable(false);
        txtSenha.setEchoChar('\u0000');
        txtSenha.setText("Digite a sua senha...");
        txtSenha.setForeground(Color.gray);
        btnVisible.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/btnVisibleTrue.png")));

        //Mostra se a conexão com Banco de Dados foi estabelecida
        conexao = ModuloConexao.conector();
        if (conexao != null) {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/databaseok.png")));

            /*Linha abaixo é outra forma de visualizar - Leo
            lblStatus.setText("<html><font size=1 color=green>ON</font></html>");*/
        } else {
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/databaseerror.png")));

            /*Linha abaixo é outra forma de visualizar - Leo
            lblStatus.setText("<html><font size=1 color=red>OFF</font></html>");*/
        }
        /*Linha abaixo serve de apoio aos status da conexão
        System.out.println(conexao);*/
    }

    public void logar() {
        String sql = "select * from tbusuarios where login =? and senha=?";
        try {
            /*Linhas abaixo preparam a consulta ao Banco de dados em função do que
        foi digitado nas caixas de texto. O "?" é substituido pelo conteúdo
        das variavéis
             */
            pst = conexao.prepareStatement(sql);
            //Caso ocorra um erro no futuro verificar a posição do admin no mysql -Leo
            pst.setString(1, txtUsuario.getText());
            pst.setString(2, txtSenha.getText());
            //Linha abaixo executa a query
            rs = pst.executeQuery();
            //Se existir senha correspondente
            if (rs.next()) {
                ///Linha abaixo obtem o conteúdo do campo perfil da tabela tbusuarios
                String perfil = rs.getString(6);
                String login = rs.getString(4);
                //System.out.println(perfil);

                //Linha abaixo exibe o conteúdo do campo da tabela
                TelaPrincipal principal = new TelaPrincipal();
                principal.setVisible(true);
                //Linha abaixo apresenta o nome do usuário na tela principal
                TelaPrincipal.lblUsuario.setText(rs.getString(2));
                this.dispose();
                conexao.close();
                /*Estrutura abaixo realiza o tratamento do perfil do usuário
                Caso seja necessário criar outros tipos de perfil, será necessário aumentar essa estrutura*/
                if (perfil.equals("admin")) {
                    TelaPrincipal.MenRel.setEnabled(true);
                    TelaPrincipal.MenCadUsu.setEnabled(true);
                    TelaPrincipal.lblUsuario.setForeground(Color.red);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário e/ou senha inválido(s)");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

// Futuramente colocar uma thread 
//    public void run() {
//
//        for (int i = 0; i <= 4; i++) {
//        }
//        carregando.dispose();
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        btnVisible = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("X System - Login");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/iconX.png")).getImage());
        setPreferredSize(new java.awt.Dimension(350, 250));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Usuário:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 66, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Senha:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 107, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Acesso ao Sistema");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 11, -1, -1));

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUsuarioMouseClicked(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 51, 200, 32));

        btnLogin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLogin.setText("Login");
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginKeyPressed(evt);
            }
        });
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 162, 70, 35));

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(153, 153, 153));
        lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/databaseerror.png"))); // NOI18N
        getContentPane().add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 162, 65, -1));

        txtSenha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSenha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSenhaMouseClicked(evt);
            }
        });
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSenhaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSenhaKeyTyped(evt);
            }
        });
        getContentPane().add(txtSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 101, 200, 30));

        btnVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisibleActionPerformed(evt);
            }
        });
        getContentPane().add(btnVisible, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 40, 30));

        setSize(new java.awt.Dimension(375, 265));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // "Chamando"o método Logar
        logar();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsuarioMouseClicked
        //Linha abaixo permite que o campo de texto se apague devido evento -Leo
        boolean flagTxtHint = false;
        //Linha abaixo traz condição para que o evento de clique aconteça uma vez -Paulo
        if ((flagTxtHint == false) && (txtUsuario.getText().contains("Digite o seu login..."))) {
            txtUsuario.setText(null);
            flagTxtHint = true;
        }

        txtUsuario.setEditable(true);
        txtUsuario.setForeground(Color.black);

    }//GEN-LAST:event_txtUsuarioMouseClicked
    private void txtSenhaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSenhaMouseClicked
//Linha abaixo possui flag para contenção do evento - Leo
        boolean flagTxtHint = false;
        if (flagTxtHint == false && txtSenha.getText().contains("Digite a sua senha...")) {
            flagTxtHint = true;
            txtSenha.setText(null);
            txtSenha.setEchoChar('\u25cf');
        }
        txtSenha.setEditable(true);
        txtSenha.setForeground(Color.black);
    }//GEN-LAST:event_txtSenhaMouseClicked

    private void txtSenhaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyTyped
        // Estrutura impede a utilização do space como forma de segurança do software
        String caracteres = " ";
        if (caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
            JOptionPane.showMessageDialog(null, "<html>Você não pode usar este caracter <b>(espaço)</b></html>", "Atenção", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtSenhaKeyTyped

    private void btnVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisibleActionPerformed
        // Evt para visualizar senha
        if (btnVisible.isSelected()) {
            txtSenha.setEchoChar('\u0000');
            //Essa Linha resolver o problema de alteração do campo txtSenha antes de receber null
        } else if (!txtSenha.getText().contains("Digite a sua senha...")) {
            txtSenha.setEchoChar('\u25cf');
        }
    }//GEN-LAST:event_btnVisibleActionPerformed

    private void btnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyPressed
        // Evento será usado para acionar teclado para usar a tecla enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            logar();
        }
    }//GEN-LAST:event_btnLoginKeyPressed

    private void txtSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyPressed
        // Evento utiliza tecla ESC para limpar campo de texto
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txtSenha.setText(null);
            txtSenha.setEditable(true);
            txtSenha.setEchoChar('\u25cf');
            txtSenha.setForeground(Color.black);
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnLogin.requestFocus();
        }
    }//GEN-LAST:event_txtSenhaKeyPressed

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        // Evento utiliza tecla ESC para limpar campo de texto
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txtUsuario.setText(null);
            txtUsuario.setEditable(true);
            txtUsuario.setForeground(Color.black);
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtSenha.requestFocus();
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JToggleButton btnVisible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
