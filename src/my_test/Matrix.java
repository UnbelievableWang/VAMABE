package my_test;

import java.math.BigInteger;
import java.util.Arrays;

public class Matrix {
    private BigInteger[][] data;
    private int rows;
    private int cols;
    // Constructor
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new BigInteger[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(this.data[i], BigInteger.ZERO);
        }
    }

    // Getters
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public BigInteger get(int row, int col) {
        return data[row][col];
    }

    // Setters
    public void set(int row, int col, BigInteger value) {
        data[row][col] = value;
    }

    // Initialize matrix with a 2D array
    public Matrix(BigInteger[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new BigInteger[rows][cols];
        for (int i = 0; i < rows; i++) {
            this.data[i] = Arrays.copyOf(data[i], cols);
        }
    }

    // Addition
    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrices dimensions do not match for addition.");
        }

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, this.get(i, j).add(other.get(i, j)));
            }
        }

        return result;
    }

    // Multiplication
    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Matrices dimensions do not match for multiplication.");
        }

        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                BigInteger sum = BigInteger.ZERO;
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.get(i, k).multiply(other.get(k, j)));
                }
                result.set(i, j, sum);
            }
        }

        return result;
    }

    // Print matrix
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
}

