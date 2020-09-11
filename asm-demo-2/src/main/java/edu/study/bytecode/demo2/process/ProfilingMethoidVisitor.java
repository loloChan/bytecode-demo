package edu.study.bytecode.demo2.process;

import edu.study.bytecode.demo2.aspect.MethodTag;
import edu.study.bytecode.demo2.aspect.ProfilingAspect;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.List;

@SuppressWarnings("all")
public class ProfilingMethoidVisitor extends AdviceAdapter {

    /**
     * 函数id，与methodTag绑定
     */
    private int methodId;

    /**
     * 函数标签
     */
    private MethodTag methodTag;

    /**
     * 存放函数开始时间的变量下标
     */
    private int startTimeIndex;

    /**
     * 入参数组下标
     */
    private int paramArryIndex;

    protected ProfilingMethoidVisitor(MethodVisitor methodVisitor, int access, String name,
                                      String descriptor, String className) {
        super(ASM5, methodVisitor, access, name, descriptor);
        //入参类型
        List<String> requestParams = ProfilingAspect.compileMethodRequestParams(descriptor);

        String fullClassName = className.replaceAll("/", ".");
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

        String returnType = descriptor.substring(descriptor.lastIndexOf(")") + 1);

        methodId = ProfilingAspect.generateMethodId(new MethodTag(access,fullClassName,simpleClassName,
                name,descriptor,requestParams,returnType));
    }

    @Override
    protected void onMethodEnter() {
        //获取方法标签
        MethodTag methodTag = ProfilingAspect.getMethodTagById(methodId);
        int localVar = 1;
        int cursor = 1;
        //判断是否为静态方法，静态方法局部变量表不包含this
        int access = this.getAccess();
        /*
            方法静态修复符判断存在缺陷，待修复。
        */
        if ((ACC_STATIC & access) == 0 ) {
            //非静态方法，局部变量表包含this
            localVar++;
            cursor++;
        }
        List<String> parameterTypeList = methodTag.getParameterTypeList();
        //局部变量表数量
        localVar += parameterTypeList.size();
        //存放startTime的索引
        startTimeIndex = localVar;
        //调用System.nanoTime()
        mv.visitMethodInsn(INVOKESTATIC,
                "java/lang/System",
                "nanoTime",
                "()J", false);
        mv.visitVarInsn(LSTORE, startTimeIndex);
        localVar += 2;

        //新建Object[]数据
        int paramLength = parameterTypeList.size();
        if (paramLength > 5) {
            mv.visitVarInsn(BIPUSH,paramLength);
        } else {
            switch (paramLength) {
                case 5:
                    mv.visitInsn(ICONST_5);
                    break;
                case 4:
                    mv.visitInsn(ICONST_4);
                    break;
                case 3:
                    mv.visitInsn(ICONST_3);
                    break;
                case 2:
                    mv.visitInsn(ICONST_2);
                    break;
                case 1:
                    mv.visitInsn(ICONST_1);
                    break;
                default:
                    mv.visitInsn(ICONST_0);
            }
        }
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        paramArryIndex = localVar;
        //把数组变量存到局部变量表paramIndex中
        mv.visitVarInsn(ASTORE,paramArryIndex);
        localVar++;

        //将入参存到数组中
        for (int i = 0; i < parameterTypeList.size(); i++) {
            //基础类型需要转换为对应的封装类型
            mv.visitVarInsn(ALOAD,paramArryIndex);
            //数组下标
            if (i <= 5){
                switch (i) {
                    case 0:
                        mv.visitInsn(ICONST_0);
                        break;
                    case 1:
                        mv.visitInsn(ICONST_1);
                        break;
                    case 2:
                        mv.visitInsn(ICONST_2);
                        break;
                    case 3:
                        mv.visitInsn(ICONST_3);
                        break;
                    case 4:
                        mv.visitInsn(ICONST_4);
                        break;
                    case 5:
                        mv.visitInsn(ICONST_5);
                        break;
                }
            } else {
                mv.visitVarInsn(BIPUSH, i);
            }
            //
            String type = parameterTypeList.get(i);
            if ("Z".equals(type)) {
                //加载变量
                mv.visitVarInsn(ILOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Boolean",
                        "valueOf",
                        "(Z)Ljava/lang/Boolean;",false);
                cursor++;
            } else if ("C".equals(type)) {
                mv.visitVarInsn(ILOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Character",
                        "valueOf",
                        "(C)Ljava/lang/Character;",false);
                cursor++;
            } else if ("B".equals(type)) {
                mv.visitVarInsn(ILOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Byte",
                        "valueOf",
                        "(B)Ljava/lang/Byte;",false);
                cursor++;
            } else if ("S".equals(type)) {
                mv.visitVarInsn(ILOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Short",
                        "valueOf",
                        "(S)Ljava/lang/Short;",false);
                cursor++;
            } else if ("I".equals(type)) {
                mv.visitVarInsn(ILOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Integer",
                        "valueOf",
                        "(I)Ljava/lang/Integer;",false);
                cursor++;
            } else if ("F".equals(type)) {
                mv.visitVarInsn(FLOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Float",
                        "valueOf",
                        "(F)Ljava/lang/Float;",false);
                cursor++;
            } else if ("J".equals(type)) {
                mv.visitVarInsn(LLOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Long",
                        "valueOf",
                        "(J)Ljava/lang/Long;",false);
                cursor += 2;
            } else if ("D".equals(type)) {
                mv.visitVarInsn(DLOAD, cursor);
                mv.visitMethodInsn(INVOKESTATIC,
                        "java/lang/Double",
                        "valueOf",
                        "(D)Ljava/lang/Double;",false);
                cursor += 2;
            } else {
                mv.visitVarInsn(ALOAD, cursor);
                cursor++;
            }
            mv.visitInsn(AASTORE);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        /*if (opcode == RETURN) {
            mv.visitInsn(ACONST_NULL);
        } else {
            mv.visitInsn(DUP);
        }*/
        mv.visitVarInsn(LLOAD, startTimeIndex);
        mv.visitVarInsn(BIPUSH, methodId-3);
        mv.visitVarInsn(ALOAD,paramArryIndex);
        mv.visitInsn(ACONST_NULL);
        mv.visitMethodInsn(INVOKESTATIC,
                "edu/study/bytecode/demo2/aspect/ProfilingAspect",
                "printInfo",
                "(JI[Ljava/lang/Object;Ljava/lang/Object;)V",false);
    }







    /*@Override
    protected void onMethodEnter() {
        //获取startTime
        mv.visitMethodInsn(INVOKESTATIC,
                "java/lang/System",
                "nanoTime",
                "()J",
                false);
        //保存startTime,
        mv.visitVarInsn(LSTORE, 3);
        //创建Object数组
        mv.visitInsn(ICONST_2);
        mv.visitTypeInsn(ANEWARRAY,"java/lang/Object");
        //保存数组到临时变量表5的位置，因为前面3放了long类型，long是要占两个slot的
        mv.visitVarInsn(ASTORE, 5);

        //将userId放到Object数组中
        mv.visitVarInsn(ALOAD,5);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ILOAD,1);
        mv.visitMethodInsn(INVOKESTATIC,
                "java/lang/Integer",
                "valueOf",
                "(I)Ljava/lang/Integer;",false);
        mv.visitInsn(AASTORE);

        //将age放到Object数组中
        mv.visitVarInsn(ALOAD, 5);
        mv.visitInsn(ICONST_1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKESTATIC,
                "java/lang/Integer",
                "valueOf",
                "(I)Ljava/lang/Integer;",false);
        mv.visitInsn(AASTORE);

        //更改局部变量表位置
        *//*mv.visitVarInsn(LLOAD, 3);
        mv.visitVarInsn(LASTORE, 1);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitVarInsn(ASTORE, 2);*//*

        //创建StringBuilder
        mv.visitTypeInsn(NEW,"java/lang/StringBuilder");
        mv.visitInsn(DUP);
        //执行构造函数
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/lang/StringBuilder",
                "<init>",
                "()V",
                false);
        //将sb对象存放于变量表6中
        mv.visitVarInsn(ASTORE, 6);

        mv.visitVarInsn(ALOAD, 6);
        mv.visitLdcInsn("queryUserInfo2 执行时间：");
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                false);
        mv.visitVarInsn(ALOAD,6);

        //将数组转换为字符串
        mv.visitVarInsn(ALOAD, 5);
        mv.visitMethodInsn(INVOKESTATIC,
                "java/util/Arrays",
                "toString",
                "([Ljava/lang/Object;)Ljava/lang/String;",false);
        //将结果存放于变量表6中
        mv.visitVarInsn(ASTORE, 7);

        //计算执行时间
        mv.visitVarInsn(ALOAD, 6);
        mv.visitMethodInsn(INVOKESTATIC,
                "java/lang/System",
                "nanoTime",
                "()J",
                false);
        mv.visitVarInsn(LLOAD, 3);
        mv.visitInsn(LSUB);

        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(J)Ljava/lang/StringBuilder;",false);

        mv.visitLdcInsn(" ns 参数为：");
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",false);

        mv.visitVarInsn(ALOAD, 7);
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",false);

        //调用sb.toString
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "toString",
                "()Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE,8);
        //获取System.out
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");
        mv.visitVarInsn(ALOAD,8);
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",false);
    }*/
}
