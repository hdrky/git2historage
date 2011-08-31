package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���������ʎ���\���N���X
 * 
 * @author higo
 *
 */
public class UnresolvedParenthesesExpressionInfo extends
        UnresolvedExpressionInfo<ParenthesesExpressionInfo> {

    /**
     * �I�u�W�F�N�g��������
     * 
     * @param parentheticExpression �������Ȋ��ʓ��̎�
     */
    public UnresolvedParenthesesExpressionInfo(
            final UnresolvedExpressionInfo<?> parentheticExpression) {

        if (null == parentheticExpression) {
            throw new IllegalArgumentException();
        }

        this.parentheticExpression = parentheticExpression;
    }

    /**
     * �������J�b�R���̎���Ԃ�
     * 
     * @return �������J�b�R���̎�
     */
    public UnresolvedExpressionInfo<?> getParentheticExpression() {
        return this.parentheticExpression;
    }

    /**
     * ���O�������s��
     */
    @Override
    public ParenthesesExpressionInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedExpressionInfo<?> unresolvedParentheticExpression = this
                .getParentheticExpression();
        final ExpressionInfo parentheticExpression = unresolvedParentheticExpression.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ParenthesesExpressionInfo(parentheticExpression, usingMethod,
                fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     *�@���ʓ��̎���ۑ����邽�߂̕ϐ�
     */
    final UnresolvedExpressionInfo<?> parentheticExpression;
}
