package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// ���� ������ �г�
public class Setting extends JPanel
{
   private JButton btnBack;      // �ڷΰ��� ��ư
   private JPanel settingPanel;	// ���� �г�
   private JPanel imgPanel;	// ���� �̹��� �г�
   private JButton imgButton1;	// '�� ���� ����' ��ư
   private JButton imgButton2;	// '���̵�� �˻�' ��ư
   private JButton imgButton3;	// '��й�ȣ ����' ��ư
   private JButton imgButton4;	// 'ȸ�� Ż��' ��ư
   private JButton imgButton5;	// '���� ����' ��ư
   private Font f1;	// ��Ʈ
   
   public Setting()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
      setBackground(new Color(57,131,66));
      //setBackground(new Color(0,0,0));
      
      setLayout(null);
      
      // ���� �г�
      settingPanel=new JPanel();
      settingPanel.setBackground(new Color(154, 205, 50));
      settingPanel.setBounds(450,100, 400, 500);
      settingPanel.setLayout(null);
      
      // ���� �̹��� �г�
      imgPanel = new JPanel();
      imgPanel.setLayout(null);
      imgPanel.setBounds(10, 10, 380, 200);
      imgPanel.setBackground(new Color(154,205,50));
      settingPanel.add(imgPanel);
      imgPanel.setLayout(null);
  
      // ���� �̹���
		ImageIcon image = new ImageIcon(Main.class.getResource("../images/settings.png"));
		Image pre = image.getImage();
		Image scale = pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
      JLabel lblimage1 = new JLabel(new ImageIcon(scale));
      lblimage1.setBounds(0, 0, 380, 200);
      imgPanel.add(lblimage1);
      
      // ��ư���� �ؽ�Ʈ�� ���� ��Ʈ
      f1 = new Font("���� ���", Font.BOLD, 17);

      // ��ư�� ����
      imgButton1 = new JButton("�� ���� ����",new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton1.setHorizontalTextPosition(JButton.CENTER);
      imgButton1.setVerticalTextPosition(JButton.CENTER);
      imgButton1.setFont(f1);
      imgButton1.setBorderPainted(false);
      imgButton1.setBounds(50, 200, 300, 40);
      settingPanel.add(imgButton1);
      
      imgButton2 = new JButton("���̵�� �˻�", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton2.setBounds(50, 260, 300, 40);
      imgButton2.setHorizontalTextPosition(JButton.CENTER);
      imgButton2.setVerticalTextPosition(JButton.CENTER);
      imgButton2.setFont(f1);
      imgButton2.setBorderPainted(false);
      settingPanel.add(imgButton2);
      
      imgButton3 = new JButton("��й�ȣ ����", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton3.setBounds(50, 320, 300, 40);
      imgButton3.setHorizontalTextPosition(JButton.CENTER);
      imgButton3.setVerticalTextPosition(JButton.CENTER);
      imgButton3.setFont(f1);
      imgButton3.setBorderPainted(false);
      settingPanel.add(imgButton3);
      
      imgButton4 = new JButton("ȸ�� Ż��", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton4.setBounds(50, 380, 300, 40);
      imgButton4.setHorizontalTextPosition(JButton.CENTER);
      imgButton4.setVerticalTextPosition(JButton.CENTER);
      imgButton4.setFont(f1);
      imgButton4.setBorderPainted(false);
      settingPanel.add(imgButton4);
      
      imgButton5 = new JButton("���� ����", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton5.setBounds(50, 440, 300, 40);
      imgButton5.setHorizontalTextPosition(JButton.CENTER);
      imgButton5.setVerticalTextPosition(JButton.CENTER);
      imgButton5.setFont(f1);
      imgButton5.setBorderPainted(false);
      settingPanel.add(imgButton5);
      
      // ��ư�鿡 �׼� ������ ���
      MyActionListener listener = new MyActionListener();
      imgButton1.addActionListener(listener);
      imgButton2.addActionListener(listener);
      imgButton3.addActionListener(listener);
      imgButton4.addActionListener(listener);
      imgButton5.addActionListener(listener);
      
      /* �ڷΰ��� ��ư(�α׾ƿ�) */
		ImageIcon image1 = new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre1 = image1.getImage();
		Image scale1 = pre1.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		
		btnBack = new JButton(new ImageIcon(scale1));
		// ��ư ��� ����
		btnBack.setBorderPainted(false);
		// ��ư ��������
		btnBack.setContentAreaFilled(false);
		// ��Ŀ���� ��� ����
		btnBack.setFocusPainted(false);
		
		btnBack.addActionListener(listener);
		btnBack.setBounds(1050, 630, 200, 60);
		add(btnBack);
    /* �ڷΰ��� ��ư(�α׾ƿ�) */
      
       settingPanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
       this.add(settingPanel);

   }
  
   // 5��, �ڷ� ����(�α� �ƿ�) ��ư���� �׼� ������ Ŭ����
   public class MyActionListener implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
         Object obj = e.getSource();
         
         if (obj == imgButton1)
         {	// �� ���� ���� ��ư
        	(new Music("select.mp3", false)).start();
        	
        	// �� ���� ���� ȭ�� �ʱ�ȭ �� ��ȯ
	  		AppManager.getInstance().getMyProfilePanel().init();
	  		AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getMyProfilePanel());
         }
         else if (obj == imgButton2)
         {	// �ٸ� ��� ���� �˻� ��ư
        	 
        	 // �Է��� ���̵� 20�ڸ� �ʰ��� ��� ���Է�
            String resultStr = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr = JOptionPane.showInputDialog(Setting.this, "�˻��� ���̵� �Է��ϼ���." + System.lineSeparator() + "(20�� �̸�)");
        		if (resultStr == null) resultStr = "";
        	} while (20 < resultStr.length()); 

         	// �Է� ���ڸ� �ݰų�, ��ĭ�̸� �������� ����
            if (resultStr != null && !resultStr.equals("")) {
	  			(new Music("select.mp3", false)).start();
	  			// �ٸ� ��� ���� ���� ȭ�� �ʱ�ȭ �� ��ȯ
            	AppManager.getInstance().getOtherProfilePanel().init(resultStr);
            	AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getOtherProfilePanel());
            }
         }
         else if (obj == imgButton3)
         { 	// ��� ��ȣ ���� ��ư
        	 
        	 // �Է��� ���� ��й�ȣ�� 20�ڸ� �ʰ��� ��� ���Է�
            String resultStr1 = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr1 = JOptionPane.showInputDialog(Setting.this, "���� ��й�ȣ�� �Է��ϼ���." + System.lineSeparator() + "(20�� �̸�)");
        		if (resultStr1 == null) resultStr1 = "";
        	} while (20 < resultStr1.length());
            
         	// �Է� ���ڸ� �ݰų�, ��ĭ�̸� �������� ����
            if (resultStr1 == null || resultStr1.equals(""))
            	return;
            
            // �Է��� ������ ��й�ȣ�� 20�ڸ� �ʰ��� ��� ���Է�
            String resultStr2 = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr2 = JOptionPane.showInputDialog(Setting.this, "������ ��й�ȣ�� �Է��ϼ���." + System.lineSeparator() + "(20�� �̸�)");
        		if (resultStr2 == null) resultStr2 = "";
        	} while (20 < resultStr2.length());
            
            if (resultStr2 != null && !resultStr2.equals(""))
            {	// �Է� ���ڸ� �ݰų�, ��ĭ�̸� �������� ����
				try
				{
					// ��� ��ȣ ���� ������ ó�� ��û
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.CHANGE_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					
					// ���̵�, ���� ��й�ȣ, ������ ��й�ȣ�� ����
					p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getId()));
					p.println(Encryptor.encrypt(resultStr1));
					p.println(Encryptor.encrypt(resultStr2));
					p.flush();
					String result = b.readLine();
				
					if (result.equals(ServerConstants.CHANGE_SUCCESS)) {
						// ��й�ȣ ���� ����
			  			(new Music("select.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "�Ϸ�Ǿ����ϴ�.");
					}
					else if (result.equals(ServerConstants.CHANGE_FAIL)) {
						// ��й�ȣ ���� ����
			  			(new Music("error.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "���� ������ �������� �ʽ��ϴ�.");
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
         }
         else if (obj == imgButton4)
         {	// ȸ�� Ż�� ��ư
            int result = 0;
  			(new Music("select.mp3", false)).start();
            result = JOptionPane.showConfirmDialog(Setting.this, "ȸ�� Ż�� �Ͻðڽ��ϱ�?");
            
            if (result != JOptionPane.YES_OPTION)
            	return;
       
            // �Է��� ��й�ȣ�� 20�ڸ� �ʰ��� ��� ���Է�
            String resultStr = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr = JOptionPane.showInputDialog(Setting.this, "��й�ȣ�� �Է��ϼ���." + System.lineSeparator() + "(20�� �̸�)");
        		if (resultStr == null) resultStr = "";
        	} while (20 < resultStr.length());
            
            if (resultStr != null && !resultStr.equals(""))
            {	// �Է� ���ڸ� �ݰų�, ��ĭ�̸� �������� ����
				try
				{
					// ȸ�� Ż�� ������ ó�� ��û
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.WITHDRAWAL_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					
					// ID, ����Ű, �Է��� ��й�ȣ ����
					p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getId()));
					p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getKey()));
					p.println(Encryptor.encrypt(resultStr));
					p.flush();
					String rlt = b.readLine();
				
					if (rlt.equals(ServerConstants.WITHDRAWAL_SUCCESS))
					{	// Ż�� ����
			  			(new Music("select.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "ȸ�� Ż�� �Ϸ�Ǿ����ϴ�.");
						
						// �α� �ƿ�
						AppManager.getInstance().getUserInfo().getBr().close();
						AppManager.getInstance().getUserInfo().getPw().close();
						AppManager.getInstance().getUserInfo().getSocket().close();
						
						// �α��� ȭ������ ��ȯ
						AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getLoginPanel());
					}
					else if (rlt.equals(ServerConstants.WITHDRAWAL_FAIL))
					{	// Ż�� ����
			  			(new Music("error.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "���� ������ �������� �ʽ��ϴ�.");
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
         }
         else if (obj == imgButton5)
         {	// ���� ���� ��ư
	  		(new Music("select.mp3", false)).start();
	  		// ���� ������ ���� ȭ������ ��ȯ
        	 AppManager.getInstance().getPlayOnlinePanel().start();
            AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getPlayOnlinePanel());
         }
         else if (obj == btnBack)
         {	// �ڷ� ����(�α� �ƿ�)
	  		(new Music("select.mp3", false)).start();
        	 if (JOptionPane.showConfirmDialog(Setting.this, "�α� �ƿ� �Ͻðڽ��ϱ�?") == JOptionPane.YES_OPTION)
        	 {
 	  			(new Music("back.mp3", false)).start();
 	  			
        		 try
        		 {	// �α��ν��� ������ ���� ������ �α��� ������ ��������� ���� ����
        			 // �Է� ��� ������ �ش� �����忡�� ���� �߻� -> �α� �ƿ�/���� ������ �Ǻ� 
	        		 AppManager.getInstance().getUserInfo().getBr().close();
	        		 AppManager.getInstance().getUserInfo().getPw().close();
	        		 AppManager.getInstance().getUserInfo().getSocket().close();
        		 }
        		 catch (Exception ex)
        		 {
        			 ex.printStackTrace();
        		 }
        		 
        		 // �α��� �������� ��ȯ
                 AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getLoginPanel());
        	 }
         }
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // Setting Class