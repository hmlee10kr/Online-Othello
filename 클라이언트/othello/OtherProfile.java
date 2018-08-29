package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// �ٸ� ��� ���� ���� ȭ��
public class OtherProfile extends JPanel
{
   private JButton btnSearch;      // ���̵�� �ٸ� ��� ���� �˻� ��ư
   private MyActionListener listener;   // ��ư Ŭ�� ������
   private JButton btnBack;	// �ڷ� ���� ��ư
   private JPanel otherProfilePanel;	// ���� �г�
   private JPanel imgPanel;	// �� ��� �̹��� �г�
   private Font f1;	// ��Ʈ
   private JLabel lbl0,lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9,lbl10;	// ���� ǥ�ÿ� ���� �󺧵�
   
   public OtherProfile()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
         setBackground(new Color(57,131,66));
         //setBackground(new Color(0,0,0));
         
         setLayout(null);
         
         // ���� �г�
         otherProfilePanel=new JPanel();
         otherProfilePanel.setBackground(new Color(154, 205, 50));
         otherProfilePanel.setBounds(450,100, 400, 500);
         otherProfilePanel.setLayout(null);

         // �� ��� �̹��� �г�
         imgPanel = new JPanel();
         imgPanel.setLayout(null);
         imgPanel.setBounds(10, 10, 380, 170);
         imgPanel.setBackground(new Color(154,205,50));
         otherProfilePanel.add(imgPanel);
         imgPanel.setLayout(null);
         
         // �� ��� �̹���
         ImageIcon image = new ImageIcon(Main.class.getResource("../images/team.png"));
          Image pre = image.getImage();
          Image scale = pre.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);         
         
         JLabel lblimage1 = new JLabel(new ImageIcon(scale));
         lblimage1.setBounds(0, 0, 380, 200);
         imgPanel.add(lblimage1);
         
         // �󺧵��� �ؽ�Ʈ�� ���� ��Ʈ
         f1 = new Font("���� ���", Font.BOLD, 17);

         // ���̵�� ���� �˻�, �ڷ� ���� ��ư �׼� ������
         listener = new MyActionListener();
         
         lbl0 = new JLabel("[       ]���� ����");	// ���̵�
         lbl0.setBounds(50,195,150,25);
         otherProfilePanel.add(lbl0);
         lbl0.setFont(f1);
         
         lbl1 = new JLabel("�� �÷��� Ƚ�� :");	// �� �÷��� Ƚ��
         lbl1.setBounds(50,230,150,25);
         otherProfilePanel.add(lbl1);
         lbl1.setFont(f1);
         
         lbl2 = new JLabel("�¸� Ƚ�� :");	// �¸� Ƚ��
         lbl2.setBounds(50,260,150,25);
         otherProfilePanel.add(lbl2);
         lbl2.setFont(f1);
         
         lbl3 = new JLabel("�й� Ƚ�� :");	// �й� Ƚ��
         lbl3.setBounds(50,290,150,25);
         otherProfilePanel.add(lbl3);
         lbl3.setFont(f1);
         
         lbl4 = new JLabel("�·� :");	// �·�
         lbl4.setBounds(50,320,150,25);
         otherProfilePanel.add(lbl4);
         lbl4.setFont(f1);
         
         lbl5 = new JLabel("Ż�� Ƚ�� :");	// Ż�� Ƚ��
         lbl5.setBounds(50,350,150,25);
         otherProfilePanel.add(lbl5);
         lbl5.setFont(f1);
         
         lbl6 = new JLabel("5ȸ");	// �� �÷��� Ƚ��
         lbl6.setBounds(210,230,50,25);
         otherProfilePanel.add(lbl6);
         lbl6.setFont(f1);
         
         lbl7 = new JLabel("5ȸ");	// �¸� Ƚ��
         lbl7.setBounds(210,260,150,25);
         otherProfilePanel.add(lbl7);
         lbl7.setFont(f1);
         
         lbl8 = new JLabel("5ȸ");	// �й� Ƚ��
         lbl8.setBounds(210,290,150,25);
         otherProfilePanel.add(lbl8);
         lbl8.setFont(f1);
         
         lbl9 = new JLabel("20%");	// �·�
         lbl9.setBounds(210,320,150,25);
         otherProfilePanel.add(lbl9);
         lbl9.setFont(f1);
         
         lbl10 = new JLabel("3ȸ");	// Ż�� Ƚ��
         lbl10.setBounds(210,350,150,25);
         otherProfilePanel.add(lbl10);
         lbl10.setFont(f1);
         
          /* �ڷ� ���� */
         ImageIcon image2 = new ImageIcon(Main.class.getResource("../images/delete.png"));
         Image pre2 = image2.getImage();
         Image scale2 = pre2.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
         
          btnBack = new JButton(new ImageIcon(scale2));
          btnBack.setBorderPainted(false);
          btnBack.setContentAreaFilled(false);
          btnBack.setFocusPainted(false);
          btnBack.setBounds(280,390,100,100);
          btnBack.addActionListener(listener);
          otherProfilePanel.add(btnBack);
          /* �ڷ� ���� */
          
          /* ���̵�� �ٸ� ��� ���� �˻� */
          ImageIcon image3 = new ImageIcon(Main.class.getResource("../images/magnifier.png"));
         Image pre3 = image3.getImage();
         Image scale3 = pre3.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
         
         btnSearch = new JButton(new ImageIcon(scale3));
         btnSearch.setBorderPainted(false);
         btnSearch.setContentAreaFilled(false);
         btnSearch.setFocusPainted(false);
          btnSearch.setBounds(20,390,100,100);
          btnSearch.addActionListener(listener);
         otherProfilePanel.add(btnSearch);
         /* ���̵�� �ٸ� ��� ���� �˻� */
         
          otherProfilePanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
          this.add(otherProfilePanel);
      }

   // ID �� �ش��ϴ� �������� �ʱ�ȭ
   	public void init(String id)
   	{
		try
		{
			// ���� �˻� ������ ó�� ��û
			Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.SEARCH_SERV_PORT);
			BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
			PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
			String result;

			// ���ڷ� ���� ���̵� ����
			p.println(Encryptor.encrypt(id));
			p.flush();
			
			result = b.readLine();
			if (result.equals(ServerConstants.SEARCH_SUCCESS))
			{	// �˻� ����
				lbl0.setText("[ " + id + " ] ���� ����");	// �� �÷��� Ƚ��
				lbl6.setText(b.readLine() + "ȸ");
				lbl9.setText(String.format("%.4f", Double.parseDouble(b.readLine())) + "%");	// �·�
				lbl7.setText(b.readLine() + "ȸ");	// �¸� Ƚ��
				lbl8.setText(b.readLine() + "ȸ");	// �й� Ƚ��
				lbl10.setText(b.readLine() + "ȸ");	// Ż�� Ƚ��
			}
			else if (result.equals(ServerConstants.SEARCH_FAIL))
			{	// �˻� ����
				lbl0.setText("[  ] ���� ����");
				lbl6.setText("");
				lbl7.setText("");
				lbl8.setText("");
				lbl9.setText("");
				lbl10.setText("");
			}
			
			b.close();
			p.close();
			s.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}  		
   	}

   public class MyActionListener implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
         Object obj = e.getSource();
         
         if (obj == btnBack)
         {	// �ڷ� ����
	  		(new Music("back.mp3", false)).start();
	  		
	  		// ���� ������ ȭ������ ��ȯ
            AppManager.getInstance().getMainFrame().switchPanel(OtherProfile.this, AppManager.getInstance().getSettingPanel());
         }
         else if(obj == btnSearch)
         {	// ���̵�� ���� �˻�
        	 
        	 // �Է��� ���̵� 20�ڸ� �ʰ��ϸ� ���Է�
        	String resultStr = null;
        	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr = JOptionPane.showInputDialog(OtherProfile.this, "�˻��� ���̵� �Է��ϼ���." + System.lineSeparator() + "(20�� �̸�)");
        		if (resultStr == null) resultStr = "";
        	} while (20 < resultStr.length());

            // �Է� ���ڸ� �ݰų�, ��ĭ�̸� �������� ����
    		if (resultStr != null && !resultStr.equals("")) init(resultStr);
         }
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // OtherProfile Class