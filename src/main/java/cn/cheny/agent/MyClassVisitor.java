package cn.cheny.agent;


import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author by chenyi
 * @date 2021/7/5
 */
public class MyClassVisitor extends ClassVisitor {

    public MyClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals("getVal")) {
            return new MyMethodVisitor(mv, access, name, descriptor);
        }
        return mv;
    }

    public static class MyMethodVisitor extends AdviceAdapter {

        private Label from = new Label();
        private Label to = new Label();
        private Label target = new Label();

        protected MyMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM7, mv, access, name, desc);
        }

        @Override
        public void onMethodEnter() {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("test");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            // 定义try开始位置
            mv.visitLabel(from);
            // 定义try...catch块
            mv.visitTryCatchBlock(from, to, target, "java/lang/Throwable");
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            // 定义try结束位置
            mv.visitLabel(to);

            // 定义catch位置
            mv.visitLabel(target);
            // 新增本地变量exception
            int local = newLocal(Type.getType(Throwable.class));
            // store exception
            mv.visitVarInsn(Opcodes.ASTORE, local);
            // load exception
            mv.visitVarInsn(Opcodes.ALOAD, local);
            // 执行throwable#printStackTrace
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
//            mv.visitInsn(ATHROW);
            // 常量10压栈
            mv.visitIntInsn(BIPUSH,10);
            // return 10出栈
            mv.visitInsn(IRETURN);
            super.visitMaxs(maxStack, maxLocals);
        }
    }
}
