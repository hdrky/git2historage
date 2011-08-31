package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����̖���������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedExpressionStatementInfo extends
        UnresolvedSingleStatementInfo<ExpressionStatementInfo> {

    /**
     * �������\�����鎮�̖���������^���ď�����
     * @param outerLocalSpace �����𒼐ڏ��L������
     * @param expression �������\�����鎮�̖��������
     */
    public UnresolvedExpressionStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> expression) {
        super(outerLocalSpace);

        if (null == expression) {
            throw new IllegalArgumentException("expression is null");
        }

        this.expression = expression;
    }

    @Override
    public ExpressionStatementInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo ownerSpace = this.getOuterLocalSpace().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final ExpressionInfo expression = this.expression.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ExpressionStatementInfo(ownerSpace, expression, fromLine,
                fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getExpression() {
        return this.expression;
    }

    /**
     * �������\�����鎮�̖���������ۑ�����ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> expression;

}
