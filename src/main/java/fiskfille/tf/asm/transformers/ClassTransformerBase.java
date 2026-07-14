package fiskfille.tf.asm.transformers;

import fiskfille.tf.TFLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public abstract class ClassTransformerBase implements IClassTransformer, Opcodes {
	protected final String classPath;
	protected final String unobfClass;

	public ClassTransformerBase(String classPath) {
		this.classPath = classPath;
		this.unobfClass = classPath.substring(classPath.lastIndexOf('.') + 1);
	}

	public static MethodNode generateSetter(String className, String methodName, String fieldName, String fieldType) {
		final MethodNode mn = new MethodNode(ACC_PUBLIC, methodName, "(" + fieldType + ")V", null, null);
		mn.visitCode();
		mn.visitVarInsn(ALOAD, 0);
		int opCode;

		if(fieldType.equals("I") || fieldType.equals("Z")) {
			opCode = ILOAD;
		}
		else if(fieldType.equals("L")) {
			opCode = LLOAD;
		}
		else if(fieldType.equals("F")) {
			opCode = FLOAD;
		}
		else if(fieldType.equals("D")) {
			opCode = DLOAD;
		}
		else {
			opCode = ALOAD;
		}

		mn.visitVarInsn(opCode, 1);
		mn.visitFieldInsn(PUTFIELD, className, fieldName, fieldType);
		mn.visitInsn(RETURN);
		mn.visitMaxs(2, 2);
		mn.visitEnd();
		return mn;
	}

	public static MethodNode generateGetter(String className, String methodName, String fieldName, String fieldType) {
		final MethodNode mn = new MethodNode(ACC_PUBLIC, methodName, "()" + fieldType, null, null);
		mn.visitCode();
		mn.visitVarInsn(ALOAD, 0);
		mn.visitFieldInsn(GETFIELD, className, fieldName, fieldType);
		int opCode;

		if(fieldType.equals("I") || fieldType.equals("Z")) {
			opCode = IRETURN;
		}
		else if(fieldType.equals("L")) {
			opCode = LRETURN;
		}
		else if(fieldType.equals("F")) {
			opCode = FRETURN;
		}
		else if(fieldType.equals("D")) {
			opCode = DRETURN;
		}
		else {
			opCode = ARETURN;
		}

		mn.visitInsn(opCode);
		mn.visitMaxs(1, 1);
		mn.visitEnd();
		return mn;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		try {
			if(transformedName.equals(classPath)) {
				TFLog.info("Patching class %s (%s)...", unobfClass, name);

				final ClassReader cr = new ClassReader(bytes);
				final ClassNode cn = new ClassNode();
				cr.accept(cn, 0);

				setupMappings();
				boolean success = processFields(cn.fields) && processMethods(cn.methods);
				addInterface(cn.interfaces);

				final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				cn.accept(cw);

				if(success) {
					TFLog.info("Patching class %s done.", unobfClass);
				}
				else {
					TFLog.error("Patching class %s failed!", unobfClass);
				}

				return cw.toByteArray();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return bytes;
	}

	public void addInterface(List<String> interfaces) {

	}

	public abstract boolean processMethods(List<MethodNode> methods);

	public abstract boolean processFields(List<FieldNode> fields);

	public abstract void setupMappings();

	public void sendPatchLog(String method) {
		TFLog.info("\tPatching method %s in %s", method, unobfClass);
	}
}
