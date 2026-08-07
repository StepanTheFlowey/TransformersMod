package fiskfille.tf.asm.transformers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.asm.TFTranslator;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ClassTransformerMethodProcess extends ClassTransformerBase {
	private final String methodName;
	private final String methodNameDev;
	private final String methodDesc;
	private final String methodDescDev;
	private String methName;
	private String methDesc;

	public ClassTransformerMethodProcess(final String classPath, final String methodName, final String methodNameDev, final String methodDesc, final String methodDescDev) {
		super(classPath);
		this.methodName = methodName;
		this.methodNameDev = methodNameDev;
		this.methodDesc = methodDesc;
		this.methodDescDev = methodDescDev;
	}

	@Override
	public boolean processMethods(final List<MethodNode> methods) {
		for(final MethodNode method : methods) {
			if(method.name.equals(methName) && method.desc.equals(methDesc)) {
				processMethod(method);
				return true;
			}
		}

		return false;
	}

	public abstract void processMethod(MethodNode method);

	@Override
	public boolean processFields(final List<FieldNode> fields) {
		return true;
	}

	@Override
	public void setupMappings() {
		methName = TFTranslator.getMappedName(methodName, methodNameDev);
		methDesc = TFTranslator.getMappedName(methodDesc, methodDescDev);
	}
}
