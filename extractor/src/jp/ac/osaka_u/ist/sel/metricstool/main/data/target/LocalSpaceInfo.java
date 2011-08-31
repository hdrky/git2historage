package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���̈�(���\�b�h�⃁�\�b�h���u���b�N)��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class LocalSpaceInfo extends UnitInfo implements HavingOuterUnit {

    /**
     * �K�v�ȏ���^���ăI�u�W�F�N�g������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn�I����
     */
    LocalSpaceInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.outerUnit = null;
        this.statements = new TreeSet<StatementInfo>();
    }

    /**
     * ���̃��[�J���X�y�[�X���Œ�`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ���̃��[�J���X�y�[�X���Œ�`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        for (final StatementInfo statement : this.getStatements()) {
            definedVariables.addAll(statement.getDefinedVariables());
        }
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * ���̃��[�J���̈�ɕ���ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param statement �ǉ����镶
     */
    public void addStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.statements.add(statement);
    }

    /**
     * ���̃��[�J���̈悩�當���폜����D
     * 
     * @param statement �폜���镶
     */
    public void removeStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.statements.remove(statement);
    }

    /**
     * ���\�b�h����уR���X�g���N�^�Ăяo���ꗗ��Ԃ�
     * 
     * @return ���\�b�h����уR���X�g���N�^�Ăяo��
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();
        for (final StatementInfo statement : this.getStatements()) {
            calls.addAll(statement.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    /**
     * ���̃��[�J���̈�̕ϐ����p��Set��Ԃ�
     * 
     * @return ���̃��[�J���̈�̕ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for (final StatementInfo statement : this.getStatements()) {
            variableUsages.addAll(statement.getVariableUsages());
        }
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * ���̃��[�J���X�y�[�X�̒����̕����� SortedSet ��Ԃ��D
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfo�ȂǁCSubsequentialBlockInfo�͊܂܂Ȃ�
     * 
     * @return ���̃��[�J���X�y�[�X�̓���SubsequentialBlock���܂ޕ����� SortedSet
     */
    public SortedSet<StatementInfo> getStatements() {
        return this.getStatementsWithoutSubsequencialBlocks();
    }

    /**
     * ���̃��[�J���X�y�[�X�̒����̕����� SortedSet ��Ԃ��D
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfo�ȂǁCSubsequentialBlockInfo���܂�
     * 
     * @return ���̃��[�J���X�y�[�X�̓���SubsequentialBlock���܂ޕ����� SortedSet
     */
    public SortedSet<StatementInfo> getStatementsWithSubsequencialBlocks() {
        final SortedSet<StatementInfo> statements = new TreeSet<StatementInfo>();
        for (final StatementInfo statement : this.statements) {

            statements.add(statement);

            if (statement instanceof IfBlockInfo) {
                final IfBlockInfo ifBlock = (IfBlockInfo) statement;
                if (ifBlock.hasElseBlock()) {
                    statements.add(ifBlock.getSequentElseBlock());
                }
            }

            else if (statement instanceof TryBlockInfo) {
                final TryBlockInfo tryBlock = (TryBlockInfo) statement;
                for (final CatchBlockInfo catchBlock : tryBlock.getSequentCatchBlocks()) {
                    statements.add(catchBlock);
                }
                if (tryBlock.hasFinallyBlock()) {
                    statements.add(tryBlock.getSequentFinallyBlock());
                }
            }
        }

        return statements;
    }

    /** 
     * ���̃��[�J���X�y�[�X�̒����̕����� SortedSet ��Ԃ��D
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfo�͊܂܂�Ȃ��D
     * 
     * @return ���̃��[�J���X�y�[�X�̒����̕����� SortedSet
     */
    public SortedSet<StatementInfo> getStatementsWithoutSubsequencialBlocks() {
        final SortedSet<StatementInfo> statements = new TreeSet<StatementInfo>();
        statements.addAll(this.statements);
        return statements;
    }

    /**
     * �������Ă���N���X��Ԃ�
     * 
     * @return �������Ă���N���X
     */
    public final ClassInfo getOwnerClass() {
        return this.getOuterClass();
    }

    /**
     * �^����ꂽLocalSpace���ɑ��݂��Ă���S�Ă�StatementInfo��SortedSet��Ԃ�
     * ����ɂ�ElseBlockInfo, CatchBlockInfo, FinallyBlockInfo���܂܂��D
     * 
     * @param localSpace ���[�J���X�y�[�X
     * @return �^����ꂽLocalSpace���ɑ��݂��Ă���S�Ă�StatementInfo��SortedSet
     */
    @Deprecated
    public static SortedSet<StatementInfo> getAllStatements(final LocalSpaceInfo localSpace) {

        if (null == localSpace) {
            throw new IllegalArgumentException("localSpace is null.");
        }

        if (localSpace instanceof ExternalMethodInfo
                || localSpace instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("localSpace is an external local space.");
        }

        final SortedSet<StatementInfo> allStatements = new TreeSet<StatementInfo>();
        for (final StatementInfo innerStatement : localSpace.getStatements()) {
            allStatements.add(innerStatement);
            if (innerStatement instanceof BlockInfo) {
                allStatements.addAll(LocalSpaceInfo.getAllStatements((BlockInfo) innerStatement));
                /*                
                                // Else, Catch, Finally �̓��ʏ���
                                // FIXME ���ʂ̐e�N���X�����Ȃǂ��đ̑Ώ����ׂ�
                                if (innerStatement instanceof IfBlockInfo) {
                                    final ElseBlockInfo elseStatement = ((IfBlockInfo) innerStatement).getSequentElseBlock();
                                    allStatements.add(elseStatement);
                                    allStatements.addAll(LocalSpaceInfo.getAllStatements(elseStatement));
                                } else if (innerStatement instanceof TryBlockInfo) {
                                    final TryBlockInfo parentTryStatement = (TryBlockInfo)innerStatement;
                                    for (final CatchBlockInfo catchStatement : parentTryStatement.getSequentCatchBlocks()){
                                        allStatements.add(catchStatement);
                                        allStatements.addAll(LocalSpaceInfo.getAllStatements(catchStatement));
                                    }
                                    final FinallyBlockInfo finallyStatement = parentTryStatement.getSequentFinallyBlock();
                                    allStatements.add(finallyStatement);
                                    allStatements.addAll(LocalSpaceInfo.getAllStatements(finallyStatement));
                                }
                */
            }
        }
        return allStatements;
    }

    /**
     * �O���̃��j�b�g��Ԃ�
     */
    @Override
    public UnitInfo getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null!";
        return this.outerUnit;
    }

    /**
     * �O���̃��j�b�g��ݒ肷��
     * 
     * @param �O���̃��j�b�g
     */
    @Override
    public void setOuterUnit(final UnitInfo outerUnit) {

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
    public final ClassInfo getOuterClass() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            // �C���i�[�N���X�Ȃ̂ł��Ȃ炸�O���̃N���X������
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof ClassInfo) {
                return (ClassInfo) outer;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * �O���̃��\�b�h��Ԃ�.
     * 
     * @return�@�O���̃��\�b�h
     */
    @Override
    public final CallableUnitInfo getOuterCallableUnit() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof CallableUnitInfo) {
                return (CallableUnitInfo) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * ���̃��[�J���X�R�[�v�̒����̕����ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<StatementInfo> statements;

    /**
     * �O���̃��j�b�g��ۑ����邽�߂̕ϐ�
     */
    private UnitInfo outerUnit;
}
