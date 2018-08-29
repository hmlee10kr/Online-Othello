import java.io.*;
import java.net.*;

/*
	�ʱ⿡ ��� ��ȣ ���� ������ ����� �ǵ���
	����Ű�� Ȯ������ �ʾ�����,
	
	���� �����߿� �ٸ� ����� ȸ�� Ż�� �ع�����,
	���� ������ �� �� ���� ���� ������ �ֱ� ������,
	���� �α����� ������ ȸ�� Ż�� �����ϵ��� �����ߴ�.
*/

// ȸ�� Ż�� ����
// ���̵�, ��� ��ȣ, ����Ű�� �޾� �۾��� ó���ϰ� ����� �����Ѵ�.
// (����Ű�� Ȯ���Ѵ�.)
public class WithdrawalServer extends ServerSocket implements Runnable
{
	private Thread thread;

	public WithdrawalServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setWithdrawalServer(this);
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
				// ���̵�, ����Ű, ��� ��ȣ
				String id = Encryptor.decrypt(br.readLine());
				String key = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());

				if (ServerManager.getInstance().getLoginServer().isValid(id, key) && ServerManager.getInstance().getMemberDAO().drop(id, password))
					// ���� �̿����� ��ȿ�� �����̰�, ���̵�� ��� ��ȣ�� ��Ȯ�ϸ� ����
					pw.println(ServerConstants.WITHDRAWAL_SUCCESS);
				else
					// ȸ�� Ż�� ����
					pw.println(ServerConstants.WITHDRAWAL_FAIL);

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
}	// WithdrawalServer Class