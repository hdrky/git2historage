package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if�@�� while �ȂǁC�����߂��������u���b�N����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class ConditionalBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���ď�����
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ConditionalBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

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
        variableUsages.addAll(this.getConditionalClause().getVariableUsages());
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
        definedVariables.addAll(this.getConditionalClause().getDefinedVariables());
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

        final ConditionInfo condition = this.getConditionalClause().getCondition();
        calls.addAll(condition.getCalls());

        return Collections.unmodifiableSet(calls);
    }

    /**
     * �����߂�ݒ肷��
     * 
     * @param conditionalClause ������
     */
    public final void setConditionalClause(final ConditionalClauseInfo conditionalClause) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == conditionalClause) {
            throw new IllegalArgumentException();
        }

        this.conditionalClause = conditionalClause;
    }

    /**
     * ���̏����t�u���b�N�̏����߂�Ԃ�
     * 
     * @return�@���̏����t�u���b�N�̏�����
     */
    public final ConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
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
        thrownExpressions.addAll(this.getConditionalClause().getCondition().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExpressions);
    }

    private ConditionalClauseInfo conditionalClause;
}
