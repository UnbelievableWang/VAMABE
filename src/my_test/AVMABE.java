package my_test;
import it.unisa.dia.gas.jpbc.Element;
import java.math.BigInteger;
import java.util.HashMap;
import java.time.Instant;
public class AVMABE {
	static double timeGsetup=0;
	static double timeAutSetup=0;
	static double timeAudSetup=0;
	static double timeKeyGen=0;
	static double timeEnc=0;
	static double timeDec=0;
	static double timeAud=0;
	static double timeVerify=0;
	static double number=0;
	public static void print() {
	    System.out.println("timeGsetup: " + timeGsetup/number);
	    System.out.println("timeAutSetup: " + timeAutSetup/number);
	    System.out.println("timeAudSetup: " + timeAudSetup/number);
	    System.out.println("timeKeyGen: " + timeKeyGen/number);
	    System.out.println("timeEnc: " + timeEnc/number);
	    System.out.println("timeDec: " + timeDec/number);
	    System.out.println("timeAud: " + timeAud/number);
	    System.out.println("timeVerify: " + timeVerify/number);
	}
	public void tprint() {
	    System.out.println("timeGsetup: " + timeGsetup);
	    System.out.println("timeAutSetup: " + timeAutSetup);
	    System.out.println("timeAudSetup: " + timeAudSetup);
	    System.out.println("timeKeyGen: " + timeKeyGen);
	    System.out.println("timeEnc: " + timeEnc);
	    System.out.println("timeDec: " + timeDec);
	    System.out.println("timeAud: " + timeAud);
	    System.out.println("timeVerify: " + timeVerify);
	    System.out.println("number: " + number);
	}


	public void test(int numAtt) {
	    number++; // 假设 number 是类中的某个静态变量
	    long time0 = System.nanoTime();
	    
	    // GSetUp
	    int rBit = 160; // order p of Zp size
	    int qBit = 521; // order of G size 521
	    PP pp = new PP(rBit, qBit);
	    
	    long time1 = System.nanoTime();
	    timeGsetup += (time1 - time0) / 1_000_000.0; // 计算 GSetUp 阶段的时间（毫秒）
	    
	    // AutSetup
	    Integer U[] = new Integer[numAtt];
	    for (int i = 0; i < numAtt; i++) {
	        U[i] = i;
	    }
	    
	    Authority Aut[] = new Authority[numAtt];
	    for (int i = 0; i < numAtt; i++) {
	        Aut[i] = new Authority(pp, U[i]);
	    }
	    
	    long time2 = System.nanoTime();
	    timeAutSetup += (time2 - time1) / 1_000_000.0; // 计算 AutSetup 阶段的时间（毫秒）
	    
	    // AudSetup
	    Auditor Aud = new Auditor(pp);
	    Element APK = Aud.APK;
	    
	    long time3 = System.nanoTime();
	    timeAudSetup += (time3 - time2) / 1_000_000.0; // 计算 AudSetup 阶段的时间（毫秒）
	    
	    // KeyGen
	    Integer Attu[] = new Integer[numAtt];
	    for (int i = 0; i < numAtt; i++) {
	        Attu[i] = i;
	    }
	    
	    User Alice = new User("Alice", Attu);
	    for (int i = 0; i < numAtt; i++) {
	        for (int j = 0; j < numAtt; j++) {
	            if (Aut[j].i == Attu[i]) {
	                Alice.getAttKey(Aut[j], pp);
	            }
	        }
	    }
	    
	    long time4 = System.nanoTime();
	    timeKeyGen += (time4 - time3) / 1_000_000.0; // 计算 KeyGen 阶段的时间（毫秒）
	    
	    // Enc
	    Element M = pp.egg_("80");
	    System.out.println("Mtobeencrypted=" + M.toString());
	    Element PK[][] = new Element[numAtt][2];
	    
	    for (int i = 0; i < numAtt; i++) {
	        PK[i][0] = Aut[i].PK1;
	        PK[i][1] = Aut[i].PK2;
	    }
	    
	    long time5 = System.nanoTime();
	    timeEnc += (time5 - time4) / 1_000_000.0; // 计算 Enc 阶段的时间（毫秒）
	    
	    // Construct LSSS
	    Matrix A = new Matrix(numAtt, numAtt + 1);
	    for (int i = 0; i < numAtt; i++) {
	        for (int j = 0; j < numAtt + 1; j++) {
	            if (i == j) {
	                A.set(i, j, new BigInteger("1"));
	            } else {
	                A.set(i, j, new BigInteger("0"));
	            }
	        }
	    }
	    
	    Matrix c = new Matrix(1, numAtt);
	    c.set(0, 0, new BigInteger("1"));
	    for (int i = 1; i < numAtt; i++) {
	        c.set(0, i, new BigInteger("0"));
	    }
	    
	    HashMap<Integer, Integer> rho = new HashMap<Integer, Integer>();
	    for (int i = 0; i < numAtt; i++) {
	        rho.put(i, U[i]);
	    }
	    
	    LSSS Arho = new LSSS(A, rho);
	    
	    CT ct = Alice.Enc(APK, "Alice", PK, Arho, M, pp);
	    
	    long time6 = System.nanoTime();
	    timeEnc += (time6 - time5) / 1_000_000.0; // 计算 Enc 阶段的时间（毫秒）
	    
	    // Dec
	    Element Message = Alice.Dec(ct, pp, c);
	    System.out.println("Mdcrypted=" + Message.toString());
	    
	    long time7 = System.nanoTime();
	    timeDec += (time7 - time6) / 1_000_000.0; // 计算 Dec 阶段的时间（毫秒）
	    
	    // Aud
	    Element Maud = Aud.Audit(ct, Aud.ASK, pp);
	    System.out.println("Maudited=" + Maud.toString());
	    
	    long time8 = System.nanoTime();
	    timeAud += (time8 - time7) / 1_000_000.0; // 计算 Aud 阶段的时间（毫秒）
	    
	    // Verify
	    User Bob = new User("Bob", Attu);
	    
	    Element u = pp.generateZr();
	    Element X1 = Alice.P_X1(ct, pp, u);
	    Element beta = Bob.V_c(pp);
	    Element X2 = Alice.P_X2(beta, pp, u);
	    boolean res = Bob.V_Check(X2, ct.Cp, ct.Ca, beta, X1, pp);
	    System.out.println("check=" + res);
	    
	    long time9 = System.nanoTime();
	    timeVerify += (time9 - time8) / 1_000_000.0; // 计算 Verify 阶段的时间（毫秒）
	}

	
	public static void main(String[] arg) {
		
		for(int i=0;i<50;i++) {
			AVMABE sys=new AVMABE();
			sys.test(5);
			sys.tprint();
		}

		AVMABE.print();
	}
	
}
