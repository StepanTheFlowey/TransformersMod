package fiskfille.tf.asm.transformers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ClassTransformerBase implements IClassTransformer {
	protected final String classPath;
	protected final String unobfClass;

	public ClassTransformerBase(final String classPath) {
		this.classPath = classPath;
		this.unobfClass = classPath.substring(classPath.lastIndexOf('.') + 1);
	}

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		try {
			if(transformedName.equals(classPath)) {
				TransformersMod.log.info("Patching class {} ({})...", unobfClass, name);

				final ClassReader classReader = new ClassReader(bytes);
				final ClassNode classNode = new ClassNode();
				classReader.accept(classNode, 0);

				setupMappings();
				if(processFields(classNode.fields) && processMethods(classNode.methods)) {
					TransformersMod.log.info("Patching class {} done.", unobfClass);
				}
				else {
					TransformersMod.log.error("Patching class {} failed!", unobfClass);
				}

				final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				return classWriter.toByteArray();
			}
		}
		catch(final Exception e) {
			e.printStackTrace();
		}

		return bytes;
	}

	public abstract boolean processMethods(List<MethodNode> methods);

	public abstract boolean processFields(List<FieldNode> fields);

	public abstract void setupMappings();
}
