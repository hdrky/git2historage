package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * �����̏���\���N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class ExpressionStatementInfo extends SingleStatementInfo {

    /**
     * ���ƈʒu����^���ď�����
     * 
     * @param ownerSpace �O���̃X�R�[�v
     * @param expression �������\�����鎮
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ExpressionStatementInfo(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo expression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == expression) {
            throw new IllegalArgumentException("expression is null");
        }

        this.expression = expression;

        this.expression.setOwnerExecutableElement(this);
    }

    /**
     * �������\�����鎮��Ԃ�
     * 
     * @return �������\�����鎮
     */
    public final ExpressionInfo getExpression() {
        return this.expression;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo#getVariableUsages()
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getExpression().getVariableUsages();
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
        return this.getExpression().getCalls();
    }

    /**
     * ���̎����̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̎����̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo expression = this.getExpression();
        sb.append(expression.getText());

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
        return Collections.unmodifiableSet(this.getExpression().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final ExpressionInfo expression = (ExpressionInfo) this.getExpression().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(outerUnit,
                expression, fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }

    /**
     * �������\�����鎮��ۑ����邽�߂̕ϐ�
     */
    private final ExpressionInfo expression;

}
