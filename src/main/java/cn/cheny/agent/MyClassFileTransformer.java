package cn.cheny.agent;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author by chenyi
 * @date 2021/7/5
 */
public class MyClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals("cn/cheny/toolbox/scan/PathScanner")) {
            System.out.println("do");
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
            System.out.println("new MyClassVisitor");
            try {
                MyClassVisitor myClassVisitor = new MyClassVisitor(cw);
                System.out.println("end new");
                classReader.accept(myClassVisitor, ClassReader.SKIP_DEBUG);
                byte[] bytes = cw.toByteArray();
                FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/chenyi/IdeaProjects/cheny/agent/src/MyMain.class"));
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                return bytes;
            }catch (Throwable e){
                e.printStackTrace();
            }

        }
        return classfileBuffer;
    }

}
