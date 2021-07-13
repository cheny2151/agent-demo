package cn.cheny.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author by chenyi
 * @date 2021/7/5
 */
public class AgentMain {

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("agent enter");
        inst.addTransformer(new MyClassFileTransformer(), true);
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class clazz : allLoadedClasses) {
            if (clazz.getName().equals("cn.cheny.toolbox.scan.PathScanner")){
                System.out.println("retransform class");
                inst.retransformClasses(clazz);
            }
        }
    }

}
