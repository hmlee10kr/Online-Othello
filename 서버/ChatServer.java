import java.io.*;
import java.net.*;
import java.util.*;

/*
	ä�� ������ ������,
	Ŭ���̾�Ʈ�� ������ ���� ���̵�, ����� ���̵� �� �����ϰ�
	ä�� ������ ���۵Ǹ� ���Ϳ��� ����� ���̵� �˻��Ͽ�
	�ش� ����� �����ִ� ������� �����Ѵ�.
*/

// ä�� ����
// (����Ű�� Ȯ������ �ʴ´�.)
public class ChatServer extends ServerSocket implements Runnable
{
	private Thread thread;
	private Vector<ChatUser> chatUsers;	// ���� ������ ��� ä�� �������� ���ͷ� �����Ѵ�.
	
	public ChatServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);
		
		chatUsers = new Vector<ChatUser>();

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setChatServer(this);
	}

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

	// ���Ϳ��� �ش� ���̵� ���� ���̵��� ��ü�� ��ȯ�Ѵ�.
	private synchronized ChatUser find(String id)
	{
		for (ChatUser info : chatUsers)
			if (info.getMyId().equals(id))
				return info;
		return null;
	}
	
	private class ChildThread extends Thread
	{
		private String myId;	// ���� ���̵�
		private String vsId;	// ����� ���̵�
		
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
				// ���� ���̵�, ����� ���̵�
				myId = Encryptor.decrypt(br.readLine());
				vsId = Encryptor.decrypt(br.readLine());
				
				// ���������� ���۵Ǿ��ٴ� ���� Ȯ���� �ٽ� �����ش�.
				pw.println(Encryptor.encrypt(myId));
				pw.println(Encryptor.encrypt(vsId));
				pw.flush();
				
				// ���Ϳ� ���� ���̵�, ����� ���̵�, ���� ������ �߰��Ѵ�.
				chatUsers.add(new ChatUser(myId, vsId, clnt, br, pw));
				
				// ����, ä�� �Է��� ������,
				// ���Ϳ��� ����� ���̵� �˻��Ͽ�
				// �ش� ����� �����ش�.
				while (true)
				{
					String msg = br.readLine();
					
					if (msg == null) {
						// Ŭ���̾�Ʈ�� ������ ������ ���
						throw new Exception("Connection reset");
					}
					else {
						// �������� ä�� �Է��� ���
						send(msg);	// ��뿡�� �޼��� ����
						// DB �� ä�� ���� �α�
						ServerManager.getInstance().getMemberDAO().chatLogging(myId, vsId, msg);
					}
				}
			}	// ChildThread �� run() �� ù��° try
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				// ���Ϳ��� �� ���� ���� �� ���� ����
				chatUsers.remove(find(myId));
				br.close();
				pw.close();
				clnt.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			interrupt();	// ������ ����
		}	// ChildThread �� run()
		
		// ��뿡�� �޼����� �����Ѵ�.
		private synchronized void send(String msg)
		{
			for (ChatUser info : chatUsers)
			{	// ���� ���̵� �� �ʿ��� ����� ���̵��� ��ü�� �˻��ϰ�
				// �� ��ü�� ����� �޼����� �����Ѵ�.
				if (info.getMyId().equals(vsId))
				{
					info.getPw().println(msg);
					info.getPw().flush();
					break;
				}
			}	// for (ChatUser info : chatUsers)
		}	// send(String msg)
	}	// ChildThread Inner Class
}	// ChatServer Class