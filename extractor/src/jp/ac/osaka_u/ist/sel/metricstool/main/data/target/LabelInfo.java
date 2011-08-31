package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ���x����`��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class LabelInfo extends UnitInfo implements StatementInfo {

    /**
     * �ʒu����^���ă��x���I�u�W�F�N�g��������
     * 
     * @param name ���x����
     * @param labeledStatement ���̃��x�����t������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public LabelInfo(final String name, final StatementInfo labeledStatement, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.name = name;
        this.labeledStatement = labeledStatement;
    }

    /**
     * ���̕��i���x����`�j�ŗp�����Ă���ϐ����p�̈ꗗ��Ԃ��D
     * �ǂ̕ϐ����p�����Ă��Ȃ��̂ŁC���set���Ԃ����
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * �ϐ���`��Set��Ԃ�
     * 
     * @return �ϐ���`��Set��Ԃ�
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̃��x���t�����̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̃��x���t�����̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(" : ");
        return sb.toString();
    }

    /**
     * ���̃��x���̖��O��Ԃ�
     * 
     * @return ���̃��x���̖��O
     */
    public String getName() {
        return this.name;
    }

    /**
     * ���̃��x�����t��������Ԃ�
     * 
     * @return ���̃��x�����t������
     */
    public StatementInfo getLabeledStatement() {
        return this.labeledStatement;
    }

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.labeledStatement.getOwnerSpace();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.labeledStatement.getOwnerMethod();
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

    /**
     * ���̃��x���̃n�b�V���R�[�h��Ԃ�
     */
    @Override
    public final int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public ExecutableElementInfo copy() {

        final String name = this.getName();
        final StatementInfo statement = (StatementInfo) this.getLabeledStatement().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LabelInfo newLabel = new LabelInfo(name, statement, fromLine,
                fromColumn, toLine, toColumn);

        return newLabel;
    }

    /**
     * ���x���̖��O��\���ϐ�
     */
    private final String name;

    /**
     * ���̃��x�����t��������\���ϐ���Ԃ�
     */
    private final StatementInfo labeledStatement;
}
