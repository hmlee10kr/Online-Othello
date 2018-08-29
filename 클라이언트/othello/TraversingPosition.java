package othello;

// 돌을 두고나서 상태정보 갱신을 위해 돌을직접 8방향으로 이동시켜 보기위한 클래스
public class TraversingPosition extends Position
{
	public TraversingPosition()
	{
		super();
	}
	public TraversingPosition(int row, int col)
	{
		super(row,col);
	}
	
	// 돌을 이동시켜 보는 메소드들
	public void moveUp()	{ row--; }	// 상
	public void moveDown()	{ row++; }	// 하
	public void moveLeft()	{ col--; }	// 좌
	public void moveRight()	{ col++; }	// 우
	public void moveRightUp()	{ moveUp(); moveRight(); }	// 상/우
	public void moveRightDown()	{ moveDown(); moveRight(); }	// 하/우
	public void moveLeftUp()	{ moveUp(); moveLeft(); }	// 상/좌
	public void moveLeftDown()	{ moveDown(); moveLeft(); }		// 하/좌
} // TraversingPosition Class