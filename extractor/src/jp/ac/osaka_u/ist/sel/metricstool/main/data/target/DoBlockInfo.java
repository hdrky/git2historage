package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * do �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class DoBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� do �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public DoBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����Do���̃e�L�X�g�\���iString�^�j��ς���
     * 
     * @return ����Do���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("do {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("} while (");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(");");

        return sb.toString();

    }

    @Override
    public boolean isLoopStatement() {
        return true;
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final DoBlockInfo newDoBlock = new DoBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final ConditionalClauseInfo newConditionalClause = this.getConditionalClause().copy();
        newDoBlock.setConditionalClause(newConditionalClause);

        final UnitInfo outerUnit = this.getOuterUnit();
        newDoBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newDoBlock.addStatement((StatementInfo) statement.copy());
        }

        return newDoBlock;
    }
}
