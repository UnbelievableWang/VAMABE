package my_test;

import java.lang.System;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import org.github.jamm.*;

import libs.*;

public class Helloworld {
	
	public static void testcom() {
		
		long stime,etime;
		stime=System.currentTimeMillis();
		
		int rBit=160; // order p of Zp size
		int qBit=521; // order of G size521
		int numPrime= 3; // how many primes consists of composite.
		//TypeACurveGenerator pg=new TypeACurveGenerator(rBit,qBit);// A types symmetric prime order group
		TypeA1CurveGenerator pg=new TypeA1CurveGenerator(numPrime,qBit); //A1 types symmetric composite group
		PairingParameters pp=pg.generate();
		
		Pairing bp =PairingFactory.getPairing(pp);

		Field G1=bp.getG1();
		Field Zr=bp.getZr();
		Element g=G1.newRandomElement().getImmutable();
		Element a=Zr.newRandomElement().getImmutable();
		Element b=Zr.newRandomElement().getImmutable();
		
		etime=System.currentTimeMillis();
		System.out.println("ParaSetup "+(etime-stime)+"ms");
		
		//get zr
		
		stime=System.currentTimeMillis();
		    Zr=bp.getZr();
		etime=System.currentTimeMillis();
		System.out.println("Choose zr "+(etime-stime)+"ms");
		
		Element ga=g.powZn(a);
		Element gb=g.powZn(b);
		Element egg_ab=bp.pairing(ga, gb);
		
		Element egggg=bp.pairing(g, g);
		
		Element egg=bp.pairing(g, g).getImmutable();
		Element ab=a.mul(b);
		Element egg_ab_p=egg.powZn(ab);
		
		
		// TODO Auto-generated method stub
		//size
		Caculator calculator = new Caculator() ;
		long sofCur=calculator.sizeOf(bp);
		long sofZr=calculator.sizeOf(Zr);
		long sofG1=calculator.sizeOf(ga);
		long sofegg=calculator.sizeOf(egg);
		System.out.println("sofZr="+sofZr);
		System.out.println("sofG1="+sofG1);
		System.out.println("sofEGG="+sofegg);
		

		
		MemoryMeter meter= new MemoryMeter();
		
		System.out.println("sofZr="+meter.measure(bp));
		System.out.println("sofG1="+meter.measure(sofG1));
		System.out.println("sofEGG="+meter.measure(sofegg));

		
		
		
		//composite
		// test G1 paring;
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			egg_ab=bp.pairing(ga, gb);
		}
		etime=System.currentTimeMillis();
		System.out.println("paring in G is "+(etime-stime)+"ms");
		
		//test Gt multiply;
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			egg_ab=egg_ab.mul(egggg);
		}
		etime=System.currentTimeMillis();
		System.out.println("multiplication in Gt is "+(etime-stime)+"ms");
		
		//test Gt exponential
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			egg_ab=egg_ab.powZn(a);
		}
		etime=System.currentTimeMillis();
		System.out.println("exponential in Gt is "+(etime-stime)+"ms");
		

		
		//test G1 exponential
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			ga.powZn(a);
		}
		etime=System.currentTimeMillis();
		System.out.println("exponential in G is "+(etime-stime)+"ms");

		//test G1xG1
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			ga.mul(ga);
		}
		etime=System.currentTimeMillis();
		System.out.println("G1xG1 is "+(etime-stime)+"ms");
		
		//zn mulab=a.mul(b);
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			ab=a.mul(b);
		}
		etime=System.currentTimeMillis();
		System.out.println("mulabzp "+(etime-stime)+"ms");
		
	}
	
	public static void testprim() {
		int rBit=160; // order p of Zp size
		int qBit=521; // order of G size
		int numPrime= 1; // how many primes consists of composite.
		//TypeACurveGenerator pg=new TypeACurveGenerator(rBit,qBit);// A types symmetric prime order group
		TypeA1CurveGenerator pg=new TypeA1CurveGenerator(numPrime,qBit); //A1 types symmetric composite group
		PairingParameters pp=pg.generate();
		
		Pairing bp =PairingFactory.getPairing(pp);
		
		
		Field G1=bp.getG1();
		Field Zr=bp.getZr();
		Element g=G1.newRandomElement().getImmutable();
		Element a=Zr.newRandomElement().getImmutable();
		Element b=Zr.newRandomElement().getImmutable();
		
		Element ga=g.powZn(a);
		Element gb=g.powZn(b);
		Element egg_ab=bp.pairing(ga, gb);
		
		Element egg=bp.pairing(g, g);
		
		Element egggg=bp.pairing(g, g).getImmutable();
		Element ab=a.mul(b);
		Element egg_ab_p=egg.powZn(ab);
		
		
		// TODO Auto-generated method stub
		long stime,etime;
		
		//composite
		// test G1 paring;
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			egg_ab=bp.pairing(ga, gb);
		}
		etime=System.currentTimeMillis();
		System.out.println("paring in G is "+(etime-stime)+"ms");
		
		//test Gt multiply;
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			egg_ab=egg_ab.mul(egggg);
		}
		etime=System.currentTimeMillis();
		System.out.println("multiplication in Gt is "+(etime-stime)+"ms");
		
		//test Gt exponential
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			egg_ab=egg_ab.powZn(a);
		}
		etime=System.currentTimeMillis();
		System.out.println("exponential in Gt is "+(etime-stime)+"ms");
		
		//test G1 exponential
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			ga=g.powZn(a);
		}
		etime=System.currentTimeMillis();
		System.out.println("exponential in G is "+(etime-stime)+"ms");
		
		//test G1xG1
		
		stime=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			ga=g.mul(ga);
		}
		etime=System.currentTimeMillis();
		System.out.println("G1xG1 is "+(etime-stime)+"ms");
	}
	
	
	
	public static void main(String[] args) {
		
		System.out.println("testcom:");
		testcom();
		System.out.println("testprim:");
		testprim();
		
	}

}
