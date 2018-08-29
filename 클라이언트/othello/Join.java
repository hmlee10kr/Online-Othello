package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// ȸ�� ���� ȭ��
public class Join extends JPanel
{
   private JButton btnBack;      // �ڷΰ��� ��ư
   private MyActionListener listener;   // �ڷΰ���, ȸ�� ���� ��ư Ŭ�� ������
   private JButton btnJoin;// ȸ�� ���� ��ư
   private JPasswordField password;	// �н����� �Է�â
   private JTextField userID;	// ���̵� �Է�â
   private JLabel lblUserID;	// ID ��
   private JLabel lblUserPW;	// PW ��
   private JPanel joinPanel;	// ���� �г�
   private JPanel imgPanel;	// ��ũ �̹��� �г�
   
   public Join()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
      setBackground(new Color(57,131,66));
      //setBackground(new Color(0,0,0));
      
      setLayout(null);
      
      // ���� �г�
      joinPanel=new JPanel();
      joinPanel.setBackground(new Color(154, 205, 50));
      joinPanel.setBounds(450,100, 400, 500);
      joinPanel.setLayout(null);
      
      // ��ũ �̹��� �г�
      imgPanel = new JPanel();
      imgPanel.setLayout(null);
      imgPanel.setBounds(10, 10, 380, 200);
      imgPanel.setBackground(new Color(154,205,50));
      joinPanel.add(imgPanel);
      imgPanel.setLayout(null);
      
      // ��ũ �̹���
      ImageIcon image = new ImageIcon(Main.class.getResource("../images/link3.png"));
       Image pre = image.getImage();
       Image scale = pre.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
      
      JLabel lblimage1 = new JLabel(new ImageIcon(scale));
      lblimage1.setBounds(0, 0, 380, 200);
      imgPanel.add(lblimage1);

      listener = new MyActionListener();
      
  		// ID ��
      lblUserID = new JLabel("ID");
      lblUserID.setBounds(50,230,50,25);
      lblUserID.setFont(new Font("SansSerif", Font.BOLD, 30));
      joinPanel.add(lblUserID);
      
      // PW ��
      lblUserPW = new JLabel("PW");
       lblUserPW.setBounds(50, 300, 50, 25);
       lblUserPW.setFont(new Font("SansSerif", Font.BOLD, 30));
       joinPanel.add(lblUserPW);
       
       // ���� 20�� ������ ���̵� �Է� �ʵ�
      userID = new JTextField(20);
      userID.setDocument(new JTextFieldLimit(20));
       userID.setBounds(130, 230, 160, 30);
       joinPanel.add(userID);

       // ���� 20�� ������ �н����� �Է� �ʵ�
       password = new JPasswordField(20);
       password.setDocument(new JTextFieldLimit(20));
       password.setBounds(130,300, 160, 30);
       joinPanel.add(password);
       
       /* ȸ�� ���� */
      ImageIcon image2 = new ImageIcon(Main.class.getResource("../images/check.png"));
      Image pre2 = image2.getImage();
      Image scale2 = pre2.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
       btnJoin = new JButton(new ImageIcon(scale2));
       btnJoin.setBorderPainted(false);
       btnJoin.setContentAreaFilled(false);
       btnJoin.setFocusPainted(false);
       btnJoin.setBounds(280,390,100,100);
       btnJoin.addActionListener(listener);
       joinPanel.add(btnJoin);
       /* ȸ�� ���� */
       
       /* �ڷ� ���� */
       ImageIcon image3 = new ImageIcon(Main.class.getResource("../images/delete.png"));
      Image pre3 = image3.getImage();
      Image scale3 = pre3.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
      btnBack = new JButton(new ImageIcon(scale3));
      btnBack.setBorderPainted(false);
      btnBack.setContentAreaFilled(false);
      btnBack.setFocusPainted(false);
       btnBack.setBounds(20,390,100,100);
       btnBack.addActionListener(listener);
      joinPanel.add(btnBack);
      /* �ڷ� ���� */
      
      joinPanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
      this.add(joinPanel);    
   }

   public class MyActionListener implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
    	  Object obj = e.getSource();
    	  
    	  if (obj == btnBack)
    	  {		// �ڷ� ����
	  			(new Music("back.mp3", false)).start();
	  			userID.setText("");
	  			password.setText("");
	  			
	  			// �α��� ȭ������ ��ȯ
		        AppManager.getInstance().getMainFrame().switchPanel(Join.this, AppManager.getInstance().getLoginPanel());
    	  }
    	  else if (obj == btnJoin)
    	  {		// ȸ�� ����
    		  
    		  // �� ĭ�� �����ҽ� ��ȭ ���� ���
    		  if (userID.getText().equals("") && password.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Join.this, "���� ������ �Է����ּ���.");
    			  return;
    		  }
    		  else if (userID.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Join.this, "���̵� �Է����ּ���.");
    			  return;
    		  }
    		  else if (password.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Join.this, "�н����带 �Է����ּ���.");
    			  return;
    		  }
    		  
				try
				{
					// ȸ�� ���� ������ ó�� ��û
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.REGISTER_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					String result;
					
					// ���̵�, �н����� ����
					p.println(Encryptor.encrypt(userID.getText()));
					p.println(Encryptor.encrypt(password.getText()));
					p.flush();
					
					result = b.readLine();
					if (result.equals(ServerConstants.REGISTER_SUCCESS))
					{	// ȸ�� ���� ����
	    	  			(new Music("select.mp3", false)).start();
		    			JOptionPane.showMessageDialog(Join.this, "ȸ������ ����.");	
		    			
		    			// �α��� ������ ȸ�� ������ ������ �α��� ��û
						Socket sock = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.LOGIN_SERV_PORT);
						BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF8"));
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF8"));	
						
						// ��� �Է��� ���̵�, �н����� ����
						pw.println(Encryptor.encrypt(userID.getText()));
						pw.println(Encryptor.encrypt(password.getText()));
						pw.flush();
						String rlt = br.readLine();
					
						if (rlt.equals(ServerConstants.LOGIN_SUCCESS))
						{	// �α��� ����
							
		    	  			// �α��� ������ ������ ����Ű ����
							String key = Encryptor.decrypt(br.readLine());
			    			
							AppManager.getInstance().getUserInfo().setId(userID.getText());
							AppManager.getInstance().getUserInfo().setKey(key);
							
							// �α��� ���������� �� ������ �̿��� �Է� ��� ������ �����带 �����
							// ����ڰ� �α� �ƿ�/���� �Ͽ� �� ������ ������, ���� ó���� �̿��� �α� �ƿ����� �Ǻ��ϰ� �ȴ�.							
							AppManager.getInstance().getUserInfo().setSocket(sock);
							AppManager.getInstance().getUserInfo().setBr(br);
							AppManager.getInstance().getUserInfo().setPw(pw);

							// ���� ������ ȭ������ ��ȯ
							AppManager.getInstance().getMainFrame().switchPanel(Join.this, AppManager.getInstance().getSettingPanel());
							
			    			userID.setText("");
			    			password.setText("");
						}
					}
					else if (result.equals(ServerConstants.REGISTER_EXIST))
					{	// �ߺ��� ���̵� ����
	    	  			(new Music("error.mp3", false)).start();
		    			JOptionPane.showMessageDialog(Join.this, "�ߺ��� ���̵� �����մϴ�.");
					}
						
					b.close();
					p.close();
					s.close();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
    	  }
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // Join Class