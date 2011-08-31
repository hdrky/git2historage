package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �O�����Z�g�p��\���N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class TernaryOperationInfo extends ExpressionInfo {

    /**
     * �O�����Z�̏�����(��ꍀ)�C��������true�̎��ɕԂ���鎮�C��������false�̎��ɕԂ���鎮(��O��)�C�J�n�ʒu�C�I���ʒu��^���ď�����
     * 
     * @param condtion ������(��ꍀ)
     * @param trueExpression ��������true�̂Ƃ��ɕԂ���鎮(���)
     * @param falseExpression ��������false�̂Ƃ��ɕԂ���鎮(��O��)
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TernaryOperationInfo(final ConditionInfo condtion, ExpressionInfo trueExpression,
            ExpressionInfo falseExpression, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == condtion || null == trueExpression || null == falseExpression) {
            throw new IllegalArgumentException();
        }

        this.condition = condtion;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;

        if (this.condition instanceof ExpressionInfo) {
            ((ExpressionInfo) this.condition).setOwnerExecutableElement(this);
        }
        this.trueExpression.setOwnerExecutableElement(this);
        this.falseExpression.setOwnerExecutableElement(this);
    }

    @Override
    public TypeInfo getType() {
        return this.trueExpression.getType();
    }

    /**
     * �O�����Z�̏�����(��ꍀ)��Ԃ�
     * @return �O�����Z�̏�����(��ꍀ)
     */
    public ConditionInfo getCondition() {
        return this.condition;
    }

    /**
     * �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)��Ԃ�
     * @return �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)
     */
    public ExpressionInfo getTrueExpression() {
        return this.trueExpression;
    }

    /**
     * �O�����Z�̏�������false�Ƃ��ɕԂ���鎮(��O��)��Ԃ�
     * @return �O�����Z�̏�������false�̂Ƃ��ɕԂ���鎮(��O��)
     */
    public ExpressionInfo getFalseExpression() {
        return this.falseExpression;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<?>> variableUsages = new HashSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getCondition().getVariableUsages());
        variableUsages.addAll(this.getTrueExpression().getVariableUsages());
        variableUsages.addAll(this.getFalseExpression().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        calls.addAll(this.getCondition().getCalls());
        calls.addAll(this.getTrueExpression().getCalls());
        calls.addAll(this.getFalseExpression().getCalls());
        return Collections.unmodifiableSet(calls);
    }

    /**
     * ���̎O�����Z�̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̎O�����Z�̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ConditionInfo condition = this.getCondition();
        sb.append(condition.getText());

        sb.append(" ? ");

        final ExpressionInfo trueExpression = this.getTrueExpression();
        sb.append(trueExpression.getText());

        sb.append(" : ");

        final ExpressionInfo falseExpression = this.getFalseExpression();
        sb.append(falseExpression.getText());

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
        thrownExceptions.addAll(this.getCondition().getThrownExceptions());
        thrownExceptions.addAll(this.getTrueExpression().getThrownExceptions());
        thrownExceptions.addAll(this.getFalseExpression().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExceptions);
    }

    @Override
    public ExecutableElementInfo copy() {
        final ConditionInfo condition = (ConditionInfo) this.getCondition().copy();
        final ExpressionInfo trueExpression = (ExpressionInfo) this.getTrueExpression().copy();
        final ExpressionInfo falseExpression = (ExpressionInfo) this.getFalseExpression().copy();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final TernaryOperationInfo newTernaryOperation = new TernaryOperationInfo(condition,
                trueExpression, falseExpression, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newTernaryOperation.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newTernaryOperation.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newTernaryOperation;
    }

    /**
     * �O�����Z�̏�����(��ꍀ)��ۑ�����ϐ�
     */
    private final ConditionInfo condition;

    /**
     * �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)��ۑ�����ϐ�
     */
    private final ExpressionInfo trueExpression;

    /**
     * �O�����Z�̏�������false�̂Ƃ��ɕԂ���鎮(��O��)��ۑ�����ϐ�
     */
    private final ExpressionInfo falseExpression;
}
