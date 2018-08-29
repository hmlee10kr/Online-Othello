package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

// �α��� ȭ��
public class Login extends JPanel
{
   private JButton btnBack;      // �ڷΰ��� ��ư
   private JButton btnLogin;	// �α��� ��ư
   private JButton btnJoin;	// ȸ�� ���� �гη� �̵��ϴ� ��ư
   private JPasswordField password;	// �н����� �Է�â
   private JTextField userID;	// ���̵� �Է�â
   private JLabel lblUserID;	// ID ��
   private JLabel lblUserPW;	// PW ��
   private JPanel loginPanel;	// ���� �г�
   private JPanel imgPanel;	// �����е� �̹��� �г�

   public Login()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
      setBackground(new Color(57,131,66));
      setLayout(null);
      
       // ���� �г�
      loginPanel=new JPanel();
      loginPanel.setBackground(new Color(154, 205, 50));
      loginPanel.setBounds(450,100, 400, 500);
      loginPanel.setLayout(null);     
      
      // �����е� �̹��� �г�
      imgPanel = new JPanel();
      imgPanel.setLayout(null);
      imgPanel.setBounds(10, 10, 380, 200);
      imgPanel.setBackground(new Color(154,205,50));
      loginPanel.add(imgPanel);
      imgPanel.setLayout(null);
  
      // �����е� �̹���
      ImageIcon image = new ImageIcon(Main.class.getResource("../images/game.png"));
       Image pre = image.getImage();
       Image scale = pre.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);     
      
      JLabel lblimage1 = new JLabel(new ImageIcon(scale));
      lblimage1.setBounds(0, 0, 380, 200);
      imgPanel.add(lblimage1);
      
      // ID ��
      lblUserID = new JLabel("ID");
      lblUserID.setBounds(50,230,50,25);
      lblUserID.setFont(new Font("SansSerif", Font.BOLD, 30));
      loginPanel.add(lblUserID);
     
      // PW ��
      lblUserPW = new JLabel("PW");
       lblUserPW.setBounds(50, 300, 50, 25);
       lblUserPW.setFont(new Font("SansSerif", Font.BOLD, 30));
       loginPanel.add(lblUserPW);
       
       // ���� 20�� ������ ���̵� �Է� �ʵ�
      userID = new JTextField(20);
      userID.setDocument(new JTextFieldLimit(20));
       userID.setBounds(130, 230, 160, 30);
       loginPanel.add(userID);

       // ���� 20�� ������ �н����� �Է� �ʵ�
       password = new JPasswordField(20);
       password.setDocument(new JTextFieldLimit(20));
       password.setBounds(130,300, 160, 30);
       loginPanel.add(password);

       // �ڷ� ����, ȸ�� ����, �α��� ��ư �׼� ������
       MyActionListener listener = new MyActionListener();
       
       /* �ڷ� ���� */
		ImageIcon image1 = new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre1 = image1.getImage();
		Image scale1 = pre1.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		
		btnBack = new JButton(new ImageIcon(scale1));
		btnBack.setBorderPainted(false);
		btnBack.setContentAreaFilled(false);
		btnBack.setFocusPainted(false);
		btnBack.addActionListener(listener);
		btnBack.setBounds(1050, 630, 200, 60);
	    add(btnBack);
	   /* �ڷ� ���� */
	    
	    /* �α��� */
      ImageIcon image2 = new ImageIcon(Main.class.getResource("../images/login1.png"));
      Image pre2 = image2.getImage();
      Image scale2 = pre2.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
       btnLogin = new JButton(new ImageIcon(scale2));
       btnLogin.setBorderPainted(false);
       btnLogin.setContentAreaFilled(false);
       btnLogin.setFocusPainted(false);
       btnLogin.setBounds(280,390,100,100);
       btnLogin.addActionListener(listener);
       loginPanel.add(btnLogin);
	    /* �α��� */
     
      /* ȸ�� ���� */
      ImageIcon image3 = new ImageIcon(Main.class.getResource("../images/add-user.png"));
      Image pre3 = image3.getImage();
      Image scale3 = pre3.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
      btnJoin = new JButton(new ImageIcon(scale3));
      btnJoin.setBorderPainted(false);
      btnJoin.setContentAreaFilled(false);
      btnJoin.setFocusPainted(false);
      btnJoin.addActionListener(listener);
      btnJoin.setBounds(20,390,100,100);
      loginPanel.add(btnJoin);     
      /* ȸ�� ���� */ 
       
      loginPanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
      this.add(loginPanel);
   }
   
   public class MyActionListener implements ActionListener{

      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
    	  Object obj = e.getSource();
    	  
    	  if (obj == btnJoin)
    	  {	// ȸ�� ���� ��ư
  			(new Music("select.mp3", false)).start();
  			
  			// ȸ�� ���� ȭ������ ��ȯ
	         AppManager.getInstance().getMainFrame().switchPanel(Login.this, AppManager.getInstance().getJoinPanel());
	         
 			userID.setText("");
 			password.setText("");
    	  }
    	  else if (obj == btnLogin)
    	  {		// �α��� ��ư
    		  
    		  // �� ĭ�� �����ҽ� ��ȭ ���� ���
    		  if (userID.getText().equals("") && password.getText().equals("")) {
    	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Login.this, "���� ������ �Է����ּ���.");
    			  return;
    		  }
    		  else if (userID.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Login.this, "���̵� �Է����ּ���.");
    			  return;
    		  }
    		  else if (password.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Login.this, "�н����带 �Է����ּ���.");
    			  return;
    		  }
    		  
				try
				{
					// �α��� ������ ó�� ��û
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.LOGIN_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					
					// �Ƶ���, �н����� ����
					p.println(Encryptor.encrypt(userID.getText()));
					p.println(Encryptor.encrypt(password.getText()));
					p.flush();
					String result = b.readLine();
				
					if (result.equals(ServerConstants.LOGIN_INUSE))
					{	// �ش� ������ �̹� �����
	    	  			(new Music("error.mp3", false)).start();
		    			JOptionPane.showMessageDialog(Login.this, "������ �̹� ������Դϴ�.");
					
						try
						{
							p.close();
							b.close();
							s.close();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
					else if (result.equals(ServerConstants.LOGIN_SUCCESS))
					{	// �α��� ����
	    	  			(new Music("select.mp3", false)).start();
	    	  			
	    	  			// �α��� ������ ������ ����Ű ����
						String key = Encryptor.decrypt(b.readLine());
		    			JOptionPane.showMessageDialog(Login.this, "�α��� ����.");
		    			
						AppManager.getInstance().getUserInfo().setId(userID.getText());
						AppManager.getInstance().getUserInfo().setKey(key);
						
						// �α��� ���������� �� ������ �̿��� �Է� ��� ������ �����带 �����
						// ����ڰ� �α� �ƿ�/���� �Ͽ� �� ������ ������, ���� ó���� �̿��� �α� �ƿ����� �Ǻ��ϰ� �ȴ�.
						AppManager.getInstance().getUserInfo().setSocket(s);
						AppManager.getInstance().getUserInfo().setBr(b);
						AppManager.getInstance().getUserInfo().setPw(p);
						
						// ���� ������ ȭ������ ��ȯ
						AppManager.getInstance().getMainFrame().switchPanel(Login.this, AppManager.getInstance().getSettingPanel());
					
		    			userID.setText("");
		    			password.setText("");
					}
					else if (result.equals(ServerConstants.LOGIN_FAIL))
					{	// �α��� ����
	    	  			(new Music("error.mp3", false)).start();
		    			JOptionPane.showMessageDialog(Login.this, "���� ������ �������� �ʽ��ϴ�.");
							
						try
						{
							p.close();
							b.close();
							s.close();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}   
    	  }
		else if (obj == btnBack)
		{	// �ڷΰ��� ��ư
			(new Music("back.mp3", false)).start();
			userID.setText("");
			password.setText("");

			// ��ǻ��, �¶��� ���� ���� ȭ������ ��ȯ
			AppManager.getInstance().getMainFrame().switchPanel(Login.this, AppManager.getInstance().getSelectPanel());
		}
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // Login Class