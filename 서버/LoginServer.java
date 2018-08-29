import java.io.*;
import java.net.*;
import java.util.*;

/*
	���� ������� ���� ������ inUseVector �� �����ϸ�,
	�ٸ� �������� ������ ��ȿ���� �˻��� ��, �� ������
	isExist, isValid ���� �Լ��� ����ϰ� �ȴ�.
*/

// �α��� ����
// ���̵�, �н����带 �޾�
// �ش� ������ ���� ������ �ִ���, ���� ��������� Ȯ���� ��
// �۾��� ó���Ͽ� ����� �����Ѵ�.
// (�α��� ������ 100,000 ������ ���� ���� ���ڿ��� �Բ� �����Ѵ�.) 
public class LoginServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	// ���� ������� ���� �������� ���Ϳ� �����Ͽ� �����Ѵ�.
	private Vector<InUseInfo> inUseVector;
	
	public LoginServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		inUseVector = new Vector<InUseInfo>();

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setLoginServer(this);
	}

	// �α��� ������ ���� �̿����� ���� ���͸� ��ȯ
	public Vector<InUseInfo> getInUseVector()	{ return inUseVector; }
	
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
	
	// ���� ������ �̿������� Ȯ���ϴ� �޼���
	public synchronized boolean isExist(String id)
	{
		int i;
		int size;

		size = inUseVector.size();
		
		for (i=0; i<size; i++)
			if (id.equals(inUseVector.get(i).getId()))
				return true;
		
		return false;	
	}
	
	// ���۵� ���̵�� Ű�� ��ȿ���� Ȯ���ϴ� �޼���
	public synchronized boolean isValid(String id, String key)
	{
		int i;
		int size;

		size = inUseVector.size();
		
		for (i=0; i<size; i++)
			if (id.equals(inUseVector.get(i).getId()) && key.equals(inUseVector.get(i).getKey()))
				return true;
		
		return false;	
	}
	
	// �̿����� ���� ���Ϳ��� �ش� ������ ���� ��ü�� �ε����� �˻��ϴ� �޼���
	public synchronized int find(String id)
	{
		int size = inUseVector.size();

		for (int i=0; i<size; i++)
			if (id.equals(inUseVector.get(i).getId()))
				return i;
		
		return -1;			
	}
	public synchronized int find(String id, String key)
	{
		int size = inUseVector.size();

		for (int i=0; i<size; i++)
			if (id.equals(inUseVector.get(i).getId()) && key.equals(inUseVector.get(i).getKey()))
				return i;
		
		return -1;			
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
				// ���̵�, �н�����
				String id = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());
				
				// MemberDAO �� login �� DB �� �ش� ���̵�� �н����带 ���� ������ �ִ��� �˻��ϴ� �޼����̴�.
				if (ServerManager.getInstance().getMemberDAO().login(id, password))
				{	// �ش� ������ ���� ������ �����Ѵ�.
					
					if (isExist(id))
					{	// �� ������ �̹� ������̴�.
						pw.println(ServerConstants.LOGIN_INUSE);
						
						// ���� ����
						pw.flush();
						pw.close();	
						br.close();	
						clnt.close();		
					}
					else
					{	// �α��� ����
						
						// 100,000 ������ ���� ���� ���ڿ� ����
						String key = Integer.toString((int)(Math.random()*100000 + 1));
						
						// ���̵�� Ű�� �����ϸ� ������, ���� �����ϴ���
						// �� �ٸ� ������ Ŭ������ ������ �ʾҽ��ϴ�.
						inUseVector.add(new InUseInfo(id, key, null, null, null));
						
						// �α��� ���� ����
						pw.println(ServerConstants.LOGIN_SUCCESS);
						// ����Ű�� �Բ� ����
						pw.println(Encryptor.encrypt(key));
						pw.flush();
	
						// ������� �α� �ƿ��� Ȯ���ϴ� ������
						(new Thread() {
							@Override
							public void run()
							{
								try
								{
									// �� �Է� ��� ���´� Ư���� �����͸� ���� �ʰ�,
									// Ŭ���̾�Ʈ�� ������ ������������ ���� ó����
									// �̿��� �α� �ƿ��� �Ǻ��Ѵ�.
									String msg = br.readLine();
									if (msg == null) throw new Exception("Connection reset");
								}	// ������� �α� �ƿ��� Ȯ���ϴ� �������� try
								catch (Exception ex)
								{
									ex.printStackTrace();
									
									// �ش� ���̵�� Ű�� ���� ������ ���Ϳ��� ����
									int idx = find(id, key);
									inUseVector.remove(idx);

									// ��Ī�� �Ǳ��� �ڷ� ���� Ű�� ���� ���,
									// Ŭ���̾�Ʈ�� Poll Server(��Ī ������ ��⿭���� �����ϴ� ����) ��
									// ó���� ��û������,
									// ���α׷��� ������� ���� ���, ó������ �ʰ� ���� ������,
									// �� ���, �α� �ƿ��� �Ǻ��� �Բ� �� ������ ���� Poll Server �� ��û�ϰ� �ȴ�.
									try
									{
										Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.POLL_SERV_PORT);
										BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
										PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
	
										p.println(Encryptor.encrypt(id));
										p.println(Encryptor.encrypt(key));
										p.flush();
										
										// ��Ī ������ ��⿭�� �����ϸ� �����ϰ�,
										// �������� ������ �������� �ʴ´�.
										System.out.println(b.readLine());
	
										b.close();
										p.close();
										s.close();
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}	// ������� �α� �ƿ��� Ȯ���ϴ� �������� catch
								finally
								{
									try
									{
										// ���� ����
										br.close();
										pw.close();
										clnt.close();
									}
									catch (Exception excp)
									{
										excp.printStackTrace();
									}
									
									interrupt();	// ������� �α� �ƿ��� Ȯ���ϴ� ������ ����
								}	// ������� �α� �ƿ��� Ȯ���ϴ� �������� finally
							}	// ������� �α� �ƿ��� Ȯ���ϴ� �������� run()
						}).start();	// ������� �α� �ƿ��� Ȯ���ϴ� ������
					}	// if (isExist(id)) �� else
				}	// if (ServerManager.getInstance().getMemberDAO().login(id, password))
				else
				{	// �ش� ������ ���� ������ ����.
					pw.println(ServerConstants.LOGIN_FAIL);
					
					// ���� ����
					pw.flush();
					pw.close();	
					br.close();	
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
}	// LoginServer Class