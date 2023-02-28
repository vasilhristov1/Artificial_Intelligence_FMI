#include <iostream>
#include <vector>
#include <time.h>
#include <chrono>
#include <iomanip>

void startingState(int N, int* queens, int* helper, int* diagonal1, int* diagonal2);
int colWithQueenWithMaxConflict(int N, int* queens, int* helper, int* diagonal1, int* diagonal2, bool& confl);
int rowWithMinConflict(int col, int N, int* queens, int* helper, int* diagonal1, int* diagonal2);
void update(int row, int col, int N, int* queens, int* helper, int* diagonal1, int* diagonal2);
void printQueens(int N, int* queens, int* helper);
void solve(int N, int* queens, int* helper, int* diagonal1, int* diag2, bool confl);

void startingState(int N, int* queens, int* helper, int* diagonal1, int* diagonal2)
{
	int col = 1;

	for (int row = 0; N > row; row++)
	{
		queens[col] = row;
		diagonal1[col - row + N - 1]++;
		diagonal2[col + row]++;
		helper[row]++;

		col += 2;

		if (N <= col)
		{
			col = 0;
		}
	}

}

int colWithQueenWithMaxConflict(int N, int* queens, int* helper, int* diagonal1, int* diagonal2, bool& confl)
{
	int maxConflict = -1;
	std::vector<int> colsWithMaxConflict;

	int currentRow, currentConflict;

	for (int currentCol = 0;  N > currentCol; currentCol++)
	{
		currentRow = queens[currentCol];

		currentConflict = helper[currentRow] + diagonal1[currentCol - currentRow + N - 1] + diagonal2[currentCol + currentRow] - 3;

		if (maxConflict == currentConflict)
		{
			colsWithMaxConflict.push_back(currentCol);
		}
		else if ( maxConflict < currentConflict)
		{
			maxConflict = currentConflict;
			colsWithMaxConflict.clear();
			colsWithMaxConflict.push_back(currentCol);
		}
	}

	if (maxConflict == 0)
	{
		confl = false;
	}

	int index = rand() % colsWithMaxConflict.size();

	return colsWithMaxConflict[index];
}

int rowWithMinConflict(int col, int N, int* queens, int* helper, int* diagonal1, int* diagonal2)
{
	int minConflict = N + 1;
	std::vector<int> rowsWithMinConflict;

	int currentConflict;

	for (int currentRow = 0; currentRow < N; currentRow++)
	{
		if (queens[col] == currentRow)
		{
			currentConflict = diagonal1[col - currentRow + N - 1] + diagonal2[col + currentRow] - 3 + helper[currentRow];
		}
		else
		{
			currentConflict = helper[currentRow] + diagonal1[col - currentRow + N - 1] + diagonal2[col + currentRow];
		}

		if (currentConflict == minConflict)
		{
			rowsWithMinConflict.push_back(currentRow);
		}
		else if (currentConflict < minConflict)
		{
			minConflict = currentConflict;
			rowsWithMinConflict.clear();
			rowsWithMinConflict.push_back(currentRow);
		}
	}

	int randIndex = rand() % rowsWithMinConflict.size();

	return rowsWithMinConflict[randIndex];
}

void update(int row, int col, int N, int* queens, int* helper, int* diagonal1, int* diagonal2)
{
	int lastRow = queens[col];
	helper[lastRow]--;
	diagonal1[col - lastRow + N - 1]--;
	diagonal2[col + lastRow]--;

	queens[col] = row;
	helper[row]++;
	diagonal1[col - row + N - 1]++;
	diagonal2[col + row]++;
}

void printQueens(int N, int* queens, int* helper)
{
	for (int row = 0; N > row; row++)
	{
		for (int col = 0; N > col; col++)
		{
			if (row == queens[col]) {
				std::cout << "* ";
			}
			else {
				std::cout << "_ ";
			}
		}
		std::cout << std::endl;
	}
}

void solve(int N, int* queens, int* helper, int* diagonal1, int* diagonal2, bool conflicts)
{
	int iter = 0;
	int k = 1;
	int col, row;

	for (int i = 0; k * N >= i; i++)
	{
		col = colWithQueenWithMaxConflict(N, queens, helper, diagonal1, diagonal2, conflicts);
		if (!conflicts)
		{
			break;
		}

		row = rowWithMinConflict(col, N, queens, helper, diagonal1, diagonal2);
		update(row, col, N, queens, helper, diagonal1, diagonal2);
	}

	if (conflicts)
	{
		solve(N, queens, helper, diagonal1, diagonal2, conflicts);
	}

}

int main()
{
	int N;
	std::cin >> N;

	//conflicts
	bool confl;
	int* queens = new int[N];
	int* helper = new int[N] {
		0
	};

	int* diagonal1;
	int* diagonal2;

	diagonal1 = new int[2 * N - 1]{
		0
	};

	diagonal2 = new int[2 * N - 1]{
		0
	};

	confl = true;

	auto start = std::chrono::high_resolution_clock::now();

	startingState(N, queens, helper, diagonal1, diagonal2);

	solve(N, queens, helper, diagonal1, diagonal2, confl);

	auto end = std::chrono::high_resolution_clock::now();

	if (100 > N) {
		printQueens(N, queens, helper);
	}

	std::cout << std::endl;

	double duration = std::chrono::duration_cast<std::chrono::nanoseconds>(end - start).count();
	duration *= 1e-9;

	std::cout << "Time: " << std::fixed << duration << std::setprecision(9) << std::endl;

	delete[] queens;
	delete[] helper;
	delete[] diagonal1;
	delete[] diagonal2;

	return 0;
}
