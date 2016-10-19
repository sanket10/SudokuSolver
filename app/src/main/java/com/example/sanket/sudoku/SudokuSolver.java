package com.example.sanket.sudoku;

/**
 * Created by Happy on 10/18/2016.
 *
 * only accept 9*9 grid and first you have to call initialChecking() method
 * otherwise you will get false
 *
 * if initialChecking() method return -1 then only you solve sudoku otherwise no
 *
 * if solveSudoku() method return true than only you will get correctly solve sudoku
 * otherwise you'll get unsolved incorrect sudoku
 * */

public class SudokuSolver {
    private boolean intial_process_done = false;

    public int initialChecking(int grid[][]){
        for(int row = 0;row < 9;row++){
            for(int column = 0;column < 9;column++){
                if(grid[row][column] != 0){
                    int number = grid[row][column];
                    grid[row][column] = 0;
                    if(!checkBox(grid,row,column,number)){
                        grid[row][column] = number;
                        return row*10+column;
                    }
                    grid[row][column] = number;
                }
            }
        }
        this.intial_process_done = true;
        return -1;
    }

    public boolean solveSudoku(int grid[][]){
        if(!this.intial_process_done){
            return false;
        }
        int location = findEmptyLocation(grid);
        int row = -1;
        int column = -1;
        if(location == -1){
            return true;
        }
        row = location / 10;
        column = location % 10;
        for(int number = 1;number < 10;number++){
            //System.out.println(row+" "+column+" "+number);
            if(checkBox(grid,row,column,number)){
                grid[row][column] = number;

                //System.out.println("run "+row+" "+column+" "+number);
                if(solveSudoku(grid)){
                    return true;
                }
                grid[row][column] = 0;
            }
        }
        return false;
    }

    private int findEmptyLocation(int grid[][]){
        int row = 0;
        int column = 0;
        for(row = 0;row < 9;row++){
            for(column = 0;column < 9;column++){
                if(grid[row][column] == 0){
                    return row*10+column;
                }
            }
        }
        return -1;
    }
    private boolean checkInRow(int grid[][],int row,int num){
        for(int column = 0;column < 9;column++){
            if(grid[row][column] == num){
                return false;
            }
        }
        return true;
    }
    private boolean checkInColumn(int grid[][],int column,int num){
        for(int row = 0;row < 9;row++){
            if(grid[row][column] == num){
                return false;
            }
        }
        return true;
    }
    private boolean checkInBox(int grid[][],int row,int column,int num){
        int cons_row = row - row % 3;
        int cons_column = column - column % 3;
        for(row = 0;row < 3;row++){
            for(column = 0;column < 3;column++){
                if(grid[cons_row+row][cons_column+column] == num){
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkBox(int grid[][],int row,int column,int number){
        return checkInRow(grid,row,number) &&
                checkInColumn(grid,column,number) &&
                checkInBox(grid,row,column,number);
    }
}
