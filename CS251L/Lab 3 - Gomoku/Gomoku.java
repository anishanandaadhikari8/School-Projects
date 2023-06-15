//Anish Adhikari UNM- aaadhikari999@gmail.com

import cs251.lab3.GomokuGUI;
import cs251.lab3.GomokuModel;

// Player 1 is set to RING, Player 2 is set to CROSS
public class Gomoku implements GomokuModel {

	
	public static GomokuModel.Square[][] m_squareBoard;
	public static Gomoku m_game;
	public static boolean m_playerOneTurn;
	public static boolean m_isAI;
	
	public static void main(String[] args) {
		// Initializing objects
		m_game = new Gomoku();

		if (args.length > 0) {
			m_game.setComputerPlayer(args[0]);
		}
		GomokuGUI.showGUI(m_game);
	}
	
	
	
	
	
	
	
	public Gomoku() {

	}
	
	public int getNumRows() {
		//return DEFAULT_NUM_ROWS;
		return 5;
	}
	
	public int getNumCols() {
		//return DEFAULT_NUM_COLS;
		return 5;
	}
	
	public int getNumInLineForWin() {
		//return SQUARES_IN_LINE_FOR_WIN;
		return 3;
	}
	
	public Outcome playAtLocation(int row, int col) {
		
		Outcome gameState = GomokuModel.Outcome.GAME_NOT_OVER;
		
		System.out.println("AI is enabled: " + m_isAI);
		
		// Checking to make sure the space being clicked on is empty before placing a new symbol
		if(m_squareBoard[row][col].equals(GomokuModel.Square.EMPTY)) {
			if(m_playerOneTurn) { 
				m_squareBoard[row][col] = GomokuModel.Square.RING;
			}
			else {
				m_squareBoard[row][col] = GomokuModel.Square.CROSS;
			}
			
			if(m_isAI) {
				makeBasicAIMove();
			}
			
			gameState = checkForWin(row, col);
			
			m_playerOneTurn ^= true;
			
		}

		return gameState;
	}
	
	
	
	// TODO: Check if there is a valid move left... board might be full
	public void makeBasicAIMove() {
		
		boolean madeAMove = false;
		
		while(!madeAMove) {
			for(int i = 0; i < getNumRows(); ++i) {
				for(int j = 0; j < getNumCols(); ++j) {
					if(m_squareBoard[i][j] == GomokuModel.Square.EMPTY) {
						// TODO: TEST CODE
						m_squareBoard[i][j] = GomokuModel.Square.CROSS;
						madeAMove = true;
					}
				}
			}
		}
		

		
		
		// Change the turn
		m_playerOneTurn ^= true;
	}
	
	// TODO: Solve for diagonals
	public Outcome checkForWin(int row, int col) {
		
		GomokuModel.Square initial = m_squareBoard[row][col];
		int winNum = getNumInLineForWin();
		
		// Horizontal and vertical checks
		int horiRight = 0;
		int vertDown = 0;
		int horiLeft = 0;
		int vertUp = 0;
		
		for(int k = 0; k < getNumInLineForWin(); ++k) {
			if(col <= getNumCols() - winNum && m_squareBoard[row][col+k].equals(initial)) {
				horiRight++;
			}
			if(col >= winNum - 1 && m_squareBoard[row][col-k].equals(initial)) {
				horiLeft++;
			}

			if(row <= getNumRows() - winNum && m_squareBoard[row+k][col].equals(initial)) {
				vertDown++;
			}
			if(row >= winNum - 1 && m_squareBoard[row-k][col].equals(initial)) {
				vertUp++;
			}
			
			if(horiRight >= winNum || horiLeft >= winNum ||
					vertDown >= winNum || vertUp >= winNum) {
				if(initial.equals(GomokuModel.Square.CROSS)) {
					return GomokuModel.Outcome.CROSS_WINS;
				}
				else {
					return GomokuModel.Outcome.RING_WINS;
				}
			}
		}
		
		// Diagonal checks
		int diagUpRight = 0;
		int diagUpLeft = 0;
		int diagDownRight = 0;
		int diagDownLeft = 0;
		
		
		
		// If a win wasn't found, just continue the game
		return GomokuModel.Outcome.GAME_NOT_OVER;
	}
	
	
	
	public void startNewGame() {
		System.out.println("Started new game"); //TODO: REMOVE THIS
		
		// Getting a board of size row * col
		m_squareBoard = new GomokuModel.Square[getNumRows()][getNumCols()];
		for(int i = 0; i < getNumRows(); ++i) {
			for(int j = 0; j < getNumCols(); ++j) {
				m_squareBoard[i][j] = GomokuModel.Square.EMPTY;
			}
		}
		// Starting game with player one(RING) going first
		m_playerOneTurn = true;
	}
	
	public String getBoardString() {
		// Extracting the string out of the Square array
		StringBuilder returnString = new StringBuilder();
		for(int i = 0; i < getNumRows(); ++i) {
			for(int j = 0; j < getNumCols(); ++j) {
				returnString.append(m_squareBoard[i][j].toChar());
			}
			returnString.append("\n");
		}
		
		return returnString.toString();
	}
	
	public void setComputerPlayer(String opponent) {
		System.out.println(opponent); // TODO: REMOVE THIS
		
		m_isAI = opponent.equals("COMPUTER") ? true : false;	
	}

}
