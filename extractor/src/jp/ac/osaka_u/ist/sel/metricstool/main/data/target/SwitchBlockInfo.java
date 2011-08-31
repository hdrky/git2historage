package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * switch �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class SwitchBlockInfo extends ConditionalBlockInfo {

    /**
     * switch �u���b�N����������
     *
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public SwitchBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����switch���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ����switch���̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("switch (");

        final ConditionInfo conditionInfo = this.getConditionalClause().getCondition();
        sb.append(conditionInfo.getText());

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
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final SwitchBlockInfo newSwitchBlock = new SwitchBlockInfo(fromLine, fromColumn, toLine,
                toColumn);

        final ConditionalClauseInfo newConditionalClause = this.getConditionalClause().copy();
        newSwitchBlock.setConditionalClause(newConditionalClause);

        final UnitInfo outerUnit = this.getOuterUnit();
        newSwitchBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newSwitchBlock.addStatement((StatementInfo) statement.copy());
        }

        return newSwitchBlock;
    }
}
