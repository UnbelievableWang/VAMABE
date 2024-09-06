package my_test;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;

public class Authority {
	public Integer i; //attribute
	Element PK1,PK2;
    Element alpha,y;
    
    Authority(PP pp, Integer i){// i in Zr
    	this.i=i;
    	this.alpha=pp.generateZr();
    	PK1=pp.egg_(alpha);
    	this.y=pp.generateZr();
    	PK2=pp.g_(y);
    }
    Element KeyGen(String RID, PP pp) {
    	Element KRi=pp.g_(alpha).mul(pp.H(RID).powZn(y));
    	return KRi;
    }
    Integer getAtt() {
    	return this.i;
    }
    Element getPKal() {
    	return PK1;
    }
    Element getPKgy() {
    	return PK2;
    }
    public static void main(String arg[]) {
		int rBit=160; // order p of Zp size
		int qBit=521; // order of G size521
		PP pp=new PP(rBit,qBit);
		
		for(int i=0;i<5;i++) {
			String RID="ddkj";
			Authority a=new Authority(pp,3);
			Element key=a.KeyGen("ddkj", pp);
			Element k=key.div(pp.H(RID).powZn(a.y));
			Element ga=pp.bp.pairing(k, pp.getg());
			System.out.println(ga+"\n"+a.PK1+"\n");
			
			Element hy=key.div(pp.g_(a.alpha));
			Element hy1=pp.H(RID).powZn(a.y);
			System.out.println(hy+"\n"+hy1+"\n");
		}
		
    }
}
