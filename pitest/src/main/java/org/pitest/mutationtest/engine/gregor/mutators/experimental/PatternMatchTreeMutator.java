package org.pitest.mutationtest.engine.gregor.mutators.experimental;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;

import org.pitest.bytecode.ASMVersion;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;


public enum PatternMatchTreeMutator implements MethodMutatorFactory {
    PATTERN_MATCH_MUTATOR;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new PatternMatchTreeMethodVisitor(this, context, methodVisitor);
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


class PatternMatchTreeMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext      context;
    private final MethodVisitor visitor;

    private static int SEEN_INVOKE_VIRTUAL = 0;
    private static int SEEN_LDC = 0;
    private static int SEEN_EQUALS = 0;


    PatternMatchTreeMethodVisitor(final MethodMutatorFactory factory,
                              final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(ASMVersion.ASM_VERSION, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
        this.visitor = delegateMethodVisitor;
    }

//    @Override
//    public void visitEnd() {
//        MethodNode methodNode = (MethodNode) this.visitor;
//        Analyzer<BasicValue> analyzer = (Analyzer<BasicValue>) new BasicInterpreter();
//        try {
//            analyzer.analyze(owner, methodNode);
//        }
//    }
}