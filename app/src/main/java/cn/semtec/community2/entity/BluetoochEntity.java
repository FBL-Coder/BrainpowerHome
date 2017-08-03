package cn.semtec.community2.entity;

public class BluetoochEntity {
	/**检验和*/
	byte[] inspectionSum = new byte[1];
	public static byte getInspectionSum(byte[] inspectionSum) {
		byte checkSum = 0;
		for(int i =0;i< inspectionSum.length;i++) {
		checkSum+=inspectionSum[i];
		}
		return checkSum;
	}
	public void setInspectionSum(byte[] inspectionSum) {
		this.inspectionSum = inspectionSum;
	}
/*	private static String getSum(byte[] inspectionSum){
		int sum = 0;
		 for(int i = 0;i < inspectionSum.length;i++){
			 sum=sum +(inspectionSum[i]&0xff);
		 }
        System.out.println(sum);
		String s = Integer.toHexString(256-sum);
		return (s.substring(s.length()-2, s.length()));
	}*/
}
