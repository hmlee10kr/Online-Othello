package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*
	������ ������ �����Ѵ�.
	
	������ �ڽ��� ���϶� ���� �� �� �ְ�
	���� ��ġ ������ �����ϸ�,
	ó�� ����� ���� �ٵϵ��� ���¸� ���� �޴´�.
*/

// �¶��� ������ �÷��� ȭ��
public class PlayReversiOnline extends JPanel
{	
	private JButton btnBack;						// �ڷΰ��� ��ư
	private JLabel lblWhiteCount, lblBlackCount;	// ��, �������� ���� ���� ǥ�ø� ���� ��

	private JLabel userOne;	// ������ ���̵�
	private JLabel userTwo;	// ������ ���̵�
	
	// ��Ī �������� ���� ���� (��Ī�� �Ϸ�ǰ� ������ ������ ���� �� ������ ����Ѵ�.)
	private Socket clnt;
	private BufferedReader br;
	private PrintWriter pw;
	
	private boolean bEnd;	// ������ ���Ḧ ��Ÿ���� ����
	private boolean bMyTurn;	// �ڽ��� ������ ��Ÿ���� ����
	private String myColor;;	// ���� �� ���� ��Ÿ���� ����
	private String myId, vsId;	// ���� ���̵�, ����� ���̵� �����ϴ� ����
	
	// �������� ���۵� 8x8 �� 64 ������ ���ڿ��� �� �ٵ����� ���¸� �����ϴ� ���� (EEEEBWEEBBWW...)
	private String encodedStatus;
	
	// ä�� �������� ���� ����
	Socket chatSock;
	BufferedReader chatBr;
	PrintWriter chatPw;
	
	// ä�� ������ ǥ�õǴ� ����, ä�� �Է� �ʵ�
	private JTextArea txtArea;
	private JTextField txt;	
	
	public PlayReversiOnline()
	{
		setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
		setBackground(new Color(57,131,66));
		setLayout(null);
		
		// ������ ����, �� ���� ǥ�ø� ���� �󺧵� �ʱ�ȭ
		lblWhiteCount = new JLabel();
		lblBlackCount = new JLabel();
		lblWhiteCount.setForeground(Color.white);
		lblBlackCount.setForeground(Color.black);
		lblWhiteCount.setFont(new Font("Consolas",Font.BOLD,120));
		lblBlackCount.setFont(new Font("Consolas",Font.BOLD,120));
		lblWhiteCount.setBounds(735,20,240,180);
		lblBlackCount.setBounds(985,20,240,180);
		lblWhiteCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlackCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblWhiteCount.setVerticalAlignment(SwingConstants.CENTER);
		lblBlackCount.setVerticalAlignment(SwingConstants.CENTER);
		
		// ���� ���̵� �󺧵� �ʱ�ȭ
		userOne = new JLabel();
		userTwo = new JLabel();
		userOne.setForeground(Color.orange);
		userTwo.setForeground(Color.orange);
		userOne.setFont(new Font("Consolas", Font.BOLD, 40));
		userTwo.setFont(new Font("Consolas", Font.BOLD, 40));
		userOne.setBounds(780, 215, 240, 40);
		userTwo.setBounds(1030, 215, 240, 40);

		// �ڷ� ���� ��ư
		ImageIcon image = new ImageIcon(Main.class.getResource("../images/back.png"));
		btnBack = new JButton(new ImageIcon(image.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH)));
		btnBack.setBorderPainted(false);
		btnBack.setContentAreaFilled(false);
		btnBack.setFocusPainted(false);
		btnBack.addActionListener(new BackListener());	// �ڷΰ��� ��ư ������
		btnBack.setBounds(1050,630,200,60);
		
		/* ä�� */
		txtArea = new JTextArea("", 10, 10);
		txtArea.setFont(new Font("SansSerif", Font.BOLD, 18));
		JScrollPane jsp = new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
											JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		txt = new JTextField(10);
		txt.setFont(new Font("SansSerif", Font.BOLD, 18));
		txt.addActionListener(new ChatListener());

		jsp.setBounds(670, 280, 560, 280);
		txt.setBounds(670, 580, 560, 40);
		add(jsp);
		add(txt);
		/* ä�� */
		
		add(btnBack);
		add(lblWhiteCount);
		add(lblBlackCount);
		add(userOne);
		add(userTwo);
		addMouseListener(new BoardListener());	// �ٵ��ǿ��� Ŭ���� ��ġ�� �Ǻ��ϰ� ������ �����ϱ� ���� �̺�Ʈ ������
	} // PlayReversiOnline Constructor
	
	// �ʱ�ȭ
	public void start()
	{
		// �� ���� ��
		lblWhiteCount.setText("0");
		lblBlackCount.setText("0");
		lblWhiteCount.setBorder(null);
		lblBlackCount.setBorder(null);
		
		// ���� ���̵� ��
		userOne.setText("User 1");
		userTwo.setText("User 2");
		
		// ä��
		txt.setText("");
		txtArea.setText("");
		txt.setEnabled(false);
		txtArea.setEditable(false);
		
		// �ʱ⿡�� �ٵ��� �ܿ� �ƹ��͵� �׸��� �ʱ� ����
		encodedStatus = "";
		for (int i=1; i<=Reversi.BOARD_SIZE; i++)
			encodedStatus += "EEEEEEEE";
		
		vsId = "";
		bEnd = true;
		repaint();
		
		chatSock = null;
		chatBr = null;
		chatPw = null;
		
		// ��Ī ������ ��⿭�� ��� ��û
		if (connectServer())
		{	// ����� ���������� �Ϸ��
			
			// �� ������� ��Ī�� �Ϸ�� ����, ������ �����
			// �������� ���۵Ǵ� ó�� ���, �ٵ����� ���¸� ��������
			// ȭ��, ��, �������� ������ ����Ѵ�.
			(new Thread() {
				@Override
				public void run()
				{
					try
					{
						myId = AppManager.getInstance().getUserInfo().getId();
						// ��Ī�� �Ϸ�Ǹ�, ������
						// ������ ���̵�, ���� ����, �ٵ����� �ʱ� ���¸� �����Ѵ�
						vsId = Encryptor.decrypt(br.readLine());
						if (vsId == null)
						{	// ��Ī�� �Ǳ����� ���� �ڷ� ���� Ű�� ���� ���, ������ ����
							interrupt();
							return;
						}
						
						myColor = br.readLine();
						encodedStatus = br.readLine();
				
						userOne.setText((myColor.equals(ServerConstants.MATCH_WHITE) ? myId : vsId));
						userTwo.setText((myColor.equals(ServerConstants.MATCH_BLACK) ? myId : vsId));
						
						bEnd = false;						
						
						// ������ ���� ���� ���� �д�
						if (myColor.equals(ServerConstants.MATCH_BLACK))
							bMyTurn = true;
						else if (myColor.equals(ServerConstants.MATCH_WHITE))
							bMyTurn = false;
						
						{	/* ä�� */
							try
							{
								// ä�� ������ ���� ��û
								chatSock = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.CHAT_SERV_PORT);
								chatBr = new BufferedReader(new InputStreamReader(chatSock.getInputStream(), "UTF8"));
								chatPw = new PrintWriter(new OutputStreamWriter(chatSock.getOutputStream(), "UTF8"));
								
								// ���� ���̵�, ����� ���̵� ����
								chatPw.println(Encryptor.encrypt(myId));
								chatPw.println(Encryptor.encrypt(vsId));
								chatPw.flush();
								
								// ä�� �Է�â Ȱ��ȭ
								txt.setEnabled(true);
								txtArea.append("[����] " + Encryptor.decrypt(chatBr.readLine()) + " ���� �����ϼ̽��ϴ�." + System.lineSeparator());
								txtArea.setCaretPosition(txtArea.getDocument().getLength());
								txtArea.append("[����] " + Encryptor.decrypt(chatBr.readLine()) + " ���� �����ϼ̽��ϴ�." + System.lineSeparator());
								txtArea.setCaretPosition(txtArea.getDocument().getLength());
								
								// �� ������� ������ ���� ������ ������ ä�� ������ �߰����ִ� ������ �Ѵ�.
								(new Thread(){
									@Override
									public void run()
									{
										try {
											while (true) {
												txtArea.append(vsId + " > " + chatBr.readLine() + System.lineSeparator());
												txtArea.setCaretPosition(txtArea.getDocument().getLength());
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										interrupt();
									}
								}).start();
							}	/* ä�� try */ 
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}	/* ä�� */
						
						// �ʱ� ���� ����, ä�� ������ ������ ��ģ�� ȭ���� ����
						updateLabels();
						repaint();

						(new Music("match.mp3", false)).start();
						// ������ ������ ���� ���� ����
						while (!bEnd)
						{
							// ó�� ����� ���� �ٵ����� ����
							String result = br.readLine();
							encodedStatus = br.readLine();
			
							if (result.equals(ServerConstants.MATCH_OK))
							{	// �װ��� ���� �� �� �־� ó���ߴ�
								(new Music("disc.mp3", false)).start();
								bMyTurn = false;	// ���� ���ʰ� �ƴ�
								updateLabels();
								repaint();
							}
							else if (result.equals(ServerConstants.MATCH_YOUR_TURN))
							{	// ���� �����̴�
								bMyTurn = true;	// ���� ����
								updateLabels();
								repaint();
							}
							else if (result.equals(ServerConstants.MATCH_CANT_PUT))
							{	// �װ��� ���� �� �� ����
								(new Music("error.mp3", false)).start();
								updateLabels();
								repaint();
								System.out.println("�װ��� ���� �� ����");
								JOptionPane.showMessageDialog(PlayReversiOnline.this, "Can't put disc to there.");
							}
							else if (result.equals(ServerConstants.MATCH_PASS))
							{	// ���� �Ѱ��� ���� �н��ؾ� �Ѵ�
								(new Music("error.mp3", false)).start();
								updateLabels();
								repaint();
								System.out.println("�ʴ� �н��̴�");
								JOptionPane.showMessageDialog(PlayReversiOnline.this, "You can't put disc anywhere. Pass.");
							}
							else if (result.equals(ServerConstants.MATCH_VS_PASS))
							{	// ������ ���� �� ���� ���� �н��ؾ� �Ѵ�
								(new Music("error.mp3", false)).start();
								updateLabels();
								repaint();
								System.out.println("��밡 �н��̴�");
								JOptionPane.showMessageDialog(PlayReversiOnline.this, "He can't put disc anywhere. Pass.");
							}
							else if (result.equals(ServerConstants.MATCH_WIN) ||
									result.equals(ServerConstants.MATCH_LOSE))
							{	// �̱�ų� �� ���
								updateLabels();
								repaint();
								bEnd = true;	// ������ �����
								handleEnd(result);	// ������ �۾� ó��
							}
							else if (result.equals(ServerConstants.MATCH_ESCAPE))
							{	// ��밡 Ż���� ���
								// ServerConstants Ŭ������ 34~39 ���� ����
								pw.println(ServerConstants.MATCH_ALIVE);
								pw.flush();
								
								updateLabels();
								repaint();
								bEnd = true;	// ������ �����
								handleEnd(result);	// ������ �۾� ó��
							}
						}
					}	// ù��° try
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					finally
					{
						interrupt();
					}
				}	// ù��° run()
			}).start();	// ù��° new Thread()
		}	// if (connectServer())
	}	// start()
	
	// ��Ī ������ ��⿭�� ��� ��û
	private boolean connectServer()
	{
		String result = "";
		
		try
		{
			// ��Ī ������ ��⿭�� ��� ��û
			clnt = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.MATCH_SERV_PORT);
			br = new BufferedReader(new InputStreamReader(clnt.getInputStream(), "UTF8"));
			pw = new PrintWriter(new OutputStreamWriter(clnt.getOutputStream(), "UTF8"));
			
			// ���� ���̵�� �α��ν� ���� ����Ű ����
			pw.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getId()));
			pw.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getKey()));
			pw.flush();

			result = br.readLine();
			if (result.equals(ServerConstants.MATCH_INVALID))	// ������ ��ȿ���� ����
				System.out.println("��ȿ���� ���� �α���");
			else if (result.equals(ServerConstants.MATCH_VALID))	// ������ ��ȿ��
				System.out.println("��ȿ�� �α���");
			
			if (result.equals(ServerConstants.MATCH_INVALID))	// ������ ��ȿ���� ���� ���, ��Ī ������ ���� ����
			{	
				br.close();
				pw.close();
				clnt.close();
				return false;
			}
			
			return true;	// ������ ��ȿ�� ���
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}	// connectServer()

	// �ٵ���, �ٵϾ� �׸���
	public void paintComponent(Graphics page)
	{
		Graphics2D g2d = (Graphics2D) page; // ���� �β��� �����ϱ� ����
		super.paintComponent(page);
		
		page.setColor(new Color(128,64,64));
		g2d.setStroke(new BasicStroke(6f));	// �� �β� ����

		// ��Ƽ �ٸ���� ����
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		// 8*8 �ٵ��� �׸���
		for (int i=1; i<=9; i++)
		{
			page.drawLine(70,70*i,630,70*i);
			page.drawLine(70*i,70,70*i,630);
		}
		
		// ���� ���¸� �������� �ٵϾ� �׸���
		g2d.setStroke(new BasicStroke(4f));	// �ٵϾ� �׵θ��� �β�
		
		int k = 0;
		for (int row=1; row<=Reversi.BOARD_SIZE; row++)
			for (int col=1; col<=Reversi.BOARD_SIZE; col++)
				// �������� ���۹��� �ٵ����� ���¸� �������� �׸�
				drawDisc(page, row, col, encodedStatus.charAt(k++));
		
	} // paintComponent()
	
	// �ٵ����� �ش� (��, ��) �� BLACK/WHITE �ٵϾ��� �׸��� �Լ� 
	public void drawDisc(Graphics page, int row, int col, char status)
	{
		// Status �� EMPTY � �����ϰ� �ֱ� ������ WHITE/BLACK �϶��� �׸�
		if (status==ServerConstants.MATCH_CHAR_WHITE || status==ServerConstants.MATCH_CHAR_BLACK)
		{
			// �ٵϾ� �׸���
			page.setColor((status==ServerConstants.MATCH_CHAR_WHITE ? Color.white : Color.black));
			page.fillOval(70*row+6, 70*col+6, 58, 58);
			// �ٵϾ� �׵θ� �׸���
			page.setColor(Color.black);
			page.drawOval(70*row+6, 70*col+6, 58, 58);
		}
	} // drawDisc()
	
	// ���� �ٵϵ��� ������ �����ϰ�, ���� ������ ������ �󺧿� ������ �����ϴ� �Լ�
	public void updateLabels()
	{
		int wCount = 0, bCount = 0;
		System.out.println(encodedStatus);
		// �������� ���۹��� �ٵ����� ���¸� �������� �� ���� ����
		for (int i=0; i<64; i++)
			if (encodedStatus.charAt(i) == ServerConstants.MATCH_CHAR_BLACK)
				bCount++;
			else if (encodedStatus.charAt(i) == ServerConstants.MATCH_CHAR_WHITE)
				wCount++;
		
		lblWhiteCount.setText(Integer.toString(wCount));
		lblBlackCount.setText(Integer.toString(bCount));
		
		if (myColor.equals(ServerConstants.MATCH_BLACK))
		{
			if (bMyTurn)
			{	// ���� �������̰� �� ���̸�
				lblBlackCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
				lblWhiteCount.setBorder(null);
			}
			else
			{	// ���� �������̰� �� ���� �ƴϸ�
				lblBlackCount.setBorder(null);
				lblWhiteCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
			}
		}
		else if (myColor.equals(ServerConstants.MATCH_WHITE))
		{
			if (bMyTurn)
			{	// ���� �Ͼ���̰� �� ���̸�
				lblBlackCount.setBorder(null);
				lblWhiteCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
			}
			else
			{	// ���� �Ͼ���̰� �� ���� �ƴϸ�
				lblBlackCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
				lblWhiteCount.setBorder(null);
			}
		}
	} // updateLabels()
	
	// ���� ����� ���� ������ �����ϱ� ���� �Լ�
	public void handleEnd(String result)
	{
		try
		{
			// ��Ī �������� ���� ����
			br.close();
			pw.close();
			clnt.close();

			// ä�� �������� ���� ����
			chatPw.close();
			chatBr.close();
			chatSock.close();

			// ä�� �Է� �Ұ�
			txt.setEnabled(false);
			
			// ���ڰ� ���� ���� ������ ���� ������ ��û�ϰ� �ȴ�.
			if (!result.equals(ServerConstants.MATCH_LOSE))
			{
				// ����� ��, ������ ������ ���� ������ ��û�ϰ� �ȴ�.
				if (!result.equals(ServerConstants.MATCH_DRAW) ||
					(result.equals(ServerConstants.MATCH_DRAW) && myColor.equals(ServerConstants.MATCH_BLACK)))
				{
					// ���� ���� ������ ó�� ��û
					clnt = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.UPDATE_SERV_PORT);
					br = new BufferedReader(new InputStreamReader(clnt.getInputStream(), "UTF8"));
					pw = new PrintWriter(new OutputStreamWriter(clnt.getOutputStream(), "UTF8"));		
					
					// �� ���̵�, �� ����Ű, ��� ���̵�, ���� ��� ����
					pw.println(Encryptor.encrypt(myId));
					pw.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getKey()));
					pw.println(Encryptor.encrypt(vsId));
					pw.println(result);
					pw.flush();
					
					// ��� ���
					System.out.println(br.readLine());
	
					// ���� ���� ������ ���� ����
					br.close();
					pw.close();
					clnt.close();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		// ������ ���и� ǥ���ϰ�, �� �÷����� ������ �����ϴ� �޽��� �ڽ��� ���
		String rlt = "";
		
		// ���� �̰�ų� ��밡 Ż���� ���
		if (result.equals(ServerConstants.MATCH_WIN) || result.equals(ServerConstants.MATCH_ESCAPE)) {
			(new Music("select.mp3", false)).start();
			rlt = "You win !!";
		}
		// ���� �� ���
		else if (result.equals(ServerConstants.MATCH_LOSE)) {
			(new Music("lose.mp3", false)).start();
			rlt = "You Lose.";
		}
		// ��� ���
		else if (result.equals(ServerConstants.MATCH_DRAW)) {
			(new Music("select.mp3", false)).start();
			rlt = "Draw !!";	
		}
		
		int select = JOptionPane.showConfirmDialog(this,
								rlt + System.lineSeparator() + "Retry?",
									"Game finished", JOptionPane.YES_NO_OPTION);
	
		if (select == JOptionPane.YES_OPTION)
		{
			// �ʱ�ȭ
			start();
		}
		// �ƴϿ��� �����ų� �޽��� �ڽ��� �׳� ������ �ƹ� �۾��� ���� ����
		
	} // handlEnd()
	
	// �ڷΰ��� ��ư�� ���� ������
	public class BackListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			if (evt.getSource() == btnBack)
			{
				(new Music("back.mp3",false)).start(); // Ŭ�� ���� ����Ʈ
				
				// ������ ���� ����(���� ��Ī�� �ȵǾ��ų� �÷����� ������ ���� ���)���� �ڷΰ��� ��ư�� ���� ���
				if (bEnd)
				{
					// ���� �������� ȭ�� ��ȯ
					AppManager.getInstance().getMainFrame().switchPanel(PlayReversiOnline.this, AppManager.getInstance().getSettingPanel());
				
					// ���� ��Ī�� �Ϸ���� �ʾ� ������ ���� ���¿��� �ڷΰ��⸦ ���� ���
					if (vsId.equals(""))
					{
						try
						{
							// ��Ī ������ ��⿭������ ������ ��û
							Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.POLL_SERV_PORT);
							BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
							PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
							
							// �� ���̵�, ����Ű�� ����
							p.println(Encryptor.encrypt(myId));
							p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getKey()));
							p.flush();
							
							if (b.readLine().equals(ServerConstants.POLL_SUCCESS))	// ��Ī ������ ��⿭���� ���� �Ϸ�
								System.out.println("��⿭���� ���� �Ϸ�");
							else if (b.readLine().equals(ServerConstants.POLL_FAIL))	// ��Ī ������ ��⿭���� ���� ����
								System.out.println("��⿭���� ���� ����");
							
							// Poll Server �� ���� ����
							b.close();
							p.close();
							s.close();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
					
					try
					{
						// ��Ī ������ ���� ����
						pw.close();
						br.close();
						clnt.close();
						
						// ä�� ������ ���� ����
						if (chatSock != null)
						{
							chatPw.close();
							chatBr.close();
							chatSock.close();
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				// ���� ���߿� �ڷΰ��� ��ư�� Ŭ���ϸ�
				else
				{	// ���� �ڷΰ��� �Ұ����� �ѹ� ���
					int select = JOptionPane.showConfirmDialog(PlayReversiOnline.this,
															"Are you sure?",
															"Back to my page", JOptionPane.YES_NO_OPTION);
					
					if (select == JOptionPane.YES_OPTION)
					{
						// ���� �������� ȭ�� ��ȯ
						AppManager.getInstance().getMainFrame().switchPanel(PlayReversiOnline.this, AppManager.getInstance().getSettingPanel());
						
						try
						{
							// ��Ī ������ ���� ����
							pw.close();
							br.close();
							clnt.close();
							
							// ä�� ������ ���� ����
							chatPw.close();
							chatBr.close();
							chatSock.close();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			}
		} // actionPerformed()
	} // BackListener Inner Class
	
	// �ٵ��ǿ��� �̺�Ʈ�� �߻��ϸ� ��ǥ�� �̿��� (��, ��) �� �Ǻ��ϰ� ó���� �����ϱ� ���� �̳� Ŭ����
	public class BoardListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent evt) {}
		@Override
		public void mousePressed(MouseEvent evt) {}
		@Override
		public void mouseEntered(MouseEvent evt) {}
		@Override
		public void mouseExited(MouseEvent evt) {}
		
		@Override
		public void mouseReleased(MouseEvent evt)
		{
			Point pt = evt.getPoint();
			// �̺�Ʈ�� �߻��� ���� ��ǥ�� ���� (��, ��) ������ ����
			int row = pt.x / 70;
			int col = pt.y / 70;
			
			// ��ǥ�� �ٵ����� ����ų� ������ ���� ����, �� ���� �ƴ϶�� �ƹ��͵� ����
			if ( (row<1 || 8<row) || (col<1 || 8<col) || bEnd || !bMyTurn)
				return;

			// ��Ī ������ (��, ��) ���� ����
			pw.println(Integer.toString(row*10 + col));
			pw.flush();
		} // mouseReleased()
	} // BoardListener Inner Class
	
	public class ChatListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == txt)
			{
				// ���� �Է��� ������ ���� ä�� ������ ������
				txtArea.append(myId + " > " + txt.getText() + System.lineSeparator());
				txtArea.setCaretPosition(txtArea.getDocument().getLength());
				// ä�� ������ ���� �Է��� ���� ����
				chatPw.println(txt.getText());
				chatPw.flush();
				txt.setText("");
			}
		} // actionPerformed(ActionEvent e)
	} // ChatListener Inner Class
} // PlayReversiOnline Class