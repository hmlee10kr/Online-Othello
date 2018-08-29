package othello;

// 상태정보를 enum 열거형으로 정의
public enum Status
{
	WHITE, BLACK, EMPTY, BOUND;	// 오델로 알고리즘의 구현상의 편의를 위해 모서리 부분을 BOUND 로 사용하게 된다 (바둑판 사이즈는 [n+2][n+2] 가 된다)
}