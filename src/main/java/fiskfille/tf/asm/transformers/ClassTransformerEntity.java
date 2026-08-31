package fiskfille.tf.asm.transformers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.asm.ASMHooksClient;
import fiskfille.tf.asm.TFTranslator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClassTransformerEntity extends ClassTransformerBase {
	public String varEntity;

	public ClassTransformerEntity() {
		super("net.minecraft.entity.Entity");
	}

	@Override
	public boolean processMethods(final List<MethodNode> methods) {
		boolean flag = false;

		for(final MethodNode method : methods) {
			if(method.name.equals(TFTranslator.getMappedName("c", "getBrightnessForRender")) && method.desc.equals("(F)I")) {
				final InsnList list = new InsnList();
				int startIndex = -1;
				int endIndex = -1;

				for(int i = 0; i < method.instructions.size(); ++i) {
					final AbstractInsnNode node = method.instructions.get(i);

					if(i + 9 < method.instructions.size()) {
						final AbstractInsnNode endNode = method.instructions.get(i + 9);

						if(endNode instanceof VarInsnNode && ((VarInsnNode) endNode).var == 6 && endNode.getOpcode() == Opcodes.ISTORE) {
							startIndex = i;
							endIndex = i + 9;
							list.add(new VarInsnNode(Opcodes.ALOAD, 0));
							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHooksClient.class), "getBrightnessForRender", "(L" + varEntity + ";)I", false));
						}
					}

					if(i >= startIndex && i < endIndex) {
						continue;
					}

					list.add(node);
				}

				method.instructions.clear();
				method.instructions.add(list);
				flag = true;
			}
		}

		return flag;
	}

	@Override
	public boolean processFields(final List<FieldNode> fields) {
		return true;
	}

	@Override
	public void setupMappings() {
		varEntity = TFTranslator.getMappedName("sa", "net/minecraft/entity/Entity");
	}
}
