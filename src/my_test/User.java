package my_test;

import java.math.BigInteger;
import java.util.HashMap;

import it.unisa.dia.gas.jpbc.Element;
public class User {
	String RID;
	Integer[] attributes;
	HashMap<Integer,Element> hash; //i,K
	
	User(String RID, Integer[] attributes){
		this.RID=RID;
		this.attributes=attributes;
		this.hash = new HashMap<Integer,Element>();
	}
	void getAttKey(Authority a, PP pp) {
		
		Element K=a.KeyGen(RID, pp);
		
		hash.put(a.getAtt(), K);
		//System.out.println(a.i.toString()+"getKey:"+K.toString());
	}
	
	CT Enc(Element APK,String SID, Element PK[][], LSSS Arho, Element M, PP pp) { // PK l X 2
		
		//System.out.println("encrypted M="+M.toString());
		Element hs=pp.H(SID);
		Element s=pp.generateZr().getImmutable();
		Element W=pp.bp.pairing(APK, hs).getImmutable();
		
		
		//System.out.println("WS0="+W.powZn(s).toString());
		//System.out.println("encrypted W="+W.toString());
		Element C0=M.mul(W.powZn(s)).getImmutable();
		//System.out.println("encrypted C0="+C0.toString());
		
		Element Cp=pp.g_(s).getImmutable();
		Element Ca=pp.bp.pairing(Cp, hs).getImmutable();
		CT ct=new CT(Arho,C0,Cp,Ca);
		Matrix v=new Matrix(Arho.n,1);
		Matrix w=new Matrix(Arho.n,1);
		
		v.set(0, 0, s.toBigInteger());
		w.set(0, 0, new BigInteger("0"));
		
		
		for(int i=1;i<Arho.n;i++) {
			Element rv=pp.generateZr().getImmutable();
			Element rw=pp.generateZr().getImmutable();
			v.set(i, 0, rv.toBigInteger());
			w.set(i, 0, rw.toBigInteger());
		}
		

		Matrix sharS= Arho.A.multiply(v);
		Matrix shar0=Arho.A.multiply(w);
		//shar0.print();
		//System.out.println("WS1="+W.pow(sharS.get(0, 0)).toString());
		//System.out.println("encrypted C01="+ct.C0.toString());
		for(int j=0;j<Arho.l;j++) {
			Element rj=pp.generateZr().getImmutable();
			

			Element C1j=W.duplicate().pow(sharS.get(j, 0)).mul(PK[Arho.rho(j)][0].powZn(rj)).getImmutable();
			Element C2j=pp.g_(rj).getImmutable();
			Element C3j=pp.g_(shar0.get(j, 0)).mul(PK[Arho.rho(j)][1].powZn(rj)).getImmutable();
			ct.addAtt(C1j, C2j, C3j);
		}
		//System.out.println("encrypted C02="+ct.C0.toString());
		return ct;
	}
	
	Element Dec(CT ct, PP pp, Matrix c) {
		
		Element CTjPI=pp.egg_("0");
		for(int j=0;j<ct.j;j++) {
			
			
			
			Element ehrc3j=pp.bp.pairing(pp.H(RID), ct.C3j[j][2]).getImmutable();
			Element ekc2j=pp.bp.pairing(hash.get(ct.Arho.rho(j)), ct.C3j[j][1]).getImmutable();
			Element CTj=ct.C3j[j][0].mul(ehrc3j).div(ekc2j).getImmutable();
			//System.out.println("powC="+CTj.pow(c.get(0, j)).toString());
			CTjPI=CTjPI.mul(CTj.pow(c.get(0, j)));
//			System.out.println("powC="+CTj.pow(c.get(0, j)).toBigInteger().toString());
//			System.out.println("ehrc3j="+ehrc3j.toBigInteger().toString());
//			System.out.println("ekc2j="+ekc2j.toBigInteger().toString());
//			System.out.println("CTj="+CTj.toBigInteger().toString());
//			System.out.println("CTjPI="+CTjPI.toString());
//			
		}
	//	System.out.println("WS2="+CTjPI.toString());
	//	ct.print();
		Element M=ct.C0.div(CTjPI); 
		return M;
	}
	Element P_X1(CT ct, PP pp, Element u) {
		
		Element X1=pp.bp.pairing(pp.g_(u), ct.Cp);
		return X1;
	}
	Element V_c(PP pp) {
		Element z=pp.generateZr();
		return z;
	}
	Element P_X2(Element c,PP pp, Element u) {
		Element X2=pp.H(RID).powZn(c).mul(pp.g_(u));
		return X2;
	}
	boolean V_Check(Element X2,Element gs,Element X,Element c,Element X1,PP pp) {
		Element left=pp.bp.pairing(X2, gs);
		Element right=X.powZn(c).mul(X1);
		boolean res=left.isEqual(right);
		
		return res;
	}
}



//Element Dec1(CT ct, PP pp, Matrix c, Element PK[][]) {
//		
//		//Element CTjPI=pp.egg_("0");
//
//			Element Ws= ct.C3j[0][0].div(PK[0][0].powZn(this.rj));
//			System.out.println("WS2="+Ws.toString());
////			
//			Element gyr=ct.C3j[0][2];
//			Element pk=PK[0][1].powZn(this.rj);
//			System.out.println("gy="+gyr.toString()+"\n"+pk.toString());
//	//	ct.print();
//		Element M=ct.C0.div(Ws); 
//		return M;
//	}
//}
