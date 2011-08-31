package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * �ꍀ���Z��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class MonominalOperationInfo extends ExpressionInfo {

    /**
     * �ꍀ���Z�̃I�y�����h�A�ʒu����^���ď�����
     * 
     * @param operand �I�y�����h
     * @param operator �I�y���[�^�[
     * @param isPreposed ���Z�q�̈ʒu
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public MonominalOperationInfo(final ExpressionInfo operand, final OPERATOR operator,
            final boolean isPreposed, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == operand || null == operator) {
            throw new IllegalArgumentException();
        }

        this.operand = operand;
        this.operator = operator;
        this.isPreposed = isPreposed;
        this.type = null != this.operator.getSpecifiedResultType() ? this.operator
                .getSpecifiedResultType() : this.operand.getType();

        this.operand.setOwnerExecutableElement(this);
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * �I�y�����h��Ԃ�
     * @return �I�y�����h
     */
    public final ExpressionInfo getOperand() {
        return this.operand;
    }

    /**
     * �ꍀ���Z�̉��Z�q��Ԃ��D
     * @return ���Z�q
     */
    public final OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * ���Z�q���O�u����Ă��邩�ǂ����Ԃ�
     * @return ���Z�q���O�u����Ă���Ȃ�true
     */
    public final boolean isPreposed() {
        return this.isPreposed;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getOperand().getVariableUsages();
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getOperand().getCalls();
    }

    /**
     * ���̒P�����Z�̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̒P�����Z�̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo operand = this.getOperand();
        final OPERATOR operator = this.getOperator();

        if (this.isPreposed()) {

            sb.append(operator.getToken());
            sb.append(operand.getText());

        } else {

            sb.append(operand.getText());
            sb.append(operator.getToken());
        }

        return sb.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getOperand().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo operand = (ExpressionInfo) this.getOperand().copy();
        final OPERATOR operator = this.getOperator();
        final boolean isPreposed = this.isPreposed();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final MonominalOperationInfo newMonominalOperation = new MonominalOperationInfo(operand,
                operator, isPreposed, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newMonominalOperation.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newMonominalOperation.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newMonominalOperation;
    }

    /**
     * �I�y�����h��ۑ����邽�߂̕ϐ�
     */
    private final ExpressionInfo operand;

    /**
     * �ꍀ���Z�̉��Z�q��ۑ����邽�߂̕ϐ�
     */
    private final OPERATOR operator;

    /**
     * �ꍀ���Z�̌��ʂ̌^��ۑ����邽�߂̕ϐ�
     */
    private final TypeInfo type;

    /**
     * ���Z�q���O�u���Ă��邩�ǂ����������ϐ�
     */
    private final boolean isPreposed;
}
