package cn.cheny.agent;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

/**
 * @author by chenyi
 * @date 2021/7/5
 */
public class StartDemo {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor virtualMachineDescriptor : list) {
            if (virtualMachineDescriptor.displayName().equals("cn.cheny.toolbox.scan.PathScanner")) {
                VirtualMachine attach = null;
                try {
                    attach = VirtualMachine.attach(virtualMachineDescriptor);
                    attach.loadAgent("/Users/chenyi/IdeaProjects/cheny/agent/target/attach-demo-1.0-SNAPSHOT.jar");
                } finally {
                    attach.detach();
                }
            }
        }
    }

}
