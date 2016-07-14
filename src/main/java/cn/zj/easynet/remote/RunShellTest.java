package cn.zj.easynet.remote;

public class RunShellTest {
	public static void restartRemoteServer() {
		// TODO Auto-generated method stub
		String shpath="/data/guoj/test.sh";   //程序路径
	    Process process =null;
	    String command1 = "chmod 777 " + shpath;
	    try {
			process = Runtime.getRuntime().exec(command1);
			process.waitFor();

			String command2 = shpath;
			Runtime.getRuntime().exec(command2).waitFor();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
