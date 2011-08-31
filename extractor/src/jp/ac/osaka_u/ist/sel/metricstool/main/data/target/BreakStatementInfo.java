package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 
 * @author t-miyake
 *
 * break����\���N���X
 */
@SuppressWarnings("serial")
public class BreakStatementInfo extends JumpStatementInfo {

    /**
     * �I�u�W�F�N�g��������
     * 
     * @param ownerSpace �I�[�i�[�u���b�N
     * @param destinationLabel ���x��
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public BreakStatementInfo(LocalSpaceInfo ownerSpace, LabelInfo destinationLabel, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, destinationLabel, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected String getReservedKeyword() {
        return "break";
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final LabelInfo label = this.getDestinationLabel();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final BreakStatementInfo newStatement = new BreakStatementInfo(outerUnit, label, fromLine,
                fromColumn, toLine, toColumn);

        return newStatement;
    }
}
