package my_test;

import java.math.BigInteger;
import java.util.HashMap;

public class LSSS {
	Matrix A; //l x n 
	HashMap<Integer,Integer> rho;// [l] -> U
	int l;
	int n;
	
	LSSS(Matrix A, HashMap<Integer,Integer> rho ){
		this.A=A;
		this.l=A.getRows();
		this.n=A.getCols();
		this.rho=rho;
	}
	int rho(Integer j) {
		
		return rho.get(j).intValue();
	}
}
