package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �z��R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayConstructorCallInfo extends ConstructorCallInfo<ArrayTypeInfo> {

    /**
     * �^��^���Ĕz��R���X�g���N�^�Ăяo����������
     * 
     * @param arrayType �Ăяo���̌^
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.indexExpressions = new TreeMap<Integer, ExpressionInfo>();
    }

    public void addIndexExpression(final int dimension, final ExpressionInfo indexExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == indexExpression) {
            throw new IllegalArgumentException();
        }

        this.indexExpressions.put(dimension, indexExpression);
        indexExpression.setOwnerExecutableElement(this);
    }

    /**
     * �C���f�b�N�X�̎����擾
     * @param dimention �C���f�b�N�X�̎����擾����z��̎���
     * @return �w�肵�������̃C���f�b�N�X�̎�
     */
    public ExpressionInfo getIndexExpression(final int dimention) {
        return this.indexExpressions.get(dimention);
    }

    /**
     * �C���f�b�N�X�̎��̃��X�g���擾
     * 
     * @return �C���f�b�N�X�̎��̃��X�g 
     */
    public SortedMap<Integer, ExpressionInfo> getIndexExpressions() {
        return Collections.unmodifiableSortedMap(this.indexExpressions);
    }

    /**
     * �z��̏��������̃e�L�X�g�\����Ԃ�
     * 
     * @return �z��̏��������̃e�L�X�g�\��
     * 
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        text.append("new ");

        final ArrayTypeInfo arrayType = this.getType();
        final TypeInfo elementType = arrayType.getElementType();
        text.append(elementType.getTypeName());

        for (final ExpressionInfo indexExpression : this.getIndexExpressions().values()) {
            text.append("[");
            text.append(indexExpression.getText());
            text.append("]");
        }

        final List<ExpressionInfo> arguments = this.getArguments();
        if (0 < arguments.size()) {
            text.append("{");
            for (final ExpressionInfo argument : arguments) {
                text.append(argument.getText());
                text.append(",");
            }
            text.deleteCharAt(text.length() - 1);
            text.append("}");
        }
        return text.toString();
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
        for (final ExpressionInfo indexExpression : this.getIndexExpressions().values()) {
            thrownExceptions.addAll(indexExpression.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    /**
     * ���̌Ăяo���ɂ�����ϐ��g�p�Q��Ԃ�
     * 
     * @return ���̌Ăяo���ɂ�����ϐ��g�p�Q
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());
        for (final ExpressionInfo indexExpression : this.getIndexExpressions().values()) {
            variableUsages.addAll(indexExpression.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    @Override
    public ExecutableElementInfo copy() {

        final ArrayTypeInfo arrayType = this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayConstructorCallInfo newCall = new ArrayConstructorCallInfo(arrayType,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        for (final ExpressionInfo argument : this.getArguments()) {
            newCall.addArgument((ExpressionInfo) argument.copy());
        }

        for (final Entry<Integer, ExpressionInfo> entry : this.getIndexExpressions().entrySet()) {
            final Integer dimension = entry.getKey();
            final ExpressionInfo indexExpression = (ExpressionInfo) entry.getValue().copy();
            newCall.addIndexExpression(dimension, indexExpression);
        }

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCall.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCall.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCall;
    }

    private final SortedMap<Integer, ExpressionInfo> indexExpressions;
}
