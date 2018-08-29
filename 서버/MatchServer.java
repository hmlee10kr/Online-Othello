import java.io.*;
import java.net.*;
import java.util.concurrent.*;

// ��Ī ����
// ���̵�, ����Ű�� �޾� ��ȿ�� �������� Ȯ���� ��⿭�� �����ϰ� ����� �����Ѵ�.
// ��⿭�� ũ�Ⱑ 2 �̻��̸� 2 ���� ��ü�� ������ ��Ī��Ű��
// �����带 �����Ͽ� ������ �����Ѵ�.
// (����Ű�� Ȯ���Ѵ�.)
public class MatchServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	// ��Ī�� ������� �������� �����ϴ� ��⿭
	private LinkedBlockingQueue<InUseInfo> queue;
	
	public MatchServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);
		
		queue = new LinkedBlockingQueue<InUseInfo>();

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setMatchServer(this);
	}

	// ��Ī ������ ��⿭�� ��ȯ
	public LinkedBlockingQueue<InUseInfo> getQueue()	{ return queue; }

	public void start()
	{
		thread.start();
	}

	@Override
	public void run()
	{
		while (true)
			try { (new ChildThread(accept())).start(); }
			catch (Exception ex) { ex.printStackTrace(); }
	}
	
	// ��⿭���� �ش� ������ ���� ��ü�� ��ȯ�ϴ� �޼���
	// Poll Server ���� ���ȴ�.
	public synchronized InUseInfo findQueue(String id, String key)
	{
		for (InUseInfo info : queue)
			if (info.getId().equals(id) && info.getKey().equals(key))
				return info;
		return null;
	}
	
	private class ChildThread extends Thread
	{
		// accept �Լ����� ��ȯ�� ������ �̿��� ���� ����
		private Socket clnt;
		private BufferedReader br;
		private PrintWriter pw;

		public ChildThread(Socket clnt)
		{
			this.clnt = clnt;
			
			try
			{
				br = new BufferedReader(new InputStreamReader(clnt.getInputStream(), "UTF8"));
				pw = new PrintWriter(new OutputStreamWriter(clnt.getOutputStream(), "UTF8"));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}	// ChildThread Inner Class constructor
		
		@Override
		public void run()
		{
			try
			{
				// ���̵�, ����Ű
				String id = Encryptor.decrypt(br.readLine());
				String key = Encryptor.decrypt(br.readLine());
				String result;
				
				System.out.println(id);
				System.out.println(key);
				
				if (ServerManager.getInstance().getLoginServer().isValid(id, key))
					// ��ȿ�� ����
					result = ServerConstants.MATCH_VALID;
				else
					// ��ȿ���� ���� ����
					result = ServerConstants.MATCH_INVALID;

				System.out.println(result);
				pw.println(result);
				pw.flush();
				

				if (result.equals(ServerConstants.MATCH_VALID))
				{	// ��ȿ�� ������ ���
					
					// ��⿭�� ��� ����� ���� ������ �Բ� �߰�
					queue.offer(new InUseInfo(id, key, clnt, br, pw));

					// ��⿭�� ũ�Ⱑ 2 �̻��̸� 2 ���� ��ü�� ������ ��Ī��Ű��
					// �����带 �����Ͽ� ���� ����
					if (2 <= queue.size())
						(new IOControlThread(queue.poll(), queue.poll())).start();
				}
				else
				{	// ��ȿ���� ���� ������ ��
					br.close();
					pw.close();
					clnt.close();
				}

				interrupt();	// ������ ����
			}	// ChildThread �� run() �� try
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}	// ChildThread �� run()
	}	// ChildThread Inner Class
	
	// ��Ī�� �� ������ ������ �����ϴ� Ŭ����
	private class IOControlThread extends Thread
	{
		// �����θ� �����ϴ� �ھ�
		private Reversi reversi;
		
		// �浹�� �鵹�� ���̵�
		private String idBlack, idWhite;
		// ����Ű�� ������ �ʴ´�.

		// �浹�� �鵹�� ���� ����
		private Socket sockBlack, sockWhite;
		private BufferedReader blackRd, whiteRd;
		private PrintWriter blackPw, whitePw;
		
		public IOControlThread(InUseInfo infoBlack, InUseInfo infoWhite)
		{
			reversi = new Reversi();
			
			idBlack = infoBlack.getId();
			idWhite = infoWhite.getId();
			
			sockBlack = infoBlack.getSocket();
			sockWhite = infoWhite.getSocket();
			blackRd = infoBlack.getBr();
			whiteRd = infoWhite.getBr();
			blackPw = infoBlack.getPw();
			whitePw = infoWhite.getPw();
		}
		
		@Override
		public void run()
		{
			// ���ܰ� �߻��� �������� ��� �� �������Լ� �߻��� ������ �����ϱ� ���� ����
			Status escape = Status.BLACK;
		
			// ������ ������ ����� ���̵� ����
			blackPw.println(Encryptor.encrypt(idWhite));
			blackPw.println(ServerConstants.MATCH_BLACK);
			whitePw.println(Encryptor.encrypt(idBlack));
			whitePw.println(ServerConstants.MATCH_WHITE);
			
			// �ʱ� �ٵ����� ���� ����
			String encodedStatus = toEncodedStatus();
			blackPw.println(encodedStatus);
			whitePw.println(encodedStatus);			
			
			blackPw.flush();
			whitePw.flush();
			
			try
			{
				while (true)
				{	
					// ���� �������� ���ܰ� �߻��� ��� �������Լ� �߻��� ������ ����
					escape = (reversi.getTurn() == Status.BLACK ? Status.BLACK : Status.WHITE);
					
					// ���� ������ �������Լ� ��ġ�� ���� �޴´�.
					String str = (reversi.getTurn() == Status.BLACK ? blackRd : whiteRd).readLine();
					if (str == null) throw new Exception("Connection reset");
					Position pos = strToPosition(str);
					
					// �Է� ���� ��ġ�� �̿��� �ھ ó���Ѵ�.
					if (reversi.handleInput(pos.getRow(), pos.getCol()))
					{
						// ������ ����Ǿ��� ���
						if (reversi.isEnd())
						{
							// ������ ��� ���� (�¸�, �й�, ���)
							(reversi.getBlackCount() > reversi.getWhiteCount() ? blackPw : whitePw)
									.println((reversi.getBlackCount() == reversi.getWhiteCount() ? ServerConstants.MATCH_DRAW : ServerConstants.MATCH_WIN));
							(reversi.getBlackCount() > reversi.getWhiteCount() ? whitePw : blackPw)
									.println((reversi.getBlackCount() == reversi.getWhiteCount() ? ServerConstants.MATCH_DRAW : ServerConstants.MATCH_LOSE));
							// ���� �ٵ����� ���� ����
							blackPw.println(toEncodedStatus());		
							whitePw.println(toEncodedStatus());
							blackPw.flush();		
							whitePw.flush();				
							
							break;
						}
						// ���� ������ ������ �н��� ���
						else if (reversi.isPass())
						{
							// �������� ����� �н��̰�, ��밡 �н���� ��� ����
							(reversi.getTurn() == Status.BLACK ? blackPw : whitePw).println(ServerConstants.MATCH_PASS);
							(reversi.getTurn() == Status.BLACK ? whitePw : blackPw).println(ServerConstants.MATCH_VS_PASS);
							// ���� �ٵ����� ���� ����
							blackPw.println(toEncodedStatus());		
							whitePw.println(toEncodedStatus());
							blackPw.flush();		
							whitePw.flush();
							
							// ���� ����
							reversi.toggleTurn();
						}
						// �Ϲ������� ���� ����
						else
						{
							// �������� ����� �����̰�, �������� �Է��� ��ġ�� ���������� ó���Ǿ��ٰ� ���� 
							(reversi.getTurn() == Status.BLACK ? blackPw : whitePw).println(ServerConstants.MATCH_YOUR_TURN);
							(reversi.getTurn() == Status.BLACK ? whitePw : blackPw).println(ServerConstants.MATCH_OK);
							// ���� �ٵ����� ���� ����
							blackPw.println(toEncodedStatus());		
							whitePw.println(toEncodedStatus());
							blackPw.flush();		
							whitePw.flush();
						}
					}	// if (reversi.handleInput(pos.getRow(), pos.getCol()))
					else
					{
						// �ش� ��ġ�� ���� �� �� ���� ���,
						// ��� ���� �� �������Ը� ���� �� �� ������ �˸�
						(reversi.getTurn() == Status.BLACK ? blackPw : whitePw).println(ServerConstants.MATCH_CANT_PUT);
						// ��� ���� �� �������Ը� ���� �ٵ����� ���� ����
						(reversi.getTurn() == Status.BLACK ? blackPw : whitePw).println(toEncodedStatus());
						(reversi.getTurn() == Status.BLACK ? blackPw : whitePw).flush();
					}
				}
			}	// IOControlThread �� run() �� ù��° try
			catch (Exception ex)
			{
				ex.printStackTrace();
				
				if (escape == Status.BLACK)
				{	// �浹���� �д� ���� ���ܰ� �߻��� ���
					
					// �鵹���� �浹�� Ż�� ����� �˸�
					whitePw.println(ServerConstants.MATCH_ESCAPE);
					// ���� �ٵ����� ���� ����
					whitePw.println(toEncodedStatus());
					whitePw.flush();
					
					try
					{
						/*
							�����尡 �Է��� ���ʾ� �޾� ó���ϱ� ������,
							�� �Ͽ� ��밡 ������, ���� ���� �ΰ� ���ʰ� �ٲ��� ����� Ż�� ����� ���� �޴´�.
							
							���� ���Ͽ� ��밡 ������ ���ʰ� �ٲ��� ����ä�� ���� ������ ���, ������ �� ���ῡ�� ���ܰ� �߻��Ѵ�.
							�� ��, ��밡 ���� Ż���ߴ��� �Ǻ��ϱ� ���� ó���� �ʿ��ϴ�.
						*/
						
						// �鵹�� Ż�� ����� ���� �ް� MATCH_ALIVE �� ������ ���,
						// �鵹�� ���� ���� ������ ó���� ��û�ϰ� �ȴ�.
						
						// �鵹���Լ� ������ ���� ���,
						// �鵹�� �浹���� ���� Ż�������� �Ǻ��� �� �ִ�.
						if (whiteRd.readLine() == null)
							throw new Exception("Connection reset");
					}	// �鵹�� ���� Ż���ߴ��� Ȯ���ϱ� ���� try
					catch (Exception e)
					{	// �� �Ͽ� ��밡 ������, ���ʰ� �ٲ��� ����ä�� ���� ���� ���
						// �� ������ ���� ���� ���� ������ ���� ������ ��û�Ѵ�.
						try
						{
							Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.UPDATE_SERV_PORT);
							BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
							PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));

							// ����, ����Ű�� ��Ī ������ ��û ����, ����, ������ ���
							p.println(Encryptor.encrypt(idBlack));
							p.println(Encryptor.encrypt(ServerConstants.UPDATE_MATCH_SERVER_REQUEST));
							p.println(Encryptor.encrypt(idWhite));
							p.println(ServerConstants.MATCH_ESCAPE);
							p.flush();
							
							System.out.println(b.readLine());

							b.close();
							p.close();
							s.close();
						}
						catch (Exception excp)
						{
							excp.printStackTrace();
						}
					}	// �鵹�� ���� Ż���ߴ��� Ȯ���ϱ� ���� catch
				}	// if (escape == Status.BLACK)
				else if (escape == Status.WHITE)
				{	// �鵹���� �д� ���� ���ܰ� �߻��� ���
					
					// �浹���� �鵹�� Ż�� ����� �˸�
					blackPw.println(ServerConstants.MATCH_ESCAPE);
					// ���� �ٵ����� ���� ����
					blackPw.println(toEncodedStatus());
					blackPw.flush();
					
					try
					{
						/*
							�����尡 �Է��� ���ʾ� �޾� ó���ϱ� ������,
							�� �Ͽ� ��밡 ������, ���� ���� �ΰ� ���ʰ� �ٲ��� ����� Ż�� ����� ���� �޴´�.
							
							���� ���Ͽ� ��밡 ������ ���ʰ� �ٲ��� ����ä�� ���� ������ ���, ������ �� ���ῡ�� ���ܰ� �߻��Ѵ�.
							�� ��, ��밡 ���� Ż���ߴ��� �Ǻ��ϱ� ���� ó���� �ʿ��ϴ�.
						*/
						
						// �浹�� Ż�� ����� ���� �ް� MATCH_ALIVE �� ������ ���,
						// �浹�� ���� ���� ������ ó���� ��û�ϰ� �ȴ�.
						
						// �浹���Լ� ������ ���� ���,
						// �浹�� �鵹���� ���� Ż�������� �Ǻ��� �� �ִ�.
						if (blackRd.readLine() == null)
							throw new Exception("Connection reset");
					}	// �浹�� ���� Ż���ߴ��� Ȯ���ϱ� ���� try
					catch (Exception e)
					{	// �� �Ͽ� ��밡 ������, ���ʰ� �ٲ��� ����ä�� ���� ���� ���
						// �� ������ ���� ���� ���� ������ ���� ������ ��û�Ѵ�.
						try
						{
							Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.UPDATE_SERV_PORT);
							BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
							PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));

							// ����, ����Ű�� ��Ī ������ ��û ����, ����, ������ ���
							p.println(Encryptor.encrypt(idWhite));
							p.println(Encryptor.encrypt(ServerConstants.UPDATE_MATCH_SERVER_REQUEST));
							p.println(Encryptor.encrypt(idBlack));
							p.println(ServerConstants.MATCH_ESCAPE);
							p.flush();
							
							System.out.println(b.readLine());

							b.close();
							p.close();
							s.close();
						}
						catch (Exception excp)
						{
							excp.printStackTrace();
						}
					}	// �浹�� ���� Ż���ߴ��� Ȯ���ϱ� ���� catch					
				}	// else if (escape == Status.WHITE)
			}	// IOControlThread �� run() �� ù��° catch
			
			try
			{
				blackRd.close();
				blackPw.close();
				sockBlack.close();
				whiteRd.close();
				whitePw.close();
				sockWhite.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			interrupt();	// ���� ���� ������ ����
		}	// IOControlThread �� run()
		
		// �ٵ����� ���¸� 8x8 �� 64 ������ ���ڿ��� ��ȯ�ϴ� �޼��� (EEEBBWBWWEEEE...)
		private synchronized String toEncodedStatus()
		{
			String encodedStatus = "";
			
			for (int i=1; i<=Reversi.BOARD_SIZE; i++)
				for (int j=1; j<=Reversi.BOARD_SIZE; j++)
					if (reversi.statusAt(i, j) == Status.BLACK)
						encodedStatus += Character.toString(ServerConstants.MATCH_CHAR_BLACK);
					else if (reversi.statusAt(i, j) == Status.WHITE)
						encodedStatus += Character.toString(ServerConstants.MATCH_CHAR_WHITE);
					else if (reversi.statusAt(i, j) == Status.EMPTY)
						encodedStatus += Character.toString(ServerConstants.MATCH_CHAR_EMPTY);
			
			return encodedStatus;
		}
		
		// ���� "34" �� �� ���ڿ��� ��ġ (3, 4) �� ��ȯ�Ͽ� ��ȯ�ϴ� �޼���
		private synchronized Position strToPosition(String str)
		{
			return new Position((int)(str.charAt(0) - '0'), (int)(str.charAt(1) - '0'));
		}
	}	// IOControlThread Inner Class
}	// MatchServer Class