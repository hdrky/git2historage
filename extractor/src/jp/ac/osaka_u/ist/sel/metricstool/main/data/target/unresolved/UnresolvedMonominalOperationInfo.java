package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ꍀ���Z�̓��e��\���N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedMonominalOperationInfo extends
        UnresolvedExpressionInfo<MonominalOperationInfo> {

    /**
     * ���ƈꍀ���Z�̌��ʂ̌^��^���ď�����
     * 
     * @param operand ��
     * @param operator �ꍀ���Z�̉��Z�q
     */
    public UnresolvedMonominalOperationInfo(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> operand,
            final OPERATOR operator) {

        if (null == operand || null == operator) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.operand = operand;
        this.operator = operator;
    }

    @Override
    public MonominalOperationInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedExpressionInfo<?> unresolvedTerm = this.getOperand();
        final ExpressionInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean isPreposed = this.isPreposed();

        this.resolvedInfo = new MonominalOperationInfo(term, this.operator, isPreposed,
                usingMethod, fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * �ꍀ���Z�̍���Ԃ�
     * 
     * @return �ꍀ���Z�̍�
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getOperand() {
        return this.operand;
    }

    public OPERATOR getOperator() {
        return this.operator;
    }

    public boolean isPreposed() {
        return this.getFromColumn() < this.operand.getFromColumn() ? true : false;
    }

    /**
     * �ꍀ���Z�̍�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> operand;

    /**
     * �ꍀ���Z�̉��Z�q
     */
    private final OPERATOR operator;

}
