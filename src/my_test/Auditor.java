package my_test;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;

public class Auditor{
    Element APK;
    Element ASK;
    
    Auditor(PP pp){
    	this.ASK=pp.generateZr();
    	APK=pp.g_(ASK).getImmutable();
    }
 
    Element Audit(CT ct,Element ASK, PP pp){
    	return ct.C0.div(ct.Ca.powZn(ASK));
    }
    public static void main(String arg[]) {
		int rBit=160; // order p of Zp size
		int qBit=521; // order of G size521
		PP pp=new PP(rBit,qBit);
		
		for(int i=0;i<5;i++) {
			Auditor a=new Auditor(pp);
			Element gsk=pp.g_(a.ASK);
			System.out.println(a.APK.toString()+"\n"+ gsk.toString()+"\n");
		}
		
    }
}