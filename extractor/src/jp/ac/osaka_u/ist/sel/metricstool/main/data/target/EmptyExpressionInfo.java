package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ��̎���\���N���X
 * return ; �� for ( ; ; ) �Ȃǂɗp����
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class EmptyExpressionInfo extends ExpressionInfo {

    /**
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public EmptyExpressionInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * void �^��Ԃ�
     * 
     * return void�^ 
     */
    @Override
    public TypeInfo getType() {
        return VoidTypeInfo.getInstance();
    }

    /**
     * ����0��String��Ԃ�
     * 
     * return ����0��String
     */
    @Override
    public String getText() {
        return "";
    }

    /**
     * �g�p����Ă���ϐ���Set��Ԃ��D
     * ���ۂ͂Ȃɂ��g�p����Ă��Ȃ��̂ŁC���Set���Ԃ����D
     * 
     * @return �g�p����Ă���ϐ���Set
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final EmptyExpressionInfo newEmptyExpression = new EmptyExpressionInfo(ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newEmptyExpression.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newEmptyExpression.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newEmptyExpression;

    }
}
