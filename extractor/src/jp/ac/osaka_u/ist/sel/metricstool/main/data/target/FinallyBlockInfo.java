package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * finally �u���b�N����\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class FinallyBlockInfo extends BlockInfo implements
        SubsequentialBlockInfo<TryBlockInfo> {

    /**
     * �Ή����� try �u���b�N����^���� finally �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public FinallyBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����finally�߂̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����finally�߂̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("finally {");
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
     * �Ή����� try �u���b�N��Ԃ�
     * ���̃��\�b�h�͏����p�~����邽�߁C�g�p�͐�������Ȃ�
     * {@link FinallyBlockInfo#getOwnerBlock()} ���g�p���ׂ��ł���D
     * 
     * @return �Ή����� try �u���b�N
     * @deprecated
     */
    public TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    @Override
    public TryBlockInfo getOwnerBlock() {
        assert null != this.ownerTryBlock : "this.ownerTryBlock must not be null!";
        return this.ownerTryBlock;
    }

    @Override
    public void setOwnerBlock(final TryBlockInfo ownerTryBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        if (null != this.ownerTryBlock) {
            throw new IllegalStateException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final FinallyBlockInfo newFinallyBlock = new FinallyBlockInfo(fromLine, fromColumn, toLine,
                toColumn);

        final TryBlockInfo ownerTryBlock = this.getOwnerBlock();
        newFinallyBlock.setOwnerBlock(ownerTryBlock);

        final UnitInfo outerUnit = this.getOuterUnit();
        newFinallyBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newFinallyBlock.addStatement((StatementInfo) statement.copy());
        }

        return newFinallyBlock;
    }

    private TryBlockInfo ownerTryBlock;

}
