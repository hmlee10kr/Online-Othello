package othello;

import java.io.*;

import javazoom.jl.player.Player;

// BGM, 사운드 이펙트 재생을 위한 클래스
public class Music extends Thread {

	private Player player; // 사용자
	private boolean isLoop; // 반복 여부를 위한 변수
	private File file; // 음악 파일을 받기 위한 변수
	private FileInputStream fis; // 파일을 전달하기 위한 변수
	private BufferedInputStream bis; // 플레이어 사용 및 생성을 위한 변수

	// 이 클래스는 javazoom을 사용하여 만들어 졌다.

	public Music(String name, boolean isLoop) {
		try {
			file = new File(Main.class.getResource("../music/" + name).toURI());
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
			this.isLoop = isLoop;
			// 파일을 toURI로 받아와서 file변수에 넣고
			// fis안에 파일 inputstream으로 파일을 전달해준다.
			// bis에 fis를 버퍼 인풋스트림으로 전달하고
			// 플레이어를 bis를 사용하여 생성한다.
			// 반복 여부를 받는 isLoop boolean변수에 값을 넣는다
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}// constructor
		// 하위 디렉토리에 있는 음악 파일에 접근한다

	// 3.other functions 구현
	// 시간
	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}
	// 플레이어의 시간과 노트가 내려오는 시간을 비교하여 싱크를 맞추어야 하므로
	// 시간을 받아오는 gettime을 구현한다.

	// 곡 종료
	public void close() {
		isLoop = false;
		player.close();
		this.interrupt();// 곡 종료
	}
	// 루프 거짓으로 초기화
	// 플레이어 닫고
	// MUSIC을 interrupt시키면서 끝낸다.

	// start 부분
	@Override
	public void run() {
		// thread 상속 받으면 무조건 해야함
		try {
			do {
				player.play();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
			} while (isLoop);// isLoop가 참인동안 계속 플레이어를 실행시키고 똑같은 파일을 받아온다

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	// http://kkckc.tistory.com/70참고
	// http://story0111.tistory.com/36 참고
	// http://gcyong.tistory.com/53
	// 생성자로 노래 이름과 반복여부를 받는다.

}
