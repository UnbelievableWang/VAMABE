package my_test;

import java.math.BigInteger;
import java.util.HashMap;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

public class PP {
	Element g;
	Element egg;
    
	Pairing bp;
	Field G1;
	Field Zq;
	HashMap<String,Element> hash;
	
	PP(int rBit, int qBit){
		TypeACurveGenerator pg=new TypeACurveGenerator(rBit,qBit);// A types symmetric prime order group
		PairingParameters pp=pg.generate();
		
		this.bp =PairingFactory.getPairing(pp);
		
		
		this.G1=bp.getG1();
		this.Zq=bp.getZr();
		this.g=G1.newRandomElement().getImmutable();
		this.egg=bp.pairing(g, g).getImmutable();
		this.hash=new HashMap<String,Element>();
	}
	public Element getg() {
		return this.g.duplicate();
	}
	public Element g_(Element z) {
		return g.powZn(z);
	}
	public Element g_(BigInteger z) {
		return g.pow(z);
	}
	public Element egg_(Element z) {
		return egg.powZn(z);
	}
	public Element egg_(String z) {
		return egg.pow(new BigInteger(z));
	}
	public Element egg(Element z) {
		return bp.pairing(g, g_(z));
	}
	public Element e(Element ga,Element gb) {
		return bp.pairing(ga, gb);
	}
	public Element H(String RID) {
		Element hr=hash.get(RID);
		if(hr==null) {
			hr=g_(Zq.newRandomElement().getImmutable());
			hash.put(RID,hr);
		}
		return hr;
	}
	public Element generateZr() {
		return Zq.newRandomElement().getImmutable();
	}
	public static void main(String arg[]) {
		int rBit=160; // order p of Zp size
		int qBit=521; // order of G size521
		PP pp=new PP(rBit,qBit);
		
		Element resres=pp.g.mul(pp.g.pow(new BigInteger("-1")));
		Element resres1=pp.g.div(pp.g);
		//System.out.print(resres.toString()+"\n"+resres1.toString());
		for(int i=0;i<4;i++) {
			Element k1=pp.generateZr().getImmutable();
			Element k2=pp.generateZr().getImmutable();
			System.out.print(k1.toString()+"\n"+k2.toString()+"\n");
		}
		
		
	}
}
