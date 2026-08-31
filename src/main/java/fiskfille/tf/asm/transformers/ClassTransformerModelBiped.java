package fiskfille.tf.asm.transformers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.asm.TFTranslator;
import fiskfille.tf.helper.TFModelHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClassTransformerModelBiped extends ClassTransformerBase {
	public ClassTransformerModelBiped() {
		super("net.minecraft.client.model.ModelBiped");
	}

	@Override
	public boolean processMethods(final List<MethodNode> methods) {
		boolean flag = false;

		for(final MethodNode method : methods) {
			if(method.name.equals(TFTranslator.getMappedName("a", "render")) && method.desc.equals(TFTranslator.getMappedName("(Lsa;FFFFFF)V", "(Lnet/minecraft/entity/Entity;FFFFFF)V"))) {
				final InsnList list = new InsnList();

				for(int i = 0; i < method.instructions.size(); ++i) {
					final AbstractInsnNode node = method.instructions.get(i);

					if(node instanceof MethodInsnNode) {
						final MethodInsnNode methodNode = (MethodInsnNode) node;

						if(methodNode.name.equals(TFTranslator.getMappedName("a", "setRotationAngles")) && methodNode.desc.equals(TFTranslator.getMappedName("(FFFFFFLsa;)V", "(FFFFFFLnet/minecraft/entity/Entity;)V"))) {
							list.add(node);
							list.add(new VarInsnNode(Opcodes.ALOAD, 0));
							list.add(new VarInsnNode(Opcodes.ALOAD, 1));
							list.add(new VarInsnNode(Opcodes.FLOAD, 2));
							list.add(new VarInsnNode(Opcodes.FLOAD, 3));
							list.add(new VarInsnNode(Opcodes.FLOAD, 4));
							list.add(new VarInsnNode(Opcodes.FLOAD, 5));
							list.add(new VarInsnNode(Opcodes.FLOAD, 6));
							list.add(new VarInsnNode(Opcodes.FLOAD, 7));
							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TFModelHelper.class), "renderBipedPre", TFTranslator.getMappedName("(Lbhm;Lsa;FFFFFF)V", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V"), false));
							continue;
						}
					}

					list.add(node);
				}

				method.instructions.clear();
				method.instructions.add(list);
				flag = true;
			}
			//          else if (method.name.equals(TFTranslator.getMappedName("a", "setRotationAngles")) && method.desc.equals(TFTranslator.getMappedName("(FFFFFFLsa;)V", "(FFFFFFLnet/minecraft/entity/Entity;)V")))
			//          {
			//              InsnList list = new InsnList();
			//
			//              for (int i = 0; i < method.instructions.size(); ++i)
			//              {
			//                  AbstractInsnNode node = method.instructions.get(i);
			//
			//                  if (node.getOpcode() == RETURN)
			//                  {
			//                      list.add(new VarInsnNode(ALOAD, 0));
			//                      list.add(new VarInsnNode(FLOAD, 1));
			//                      list.add(new VarInsnNode(FLOAD, 2));
			//                      list.add(new VarInsnNode(FLOAD, 3));
			//                      list.add(new VarInsnNode(FLOAD, 4));
			//                      list.add(new VarInsnNode(FLOAD, 5));
			//                      list.add(new VarInsnNode(FLOAD, 6));
			//                      list.add(new VarInsnNode(ALOAD, 7));
			//                      list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ModelHelper.class), "setRotationAngles", ALTranslator.getMappedName("(Lbhm;FFFFFFFLsa;)V", "(Lnet/minecraft/client/model/ModelBiped;FFFFFFLnet/minecraft/entity/Entity;)V"), false));
			//                  }
			//
			//                  list.add(node);
			//              }
			//
			//              method.instructions.clear();
			//              method.instructions.add(list);
			//              flag = true;
			//          }
		}

		return flag;
	}

	@Override
	public boolean processFields(final List<FieldNode> fields) {
		return true;
	}

	@Override
	public void setupMappings() {}
}
