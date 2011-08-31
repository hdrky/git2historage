package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���������[�J���ϐ��g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public class UnresolvedLocalVariableUsageInfo extends
        UnresolvedVariableUsageInfo<LocalVariableUsageInfo> {

    /**
     * �K�v�ȏ���^���āC�I�u�W�F�N�g��������
     * 
     * @param usedVariable �g�p����Ă���ϐ�
     * @param reference �Q�Ƃ��ǂ���
     * @param assignment ������ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedLocalVariableUsageInfo(final UnresolvedLocalVariableInfo usedVariable,
            boolean reference, final boolean assignment,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(usedVariable.getName(), reference, assignment, outerUnit, fromLine, fromColumn,
                toLine, toColumn);

        this.usedVariable = usedVariable;
    }

    @Override
    public LocalVariableUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final LocalVariableInfo usedVariable = this.getUsedVariable().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = LocalVariableUsageInfo.getInstance(usedVariable, reference, assignment,
                usingMethod, fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * �g�p����Ă���ϐ���Ԃ�
     * 
     * @return �g�p����Ă���ϐ�
     */
    public UnresolvedLocalVariableInfo getUsedVariable() {
        return this.usedVariable;
    }

    private UnresolvedLocalVariableInfo usedVariable;
}
