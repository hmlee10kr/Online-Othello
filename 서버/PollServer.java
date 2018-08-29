import java.io.*;
import java.net.*;

/*
	Ŭ���̾�Ʈ�� ��Ī�� ��û�ϰ�
	��Ī�� �Ϸ�Ǳ� �� �ڷ� ���� Ű�� ���� ���,
	��Ī ������ ��⿭���� ������ �ʰ� ���� �ȴ�.
	
	�̸� �ذ��ϱ� ����, ��Ī�� �Ϸ�Ǳ� ��
	�ڷ� ���� Ű�� ������, �� ������ �۾��� ��û�ϰ� �ȴ�.
	
	���α׷��� ���� ���� �� ������ ��û�� ������ �ʱ� ������,
	�α��� ������ �α� �ƿ��� �Ǻ��� �Բ� ���� �� ������ ��û�ϰ� �ȴ�.
*/

// ��Ī ������ ��⿭���� �����ϴ� ����
// ���̵�, ����Ű�� �޾� �۾��� ó���ϰ� ����� �����Ѵ�.
// (����Ű�� Ȯ���Ѵ�.)
public class PollServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	public PollServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setPollServer(this);
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
				
				// �ش� ���̵�, ����Ű�� ���� ��ü�� ��⿭���� �˻��Ͽ� ����
				MatchServer matchServer = ServerManager.getInstance().getMatchServer();
				InUseInfo info = matchServer.findQueue(id, key);
				
				if (info != null)
				{
					info.getBr().close();
					info.getPw().close();
					info.getSocket().close();
					matchServer.getQueue().remove(info);
					
					// ��Ī ������ ��⿭���� ���� ����
					pw.println(ServerConstants.POLL_SUCCESS);
				}
				else
				{
					// �׷��� ������ ���� ��ü�� ��⿭�� �������� ����
					pw.println(ServerConstants.POLL_FAIL);
				}

				pw.flush();
				pw.close();	
				br.close();	
				clnt.close();
				
				interrupt();	// ������ ����
			}	// ChildThread �� run() �� try
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}	// ChildThread �� run()
	}	// ChildThread Inner Class
}	// PollServer Class