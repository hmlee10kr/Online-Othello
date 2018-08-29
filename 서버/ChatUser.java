import java.io.*;
import java.net.*;

// �� ������ ä�� �̿��� ���� �������� ���� Ŭ����
public class ChatUser
{
	private String myId;	// ���� ���̵�
	private String vsId;	// ����� ���̵�
	// ���� ����
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	
	public ChatUser()
	{
		this("", "", null, null , null);
	}
	
	public ChatUser(String myId, String vsId, Socket socket, BufferedReader br, PrintWriter pw)
	{
		setMyId(myId);
		setVsId(vsId);
		setBr(br);
		setPw(pw);
		setSocket(socket);
	}

	// getter/setter
	public String getMyId()			{ return myId; }
	public String getVsId()			{ return vsId; }
	public Socket getSocket()		{ return socket; }
	public BufferedReader getBr()	{ return br; }
	public PrintWriter getPw()		{ return pw; }
	public void setMyId(String myId)			{ this.myId = myId; }
	public void setVsId(String vsId)			{ this.vsId = vsId; }
	public void setSocket(Socket socket)	{ this.socket = socket; }
	public void setBr(BufferedReader br)	{ this.br = br; }
	public void setPw(PrintWriter pw)		{ this.pw = pw; }
}	// ChatUser Class