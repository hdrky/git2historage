package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���\�b�h�Ăяo���C�R���X�g���N�^�Ăяo���̋��ʂ̐e�N���X
 * 
 * @param <T> �Ăяo����郆�j�b�g�̌^
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class CallInfo<T extends CallableUnitInfo> extends ExpressionInfo {

    /**
     * �Ăяo����Collection����Ăяo����Ă��郆�j�b�g��Set��Ԃ�
     * 
     * @param calls �Ăяo����Collection
     * @return �Ăяo����Ă��郆�j�b�g��Set
     */
    public static Set<CallableUnitInfo> getCallees(
            Collection<CallInfo<? extends CallableUnitInfo>> calls) {

        final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
        for (final CallInfo<?> call : calls) {
            final CallableUnitInfo callee = call.getCallee();
            callees.add(callee);
        }
        return Collections.unmodifiableSet(callees);
    }

    /**
     * @param callee �Ă΂�Ă���I�u�W�F�N�g�C���̌Ăяo�����C�z��̃R���X�g���N�^�̏ꍇ��null�������Ă���D
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    CallInfo(final T callee, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.arguments = new LinkedList<ExpressionInfo>();
        this.typeArguments = new LinkedList<ReferenceTypeInfo>();

        this.callee = callee;

        // ���\�b�h�Ăяo���֌W���\�z
        if (null != callee) {
            this.callee.addCaller(ownerMethod);
        }
    }

    /**
     * ���̃��\�b�h�Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param argument �ǉ����������
     */
    public final void addArgument(final ExpressionInfo argument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == argument) {
            throw new NullPointerException();
        }

        this.arguments.add(argument);
        argument.setOwnerExecutableElement(this);
    }

    /**
     * ���̌Ăяo���̎�������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param arguments �ǉ����������
     */
    public final void addArguments(final List<ExpressionInfo> arguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arguments) {
            throw new NullPointerException();
        }

        this.arguments.addAll(arguments);

        for (final ExpressionInfo argument : arguments) {
            argument.setOwnerExecutableElement(this);
        }
    }

    /**
     * ���̃��\�b�h�Ăяo���̌^������ǉ��D�v���O�C������͌Ăяo���Ȃ�
     * 
     * @param typeArgument �ǉ�����^����
     */
    public final void addTypeArgument(final ReferenceTypeInfo typeArgument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * ���̌Ăяo���̌^������ǉ��D�v���O�C������͌Ăяo���Ȃ��D
     * 
     * @param typeArguments �ǉ�����^����
     */
    public final void addTypeArguments(final List<ReferenceTypeInfo> typeArguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArguments) {
            throw new NullPointerException();
        }

        this.typeArguments.addAll(typeArguments);
    }

    /**
     * ���̌Ăяo���̎�������List��Ԃ��D
     * 
     * @return�@���̌Ăяo���̎�������List
     */
    public List<ExpressionInfo> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * ���̌Ăяo���̌^������List��Ԃ��D
     * 
     * @return ���̌Ăяo���̌^������List
     */
    public List<ReferenceTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * ���̌Ăяo���ɂ�����ϐ��g�p�Q��Ԃ�
     * 
     * @return ���̌Ăяo���ɂ�����ϐ��g�p�Q
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        for (final ExpressionInfo parameter : this.getArguments()) {
            variableUsages.addAll(parameter.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * ���̌Ăяo���œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        for (final ExpressionInfo parameter : this.getArguments()) {
            thrownExceptions.addAll(parameter.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    /**
     * ���̌Ăяo���ŌĂяo����Ă�����̂�Ԃ�
     * 
     * @return ���̌Ăяo���ŌĂяo����Ă������
     */
    public final T getCallee() {
        return this.callee;
    }

    private final T callee;

    private final List<ExpressionInfo> arguments;

    private final List<ReferenceTypeInfo> typeArguments;

    /**
     * ��̌Ăяo����Set��\��
     */
    public static final SortedSet<CallInfo<? extends CallableUnitInfo>> EmptySet = Collections
            .unmodifiableSortedSet(new TreeSet<CallInfo<? extends CallableUnitInfo>>());
}
