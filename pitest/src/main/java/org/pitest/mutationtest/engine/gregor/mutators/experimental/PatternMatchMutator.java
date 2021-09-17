package org.pitest.mutationtest.engine.gregor.mutators.experimental;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.bytecode.ASMVersion;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;


public enum PatternMatchMutator implements MethodMutatorFactory {
    PATTERN_MATCH_MUTATOR;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new PatternMatchMethodVisitor(this, context, methodVisitor);
    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName();
    }

    @Override
    public String getName() {
        return name();
    }
}


class PatternMatchMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext      context;

    private static int SEEN_INVOKE_VIRTUAL = 0;
    private static int SEEN_LDC = 0;
    private static int SEEN_EQUALS = 0;


    PatternMatchMethodVisitor(final MethodMutatorFactory factory,
                                  final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(ASMVersion.ASM_VERSION, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (opcode == Opcodes.INVOKEVIRTUAL){
//            System.out.println("Name: " + name);
//            System.out.println("Owner: " + owner);
        }

        if(opcode == Opcodes.INVOKEVIRTUAL && owner.contains("DealLog")) {
//            System.out.println("------------START------------");
//            System.out.println("INVOKE DealLog Methods CALLED");
            if(SEEN_INVOKE_VIRTUAL == 0){
                SEEN_INVOKE_VIRTUAL = 1;
            }
            else{
                SEEN_INVOKE_VIRTUAL = 0;
                SEEN_LDC = 0;
                SEEN_EQUALS = 0;
            }
        }
        else if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("java/lang/String") && name.equals("equals")){
//            System.out.println("------------START------------");
//            System.out.println("Java String Equal Methods CALLED");
            if(SEEN_INVOKE_VIRTUAL == 1 && SEEN_LDC == 1){
                SEEN_EQUALS = 1;
            }
            else{
                SEEN_INVOKE_VIRTUAL = 0;
                SEEN_LDC = 0;
                SEEN_EQUALS = 0;
            }
        }
        else{
//            System.out.println("------------START------------");
//            System.out.println("STATE RESET AT: INVOKE VIRTUAL");
            SEEN_INVOKE_VIRTUAL = 0;
            SEEN_LDC = 0;
            SEEN_EQUALS = 0;
        }
//        logInternalState();
//        System.out.println("-------------END-------------");
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitLdcInsn(Object value){
        if (SEEN_INVOKE_VIRTUAL == 1){
//            System.out.println("------------START------------");
//            System.out.println("SEEN LDC AFTER INVOKE VIRTUAL");
            SEEN_LDC = 1;
        }
        else{
//            System.out.println("------------START------------");
//            System.out.println("STATE RESET AT: LDC");
            SEEN_INVOKE_VIRTUAL = 0;
            SEEN_LDC = 0;
            SEEN_EQUALS = 0;
        }
//        logInternalState();
//        System.out.println("-------------END-------------");
        super.visitLdcInsn(value);
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {

        if (canMutate(opcode)
                && SEEN_INVOKE_VIRTUAL == 1
        ) {
            final MutationIdentifier newId = this.context.registerMutation(this.factory, "CSTP Pattern-Match Mutator");

            // should mutate?
//            System.out.println("!!!!!!!!!!!!START!!!!!!!!!!!!");
//            System.out.println("MUTATED AT: JUMP INSN");
            super.visitInsn(Opcodes.POP);
//            this.mv.visitJumpInsn(opcode, label);
        } else {
//            System.out.println("!!!!!!!!!!!!START!!!!!!!!!!!!");
//            System.out.println("CAN MUTATE YIELDS FALSE");
            this.mv.visitJumpInsn(opcode, label);
        }

        SEEN_INVOKE_VIRTUAL = 0;
        SEEN_LDC = 0;
        SEEN_EQUALS = 0;
//        System.out.println("!!!!!!!!!!!!END!!!!!!!!!!!!");
    }

    private boolean canMutate(final int opcode) {
        return opcode == Opcodes.IFEQ;
    }

    private void logInternalState(){
        System.out.println("SEEN_INVOKE_VIRTUAL = " + SEEN_INVOKE_VIRTUAL);
        System.out.println("SEEN_LDC = " + SEEN_LDC);
        System.out.println("SEEN_EQUALS = " + SEEN_EQUALS);
    }
}