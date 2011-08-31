package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * return���̏���ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class ReturnStatementInfo extends SingleStatementInfo {

    /**
     * return���̖߂�l��\�����ƈʒu����^���ď�����
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param ownerSpace ���𒼐ڏ��L������
     * @param returnedExpression
     * @param fromLine
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ReturnStatementInfo(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo returnedExpression, int fromLine, int fromColumn, int toLine,
            int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        assert null != returnedExpression: "returnedExpression must not be null";
        
        this.returnedExpression = returnedExpression;
        this.returnedExpression.setOwnerExecutableElement(this);
    }

    /**
     * return���̖߂�l��\������Ԃ�
     * 
     * @return return���̖߂�l��\����
     */
    public final ExpressionInfo getReturnedExpression() {
        return this.returnedExpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getReturnedExpression().getVariableUsages();
    }

    /**
     * ��`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ��`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getReturnedExpression().getCalls();
    }

    /**
     * ����return���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����return���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("return ");

        final ExpressionInfo statement = this.getReturnedExpression();
        sb.append(statement.getText());

        sb.append(";");

        return sb.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getReturnedExpression().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final ExpressionInfo returnedExpression = (ExpressionInfo) this.getReturnedExpression()
                .copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ReturnStatementInfo newStatement = new ReturnStatementInfo(outerUnit,
                returnedExpression, fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }

    /**
     * return���̖߂�l��\������ۑ����邽�߂̕ϐ�
     */
    private final ExpressionInfo returnedExpression;
}
