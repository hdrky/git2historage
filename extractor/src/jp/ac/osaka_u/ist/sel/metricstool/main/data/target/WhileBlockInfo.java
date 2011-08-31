package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * while �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class WhileBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� while �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public WhileBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("while (");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

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

        final WhileBlockInfo newWhileBlock = new WhileBlockInfo(fromLine, fromColumn, toLine,
                toColumn);

        final ConditionalClauseInfo newConditionalClause = this.getConditionalClause().copy();
        newWhileBlock.setConditionalClause(newConditionalClause);

        final UnitInfo outerUnit = this.getOuterUnit();
        newWhileBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newWhileBlock.addStatement((StatementInfo) statement.copy());
        }

        return newWhileBlock;
    }
}
