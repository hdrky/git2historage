package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * try �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class TryBlockInfo extends BlockInfo {

    /**
     * �ʒu����^���� try �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TryBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.sequentFinallyBlock = null;
        this.sequentCatchBlocks = new TreeSet<CatchBlockInfo>();
    }

    /**
     * �Ή����� finally �����Z�b�g����
     * 
     * @param sequentFinallyBlock �Ή����� finally ��
     */
    public void setSequentFinallyBlock(final FinallyBlockInfo sequentFinallyBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentFinallyBlock) {
            throw new NullPointerException();
        }

        this.sequentFinallyBlock = sequentFinallyBlock;
    }

    /**
     * �Ή����� finally ����Ԃ�
     * 
     * @return �Ή����� finally ��
     */
    public FinallyBlockInfo getSequentFinallyBlock() {
        return this.sequentFinallyBlock;
    }

    /**
     * �Ή�����catch�u���b�N��ǉ�����
     * @param catchBlock �Ή�����catch�u���b�N
     */
    public void addSequentCatchBlock(final CatchBlockInfo catchBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == catchBlock) {
            throw new IllegalArgumentException("catchBlock is null");
        }

        this.sequentCatchBlocks.add(catchBlock);
    }

    /**
     * �Ή�����catch�u���b�N��Set��Ԃ�
     * @return �Ή�����catch�u���b�N��Set
     */
    public SortedSet<CatchBlockInfo> getSequentCatchBlocks() {
        return this.sequentCatchBlocks;
    }

    /**
     * �Ή�����finally�u���b�N�����݂��邩�ǂ����Ԃ�
     * @return �Ή�����finally�u���b�N�����݂���Ȃ�true
     */
    public boolean hasFinallyBlock() {
        return null != this.sequentFinallyBlock;
    }

    /**
     * ����try���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����try���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("try {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final TryBlockInfo newTryBlock = new TryBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final FinallyBlockInfo sequentFinallyBlock = (FinallyBlockInfo) this
                .getSequentFinallyBlock().copy();
        newTryBlock.setSequentFinallyBlock(sequentFinallyBlock);

        for (final CatchBlockInfo catchBlockInfo : this.getSequentCatchBlocks()) {
            newTryBlock.addSequentCatchBlock((CatchBlockInfo) catchBlockInfo.copy());
        }

        final UnitInfo outerUnit = this.getOuterUnit();
        newTryBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newTryBlock.addStatement((StatementInfo) statement.copy());
        }

        return newTryBlock;
    }

    /**
     * �Ή�����catch�u���b�N��ۑ�����ϐ�
     */
    private final SortedSet<CatchBlockInfo> sequentCatchBlocks;

    private FinallyBlockInfo sequentFinallyBlock;
}
