package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * throw���̏���ۗL����N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class ThrowStatementInfo extends SingleStatementInfo {

    /**
     * throw���ɂ���ē��������O��\�����ƈʒu����^���ď�����
     * 
     * @param ownerSpace ���𒼐ڏ��L������
     * @param thrownEpression throw���ɂ���ē��������O��\����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ThrowStatementInfo(final LocalSpaceInfo ownerSpace, ExpressionInfo thrownEpression,
            int fromLine, int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == thrownEpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        this.thrownEpression = thrownEpression;

        this.thrownEpression.setOwnerExecutableElement(this);
    }

    /**
     * throw���ɂ���ē��������O��\������Ԃ�
     * 
     * @return throw���ɂ���ē��������O��\����
     */
    public final ExpressionInfo getThrownExpression() {
        return this.thrownEpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getThrownExpression().getVariableUsages();
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
        return this.getThrownExpression().getCalls();
    }

    /**
     * ����throw���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����throw���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("throw ");

        final ExpressionInfo expression = this.getThrownExpression();
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
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        if (this.getThrownExpression().getType() instanceof ClassTypeInfo) {
            thrownExpressions.add((ClassTypeInfo) this.getThrownExpression().getType());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final ExpressionInfo thrownExpression = (ExpressionInfo) this.getThrownExpression().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ThrowStatementInfo newStatement = new ThrowStatementInfo(outerUnit, thrownExpression,
                fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }

    /**
     * throw���ɂ���ē��������O��\����
     */
    private final ExpressionInfo thrownEpression;

}
