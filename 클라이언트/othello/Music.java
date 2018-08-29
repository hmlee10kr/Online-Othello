package othello;

import java.io.*;

import javazoom.jl.player.Player;

// BGM, ���� ����Ʈ ����� ���� Ŭ����
public class Music extends Thread {

	private Player player; // �����
	private boolean isLoop; // �ݺ� ���θ� ���� ����
	private File file; // ���� ������ �ޱ� ���� ����
	private FileInputStream fis; // ������ �����ϱ� ���� ����
	private BufferedInputStream bis; // �÷��̾� ��� �� ������ ���� ����

	// �� Ŭ������ javazoom�� ����Ͽ� ����� ����.

	public Music(String name, boolean isLoop) {
		try {
			file = new File(Main.class.getResource("../music/" + name).toURI());
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
			this.isLoop = isLoop;
			// ������ toURI�� �޾ƿͼ� file������ �ְ�
			// fis�ȿ� ���� inputstream���� ������ �������ش�.
			// bis�� fis�� ���� ��ǲ��Ʈ������ �����ϰ�
			// �÷��̾ bis�� ����Ͽ� �����Ѵ�.
			// �ݺ� ���θ� �޴� isLoop boolean������ ���� �ִ´�
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}// constructor
		// ���� ���丮�� �ִ� ���� ���Ͽ� �����Ѵ�

	// 3.other functions ����
	// �ð�
	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}
	// �÷��̾��� �ð��� ��Ʈ�� �������� �ð��� ���Ͽ� ��ũ�� ���߾�� �ϹǷ�
	// �ð��� �޾ƿ��� gettime�� �����Ѵ�.

	// �� ����
	public void close() {
		isLoop = false;
		player.close();
		this.interrupt();// �� ����
	}
	// ���� �������� �ʱ�ȭ
	// �÷��̾� �ݰ�
	// MUSIC�� interrupt��Ű�鼭 ������.

	// start �κ�
	@Override
	public void run() {
		// thread ��� ������ ������ �ؾ���
		try {
			do {
				player.play();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
			} while (isLoop);// isLoop�� ���ε��� ��� �÷��̾ �����Ű�� �Ȱ��� ������ �޾ƿ´�

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	// http://kkckc.tistory.com/70����
	// http://story0111.tistory.com/36 ����
	// http://gcyong.tistory.com/53
	// �����ڷ� �뷡 �̸��� �ݺ����θ� �޴´�.

}
