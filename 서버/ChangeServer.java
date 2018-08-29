import java.io.*;
import java.net.*;

/*
	���� �̿��߿� �ٸ� ����� ��� ��ȣ�� �����ϴ���,
	�������� (���̵�, ����Ű) �� �α����� ������ �����ϱ� ������
	���� �÷���, ���� ���� ���� ���� ���� �̿� �����ϴ�.
*/

// ��� ��ȣ ���� ����
// ���̵�, ���� ��� ��ȣ, ������ ��� ��ȣ�� �޾� �۾��� ó���ϰ� ����� �����Ѵ�.
// (����Ű�� Ȯ������ �ʴ´�.)
public class ChangeServer extends ServerSocket implements Runnable
{
	private Thread thread;

	public ChangeServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);
		
		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setChangeServer(this);
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
				// ���̵�, ���� ��� ��ȣ, ������ ��� ��ȣ
				String id = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());
				String change = Encryptor.decrypt(br.readLine());
				
				// MemberDAO �� login �� DB �� �ش� ���̵�� �н����带 ���� ������ �ִ��� �˻��ϴ� �޼����̴�.
				if (ServerManager.getInstance().getMemberDAO().login(id, password))
				{	// �ش� ������ ���� ������ �����Ѵ�.
					
					// ��ü�� ���� �������� ������ ��, ��� ��ȣ�� ����
					MemberDTO memberDTO = ServerManager.getInstance().getMemberDAO().info(id);
					memberDTO.setPassword(change);
					
					// �ش� ��ü�� ���ڷ� �����Ͽ�, ���� ���� ����
					ServerManager.getInstance().getMemberDAO().update(memberDTO);
					
					// ��� ��ȣ ���濡 �����ߴ�.
					pw.println(ServerConstants.CHANGE_SUCCESS);
				}
				else
				{	// �ش� ������ ���� ������ �������� �ʴ´�.
					pw.println(ServerConstants.CHANGE_FAIL);
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
}	// ChangeServer Class