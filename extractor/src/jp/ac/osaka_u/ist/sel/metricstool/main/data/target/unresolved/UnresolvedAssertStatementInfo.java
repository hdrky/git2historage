package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * assert���̖���������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedAssertStatementInfo extends
        UnresolvedSingleStatementInfo<AssertStatementInfo> {

    /**
     * �������A�T�[�g���𐶐�
     * 
     * @param outerLocalSpace �O���̃u���b�N
     */
    public UnresolvedAssertStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace) {
        super(outerLocalSpace);
    }

    @Override
    public AssertStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ���[�J���X�y�[�X������
        final UnresolvedLocalSpaceInfo<?> unresolvedOuterLocalSpace = this.getOuterLocalSpace();
        final LocalSpaceInfo outerLocalSpace = unresolvedOuterLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        final UnresolvedExpressionInfo<?> unresolvedAssertedExpression = this
                .getAssertedExpression();
        final ExpressionInfo assertedExpression = unresolvedAssertedExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final UnresolvedExpressionInfo<?> unresolvedMessageExpression = this.getMessageExpression();
        final ExpressionInfo messageExpression = null == unresolvedMessageExpression ? new EmptyExpressionInfo(
                usingMethod, toLine, toColumn, toLine, toColumn)
                : unresolvedMessageExpression.resolve(usingClass, usingMethod, classInfoManager,
                        fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new AssertStatementInfo(outerLocalSpace, assertedExpression,
                messageExpression, fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���؂̌��ʂ�false�ł������Ƃ��ɏo�͂���郁�b�Z�[�W��\�����̖���������ݒ肷��
     * @param messageExpression
     */
    public final void setMessageExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> messageExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == messageExpression) {
            throw new IllegalArgumentException();
        }
        this.messageExpression = messageExpression;
    }

    /**
     * ���b�Z�[�W��Ԃ�
     * 
     * @return�@���b�Z�[�W
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getMessageExpression() {
        return this.messageExpression;
    }

    /**
     * ���؎��̖���������ݒ肷��
     * @param assertedExpression ���؎��̖��������
     */
    public final void setAsserttedExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assertedExpression) {
            throw new IllegalArgumentException();
        }

        this.assertedExpression = assertedExpression;
    }

    /**
     * ���؎���Ԃ�
     * 
     * @return�@���؎�
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getAssertedExpression() {
        return this.assertedExpression;
    }

    /**
     * ���؎��̖���������ۑ�����ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression;

    /**
     * ���؎���false��Ԃ��Ƃ��ɏo�͂���郁�b�Z�[�W��\�����̖���������ۑ����邽�߂̕ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> messageExpression;

}
