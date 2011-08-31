package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * for �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ForBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� for �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ForBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.initilizerExpressions = new TreeSet<ConditionInfo>();
        this.iteratorExpressions = new TreeSet<ExpressionInfo>();

    }

    /**
     * �ϐ����p�̈ꗗ��Ԃ��D
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        variableUsages.addAll(super.getVariableUsages());
        for (final ConditionInfo initializerExpression : this.getInitializerExpressions()) {
            variableUsages.addAll(initializerExpression.getVariableUsages());
        }
        for (final ExpressionInfo iteratorExpression : this.getIteratorExpressions()) {
            variableUsages.addAll(iteratorExpression.getVariableUsages());
        }
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * ��`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ��`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(super.getDefinedVariables());
        for (final ConditionInfo initializerExpression : this.getInitializerExpressions()) {
            definedVariables.addAll(initializerExpression.getDefinedVariables());
        }
        for (final ExpressionInfo iteratorExpression : this.getIteratorExpressions()) {
            definedVariables.addAll(iteratorExpression.getDefinedVariables());
        }
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * �Ăяo���ꗗ��Ԃ�
     * 
     * @return �Ăяo���ꗗ
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        calls.addAll(super.getCalls());

        final SortedSet<ConditionInfo> initializerExpressions = this.getInitializerExpressions();
        for (final ConditionInfo initializerExpression : initializerExpressions) {
            calls.addAll(initializerExpression.getCalls());
        }

        final SortedSet<ExpressionInfo> iteratorExpressions = this.getIteratorExpressions();
        for (final ExpressionInfo iteratorExpression : iteratorExpressions) {
            calls.addAll(iteratorExpression.getCalls());
        }

        return Collections.unmodifiableSet(calls);
    }

    /**
     * ����for���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����for���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("for (");

        final SortedSet<ConditionInfo> initializerExpressions = this.getInitializerExpressions();
        for (final ConditionInfo initializerExpression : initializerExpressions) {
            sb.append(initializerExpression.getText());
            if (initializerExpression instanceof StatementInfo) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(",");
        }
        if (0 < initializerExpressions.size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(" ; ");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(" ; ");

        final SortedSet<ExpressionInfo> iteratorExpressions = this.getIteratorExpressions();
        for (final ExpressionInfo iteratorExpression : iteratorExpressions) {
            sb.append(iteratorExpression.getText());
            sb.append(",");
        }
        if (0 < iteratorExpressions.size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(") {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * for���̏���������ǉ�
     * @param initializerExpression ��������
     */
    public final void addInitializerExpressions(final ConditionInfo initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initializerExpression is null");
        }

        this.initilizerExpressions.add(initializerExpression);

        // �֋X��CinitializerExpression �� ExpressionExpressionInfo�ŕ��
        if (initializerExpression instanceof ExpressionInfo) {
            final int fromLine = initializerExpression.getFromLine();
            final int fromColumn = initializerExpression.getFromColumn();
            final int toLine = initializerExpression.getToLine();
            final int toColumn = initializerExpression.getToColumn();

            //final ExpressionStatementInfo ownerStatement = new ExpressionStatementInfo(this,
            //        (ExpressionInfo) initializerExpression, fromLine, fromColumn, toLine, toColumn);
            //((ExpressionInfo) initializerExpression).setOwnerExecutableElement(ownerStatement);
            ((ExpressionInfo) initializerExpression).setOwnerExecutableElement(this);
        }

        initializerExpression.setOwnerConditionalBlock(this);
    }

    public final void removeInitializerExpressions(final ConditionInfo initializerExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == initializerExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }

        this.initilizerExpressions.remove(initializerExpression);
    }

    /**
     * for���̍X�V����ǉ�
     * @param iteratorExpression �J��Ԃ���
     */
    public final void addIteratorExpressions(final ExpressionInfo iteratorExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }

        this.iteratorExpressions.add(iteratorExpression);

        // �֋X��CiteratorExpression �� ExpressionStatementInfo�ŕ��
        {
            final int fromLine = iteratorExpression.getFromLine();
            final int fromColumn = iteratorExpression.getFromColumn();
            final int toLine = iteratorExpression.getToLine();
            final int toColumn = iteratorExpression.getToColumn();

            //final ExpressionStatementInfo ownerStatement = new ExpressionStatementInfo(this,
            //        iteratorExpression, fromLine, fromColumn, toLine, toColumn);
            //iteratorExpression.setOwnerExecutableElement(ownerStatement);
            iteratorExpression.setOwnerExecutableElement(this);
            iteratorExpression.setOwnerConditionalBlock(this);
        }
    }

    public final void removeIteratorExpressions(final ExpressionInfo iteratorExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }

        this.iteratorExpressions.remove(iteratorExpression);
    }

    /**
     * ���������̃Z�b�g��Ԃ�
     * @return ���������̃Z�b�g
     */
    public final SortedSet<ConditionInfo> getInitializerExpressions() {
        return Collections.unmodifiableSortedSet(this.initilizerExpressions);
    }

    /**
     * �X�V���̃Z�b�g��Ԃ�
     * @return �X�V��
     */
    public final SortedSet<ExpressionInfo> getIteratorExpressions() {
        return Collections.unmodifiableSortedSet(this.iteratorExpressions);
    }

    @Override
    public boolean isLoopStatement() {
        return true;
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        thrownExpressions.addAll(super.getThrownExceptions());
        for (final ConditionInfo initializerExpression : this.getInitializerExpressions()) {
            thrownExpressions.addAll(initializerExpression.getThrownExceptions());
        }
        for (final ExpressionInfo iteratorExpression : this.getIteratorExpressions()) {
            thrownExpressions.addAll(iteratorExpression.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ForBlockInfo newForBlock = new ForBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final ConditionalClauseInfo newConditionalClause = this.getConditionalClause().copy();
        newForBlock.setConditionalClause(newConditionalClause);

        for (final ConditionInfo initializer : this.getInitializerExpressions()) {
            newForBlock.addInitializerExpressions((ConditionInfo) initializer.copy());
        }

        for (final ExpressionInfo iterator : this.getIteratorExpressions()) {
            newForBlock.addIteratorExpressions((ExpressionInfo) iterator.copy());
        }

        final UnitInfo outerUnit = this.getOuterUnit();
        newForBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newForBlock.addStatement((StatementInfo) statement.copy());
        }

        return newForBlock;
    }

    /**
     * ����������ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ConditionInfo> initilizerExpressions;

    /**
     * �X�V����ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ExpressionInfo> iteratorExpressions;
}
