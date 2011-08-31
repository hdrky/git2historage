package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedForeachBlockInfo extends UnresolvedConditionalBlockInfo<ForeachBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCforeach �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedForeachBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
        this.iteratorExpression = null;
        this.iteratorVariable = null;
    }

    /**
     * ���̖����� for �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public ForeachBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� foreach���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ForeachBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // �O���̋�Ԃ��擾
        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
    * �ϐ���`��ݒ肷��
    * 
    * @param iteraotorVariableDeclaration �ϐ���`
    */
    public void setIteratorVariable(
            final UnresolvedVariableDeclarationStatementInfo iteratorVariable) {

        if (null == iteratorVariable) {
            throw new IllegalArgumentException();
        }

        this.iteratorVariable = iteratorVariable;

        if (null != this.iteratorExpression) {

            final int fromLine = this.iteratorVariable.getFromLine();
            final int fromColumn = this.iteratorVariable.getFromColumn();
            final int toLine = this.iteratorExpression.getToLine();
            final int toColumn = this.iteratorExpression.getToColumn();
            final UnresolvedForeachConditionInfo condition = new UnresolvedForeachConditionInfo(
                    this, fromLine, fromColumn, toLine, toColumn);
            condition.setIteratorVariable(this.iteratorVariable);
            condition.setIteratorExpression(this.iteratorExpression);

            final UnresolvedConditionalClauseInfo conditionalClause = new UnresolvedConditionalClauseInfo(
                    this, condition);
            this.setConditionalClause(conditionalClause);
        }
    }

    /**
     * �J��Ԃ��p�̎���ݒ肷��
     * 
     * @param iteratorExpression �J��Ԃ��p�̎�
     */
    public void setIteratorExpression(final UnresolvedExpressionInfo<?> iteratorExpression) {

        if (null == iteratorExpression) {
            throw new IllegalArgumentException();
        }

        this.iteratorExpression = iteratorExpression;

        if (null != this.iteratorVariable) {
            final int fromLine = this.iteratorVariable.getFromLine();
            final int fromColumn = this.iteratorVariable.getFromColumn();
            final int toLine = this.iteratorExpression.getToLine();
            final int toColumn = this.iteratorExpression.getToColumn();
            final UnresolvedForeachConditionInfo condition = new UnresolvedForeachConditionInfo(
                    this, fromLine, fromColumn, toLine, toColumn);
            condition.setIteratorVariable(this.iteratorVariable);
            condition.setIteratorExpression(this.iteratorExpression);

            final UnresolvedConditionalClauseInfo conditionalClause = new UnresolvedConditionalClauseInfo(
                    this, condition);
            this.setConditionalClause(conditionalClause);
        }
    }

    /**
     * �ϐ���`��Ԃ�
     * 
     * @return �ϐ���`
     */
    public UnresolvedVariableDeclarationStatementInfo getIteratorVariable() {
        return this.iteratorVariable;
    }

    /**
     * �J��Ԃ��p�̎���Ԃ�
     * 
     * @return �J��Ԃ��p�̎�
     */
    public UnresolvedExpressionInfo<?> getIteratorExpression() {
        return this.iteratorExpression;
    }

    private UnresolvedVariableDeclarationStatementInfo iteratorVariable;

    private UnresolvedExpressionInfo<?> iteratorExpression;

}
