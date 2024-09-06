package my_test;

import java.math.BigInteger;
import java.util.HashMap;

import it.unisa.dia.gas.jpbc.Element;

public class CT {
    LSSS Arho;
    Element C0, Cp, Ca;
    Element[][] C3j;
    int j ;

    CT(LSSS Arho, Element C0, Element Cp, Element Ca ) {
        this.Arho = Arho;
        this.C0 = C0;
        this.Cp = Cp;
        this.Ca = Ca;
        this.j=0;
        // Initialize the array with a fixed size (maxSize)
        this.C3j = new Element[10][3]; // Each row will hold 3 Elements: C1, C2, C3
    }
    public void print() {
    	System.out.println(C0.toString()+Cp.toString()+Ca.toString());
    	for(int i=0;i<this.j;i++) {
    		System.out.println(C3j[i][0].toString()+C3j[i][1].toString()+C3j[i][2].toString());
    	}
    }
    
    public void addAtt(Element C1, Element C2, Element C3) {
        // Ensure we do not exceed the array bounds
        if (j < C3j.length) {
            Element[] Ci = new Element[]{C1, C2, C3};
            this.C3j[j] = Ci;
            j++;
            //this.print();
        } else {
            System.out.println("Cannot add more elements. Array is full.");
        }
    }
    public static void main(String arg[]) {
		int rBit=160; // order p of Zp size
		int qBit=521; // order of G size521
		PP pp=new PP(rBit,qBit);
		Integer U[]= {0,1,2,3,4}; // Universe
		Matrix A=new Matrix(5,6);
		for(int i=0;i<5;i++) {
			for(int j=0;j<6;j++) {
				if(i==j) {
					A.set(i, j, new BigInteger("1"));
				}else {
					A.set(i, j, new BigInteger("0"));
				}
			}
			
		}
		Matrix c=new Matrix(1,5);
		c.set(0, 0, new BigInteger("1"));
		for(int i=1;i<5;i++) {
			c.set(0, i, new BigInteger("0"));
		}
		
		A.set(0, 0, new BigInteger("1"));
		
		HashMap<Integer,Integer> rho =new HashMap<Integer,Integer>();
		for(int i=0;i<5;i++) {
			rho.put(i, U[i]);
		}
		
		LSSS Arho=new LSSS( A, rho);
		
		CT ct=new CT(Arho,pp.g_(new BigInteger("0")),pp.g_(new BigInteger("1")),pp.g_(new BigInteger("2")));
		
		for(int i=0;i<2;i++) {
			ct.addAtt(pp.g_(new BigInteger("0")),pp.g_(new BigInteger("1")),pp.g_(new BigInteger("2")));
		}
		
    }
}
