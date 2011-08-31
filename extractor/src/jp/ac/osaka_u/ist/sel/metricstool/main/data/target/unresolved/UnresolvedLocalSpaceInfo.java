package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.HavingOuterUnit;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���̈�(���\�b�h�ƃ��\�b�h���̃u���b�N)��\���N���X
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class UnresolvedLocalSpaceInfo<T extends LocalSpaceInfo> extends
        UnresolvedUnitInfo<T> implements UnresolvedHavingOuterUnit {

    /**
     * �ʒu����^���ď�����
     */
    public UnresolvedLocalSpaceInfo(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.calls = new HashSet<UnresolvedCallInfo<?>>();
        this.variableUsages = new HashSet<UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.statements = new TreeSet<UnresolvedStatementInfo<?>>();
        this.outerUnit = outerUnit;
    }

    /**
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ǉ�����
     * 
     * @param call ���\�b�h�܂��̓R���X�g���N�^�Ăяo��
     */
    public final void addCall(final UnresolvedCallInfo<?> call) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == call) {
            throw new NullPointerException();
        }

        this.calls.add(call);
    }

    /**
     * �ϐ��g�p��ǉ�����
     * 
     * @param variableUsage �ϐ��g�p
     */
    public final void addVariableUsage(
            final UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == variableUsage) {
            throw new NullPointerException();
        }

        this.variableUsages.add(variableUsage);
    }

    /**
     * ���[�J���ϐ���ǉ�����
     * 
     * @param localVariable ���[�J���ϐ�
     */
    public final void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * ����������ǉ�����
     * 
     * @param statement ��������
     */
    public void addStatement(final UnresolvedStatementInfo<?> statement) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        // Catch, Finally, Else�u���b�N�̂Ƃ��́C�ǉ����Ȃ�
        if (statement instanceof UnresolvedCatchBlockInfo
                || statement instanceof UnresolvedFinallyBlockInfo
                || statement instanceof UnresolvedElseBlockInfo) {
            return;
        }

        this.statements.add(statement);
    }

    /**
     * TODO ���O��ς���
     * �C���i�[�u���b�N��ǉ�����
     * 
     * @param innerLocalInfo �ǉ�����C���i�[�u���b�N
     */
    public void addChildSpaceInfo(final UnresolvedLocalSpaceInfo<?> innerLocalInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerLocalInfo) {
            throw new NullPointerException();
        }

        this.variableUsages.addAll(innerLocalInfo.variableUsages);
        this.localVariables.addAll(innerLocalInfo.localVariables);
        this.calls.addAll(innerLocalInfo.calls);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo������уR���X�g���N�^�Ăяo���� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�������\�b�h�Ăяo������уR���X�g���N�^�Ăяo���� Set
     */
    public final Set<UnresolvedCallInfo<?>> getCalls() {
        return Collections.unmodifiableSet(this.calls);
    }

    /**
     * ���̃u���b�N���ōs���Ă��関�����ϐ��g�p�� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���ōs���Ă��関�����ϐ��g�p�� Set
     */
    public final Set<UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>> getVariableUsages() {
        return Collections.unmodifiableSet(this.variableUsages);
    }

    /**
     * ���̃u���b�N���Œ�`����Ă��関�������[�J���ϐ��� Set ��Ԃ�
     * 
     * @return ���̃u���b�N���Œ�`����Ă��関�������[�J���ϐ��� Set
     */
    public final Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * ���̃u���b�N���̖����������u���b�N�� Set ��Ԃ�
     * else, catch, finally�u���b�N�͊܂܂�Ȃ�
     * 
     * @return ���̃u���b�N���̖����������u���b�N�� Set
     */
    public final Set<UnresolvedStatementInfo<? extends StatementInfo>> getStatements() {
        return Collections.unmodifiableSet(this.statements);
    }

    /**
     * ���̃��[�J���X�y�[�X�̒����̕����� SortedSet ��Ԃ��D
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfo�ȂǁCSubsequentialBlockInfo���܂�
     * 
     * @return ���̃��[�J���X�y�[�X�̓���SubsequentialBlock���܂ޕ����� SortedSet
     */
    public final SortedSet<UnresolvedStatementInfo<? extends StatementInfo>> getStatementsWithSubsequencialBlocks() {
        return Collections.unmodifiableSortedSet(this.statements);
    }

    /** 
     * ���̃��[�J���X�y�[�X�̒����̕����� SortedSet ��Ԃ��D
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfo�͊܂܂�Ȃ��D
     * 
     * @return ���̃��[�J���X�y�[�X�̒����̕����� SortedSet
     */
    public final SortedSet<UnresolvedStatementInfo<? extends StatementInfo>> getStatementsWithOutSubsequencialBlocks() {
        final SortedSet<UnresolvedStatementInfo<? extends StatementInfo>> statements = new TreeSet<UnresolvedStatementInfo<? extends StatementInfo>>();
        for (final UnresolvedStatementInfo<? extends StatementInfo> statementInfo : this.statements) {
            if (!(statementInfo instanceof UnresolvedSubsequentialBlockInfo<?>)) {
                statements.add(statementInfo);
            }
        }

        return Collections.unmodifiableSortedSet(this.statements);
    }

    /**
     * ���̃u���b�N���̖����������u���b�N
     * 
     */

    /**
     * ���̃��[�J���̈�̃C���i�[�̈�𖼑O��������
     * 
     * @param usingClass ���̗̈悪���݂��Ă���N���X
     * @param usingMethod ���̗̈悪���݂��Ă��郁�\�b�h
     * @param classInfoManager �N���X�}�l�[�W��
     * @param fieldInfoManager �t�B�[���h�}�l�[�W��
     * @param methodInfoManager ���\�b�h�}�l�[�W��
     */
    public void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        for (final UnresolvedStatementInfo<?> unresolvedStatement : this.getStatements()) {

            // �X�e�[�g�����g���u���b�N�̎�            
            if (unresolvedStatement instanceof UnresolvedBlockInfo<?>) {

                final UnresolvedBlockInfo<?> unresolvedBlock = (UnresolvedBlockInfo<?>) unresolvedStatement;
                final StatementInfo block = unresolvedStatement.resolve(usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                this.resolvedInfo.addStatement(block);

                unresolvedBlock.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                        fieldInfoManager, methodInfoManager);
            }

            // �X�e�[�g�����g�� single �X�e�[�g�����g�̎�
            else {
                final StatementInfo statement = unresolvedStatement.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                this.resolvedInfo.addStatement(statement);
            }
        }
    }

    @Override
    public final UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null!";
        return this.outerUnit;
    }

    @Override
    public final void setOuterUnit(UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * �O���̃N���X��Ԃ�.
     * 
     * @return�@�O���̃N���X
     */
    @Override
    public final UnresolvedClassInfo getOuterClass() {

        UnresolvedUnitInfo<? extends UnitInfo> outer = this.getOuterUnit();

        while (true) {

            // �C���i�[�N���X�Ȃ̂ł��Ȃ炸�O���̃N���X������
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof UnresolvedClassInfo) {
                return (UnresolvedClassInfo) outer;
            }

            outer = ((UnresolvedHavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * �O���̃��\�b�h��Ԃ�.
     * 
     * @return�@�O���̃��\�b�h
     */
    @Override
    public final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {

        UnresolvedUnitInfo<? extends UnitInfo> outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof UnresolvedCallableUnitInfo<?>) {
                return (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((UnresolvedHavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * ���\�b�h�܂��̓R���X�g���N�^�Ăяo����ۑ�����ϐ�
     */
    protected final Set<UnresolvedCallInfo<?>> calls;

    /**
     * �t�B�[���h�g�p��ۑ�����ϐ�
     */
    protected final Set<UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>> variableUsages;

    /**
     * ���̃��[�J���̈���Œ�`����Ă��郍�[�J���ϐ���ۑ�����ϐ�
     */
    protected final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * ���̃��[�J���̈���Œ�`���ꂽ����������ۑ�����ϐ�
     */
    protected final SortedSet<UnresolvedStatementInfo<?>> statements;

    private UnresolvedUnitInfo<? extends UnitInfo> outerUnit;
}
