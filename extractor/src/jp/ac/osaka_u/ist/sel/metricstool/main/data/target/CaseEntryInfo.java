package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch ���� case �G���g����\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class CaseEntryInfo extends UnitInfo implements StatementInfo {

    /**
     * �Ή����� switch �u���b�N����^���� case �G���g����������
     * 
     * @param ownerSwitchBlock ���� case �G���g���������� switch �u���b�N
     * @param label ���� case �G���g���̃��x��
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final ExpressionInfo label,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerSwitchBlock) || (null == label)) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = label;

        this.label.setOwnerExecutableElement(this);
    }

    /**
     * �Ή����� switch �u���b�N����^���� case �G���g����������
     * 
     * @param ownerSwitchBlock ���� case �G���g���������� switch �u���b�N
     * @param breakStatement ���� case �G���g���� break ���������ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    protected CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = null;
    }

    /**
     * ���̕��icase �G���g���j�ŗp�����Ă���ϐ����p�̈ꗗ��Ԃ��D
     * �ǂ̕ϐ����p�����Ă��Ȃ��̂ŁC���set���Ԃ����
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
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
     * ����case�G���g���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����case�G���g���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("case ");

        final ExpressionInfo label = this.getLabel();
        sb.append(label.getText());

        sb.append(":");

        return sb.toString();
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��Ԃ�
     * 
     * @return ���� case �G���g���������� switch �u���b�N
     */
    public final SwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
    }

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.getOwnerSwitchBlock();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.getOwnerSwitchBlock().getOwnerMethod();
    }

    /**
     * ���� case �G���g���̂̃��x����Ԃ�
     * 
     * @return ���� case �G���g���̃��x��
     */
    public final ExpressionInfo getLabel() {
        return this.label;
    }

    /**
     * case�G���g���̃n�b�V���R�[�h��Ԃ�
     */
    @Override
    public final int hashCode() {
        return this.getOwnerSwitchBlock().hashCode()
                + ((null != this.getLabel()) ? this.getLabel().hashCode() : 0);
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

    @Override
    public ExecutableElementInfo copy() {

        final SwitchBlockInfo ownerBlock = this.getOwnerSwitchBlock();
        final ExpressionInfo label = (ExpressionInfo) this.getLabel().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final CaseEntryInfo newCaseEntry = new CaseEntryInfo(ownerBlock, label, fromLine,
                fromColumn, toLine, toColumn);

        return newCaseEntry;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * ���� case �G���g���̃��x����ۑ�����ϐ�
     */
    private ExpressionInfo label;
}
