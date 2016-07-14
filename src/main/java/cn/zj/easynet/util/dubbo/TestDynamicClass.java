package cn.zj.easynet.util.dubbo;


public class TestDynamicClass {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String code = "package cn.zj.easynet.util.dubbo;public class Tank3 implements cn.zj.easynet.util.dubbo.Machine {\npublic void run() {        System.out.println(\"Tank3 is running!\");    }}";
        JavassistCompiler compile = new JavassistCompiler();
        Class<? extends Machine> machineCls = (Class<? extends Machine>) compile.compile(code, TestDynamicClass.class.getClassLoader());
        Machine machine = machineCls.newInstance();
        machine.run();
        

    }
}
