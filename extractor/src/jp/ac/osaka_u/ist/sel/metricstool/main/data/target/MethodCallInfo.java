package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * ���\�b�h�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class MethodCallInfo extends CallInfo<MethodInfo> {

    /**
     * �Ăяo����郁�\�b�h��^���ăI�u�W�F�N�g��������
     *
     * @param qualifierType ���\�b�h�Ăяo���̐e�̌^
     * @param qualifierExpression ���\�b�h�Ăяo���̐e�G���e�B�e�B
     * @param callee �Ăяo����Ă��郁�\�b�h
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public MethodCallInfo(final TypeInfo qualifierType, final ExpressionInfo qualifierExpression,
            final MethodInfo callee, final TypeInfo type, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if ((null == qualifierType) || (null == callee) || (null == type)
                || (null == qualifierExpression)) {
            throw new NullPointerException();
        }

        this.qualifierType = qualifierType;
        this.qualifierExpression = qualifierExpression;
        this.type = type;
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return this.type;
        //        final MethodInfo callee = this.getCallee();
        //        final TypeInfo returnType = callee.getReturnType();
        //
        //        // ��`�̕Ԃ�l���^�p�����[�^�łȂ���΂��̂܂ܕԂ���
        //        if (!(returnType instanceof TypeParameterTypeInfo)) {
        //            return returnType;
        //        }
        //
        //        //�@�^�p�����[�^�̏ꍇ
        //        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getQualifierType();
        //        final List<TypeInfo> typeArguments = callOwnerType.getTypeArguments();
        //
        //        // �^����������ꍇ�́C���̌^��Ԃ�
        //        if (0 < typeArguments.size()) {
        //            final int typeParameterIndex = ((TypeParameterTypeInfo) returnType)
        //                    .getReferncedTypeParameter().getIndex();
        //            final TypeInfo typeArgument = typeArguments.get(typeParameterIndex);
        //            return typeArgument;
        //
        //            // �^�������Ȃ��ꍇ�́C����Ȍ^��Ԃ�
        //        } else {
        //
        //            // Java�@�̏ꍇ (�^�p�����[�^��1.5���瓱�����ꂽ)
        //            if (Settings.getInstance().getLanguage().equals(LANGUAGE.JAVA15)) {
        //                final ClassInfo referencedClass = DataManager.getInstance().getClassInfoManager()
        //                        .getClassInfo(new String[] { "java", "lang", "Object" });
        //                final TypeInfo classType = new ClassTypeInfo(referencedClass);
        //                return classType;
        //            }
        //        }
        //
        //        assert false : "Here shouldn't be reached!";
        //        return null;
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);
        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * ���̃��\�b�h�Ăяo�����������Ă���^��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo�����������Ă���^
     */
    public TypeInfo getQualifierType() {
        return this.qualifierType;
    }

    /**
     * ���̎��i���\�b�h�Ăяo���j�ɂ�����ϐ����p�̈ꗗ��Ԃ��N���X
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        variableUsages.addAll(quantifierExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();
        calls.add(this);
        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        calls.addAll(quantifierExpression.getCalls());
        return Collections.unmodifiableSet(calls);
    }

    /**
     * ���̃��\�b�h�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo ownerExpression = this.getQualifierExpression();
        sb.append(ownerExpression.getText());

        sb.append(".");

        final MethodInfo method = this.getCallee();
        sb.append(method.getMethodName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }
        if (0 < this.getArguments().size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        thrownExceptions.addAll(super.getThrownExceptions());
        thrownExceptions.addAll(this.getQualifierExpression().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExceptions);
    }

    @Override
    public ExecutableElementInfo copy() {

        final TypeInfo qualifierType = this.getQualifierType();
        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        final MethodInfo callee = this.getCallee();
        final TypeInfo type = this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final MethodCallInfo newCall = new MethodCallInfo(qualifierType, qualifierExpression,
                callee, type, ownerMethod, fromLine, fromColumn, toLine, toColumn);
        for (final ExpressionInfo argument : this.getArguments()) {
            newCall.addArgument((ExpressionInfo) argument.copy());
        }

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCall.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCall.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCall;
    }

    /**
     * ���̃��\�b�h�Ăяo���̐e�C�܂肱�̃��\�b�h�Ăяo�����������Ă���v�f��Ԃ�
     * 
     * @return ���̃��\�b�h�Ăяo���̐e
     */
    public final ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    private final TypeInfo qualifierType;

    private final ExpressionInfo qualifierExpression;

    private final TypeInfo type;
}
