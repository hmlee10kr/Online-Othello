import java.io.*;
import java.net.*;

// �α��ν��� ���̵�, ����Ű, ���� ������ �����ϴ� Ŭ����
public class InUseInfo
{
	// ���̵�, ����Ű
	private String id;
	private String key;
	
	// �α��ν� ����� ���� ����
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	
	public InUseInfo()
	{
		this("", "", null, null, null);
	}
	
	public InUseInfo(String id, String key, Socket socket, BufferedReader br, PrintWriter pw)
	{
		setId(id);
		setKey(key);
		setSocket(socket);
		setBr(br);
		setPw(pw);
	}

	// getter/setter
	public String getId()	{ return id; }
	public String getKey()	{ return key; }
	public BufferedReader getBr()	{ return br; }
	public PrintWriter getPw()		{ return pw; }
	public Socket getSocket()		{ return socket; }
	public void setId(String id)	{ this.id = id; }
	public void setKey(String key)	{ this.key = key; }
	public void setSocket(Socket socket)	{ this.socket = socket; }
	public void setBr(BufferedReader br)	{ this.br = br; }
	public void setPw(PrintWriter pw)		{ this.pw = pw; }
}