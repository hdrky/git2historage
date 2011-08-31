package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����������g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedParameterUsageInfo extends
        UnresolvedVariableUsageInfo<ParameterUsageInfo> {

    /**
     * �g�p����Ă�������C�Q�Ƃ��ǂ�����^���ď�����
     * 
     * @param usedVariable �g�p����Ă���ϐ�
     * @param reference �Q�Ƃ��ǂ���
     * @param assignment ������ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedParameterUsageInfo(final UnresolvedParameterInfo usedVariable,
            boolean reference, final boolean assignment,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(usedVariable.getName(), reference, assignment, outerUnit, fromLine, fromColumn,
                toLine, toColumn);

        this.usedVariable = usedVariable;
    }

    /**
     * ���̖����������g�p����������
     */
    @Override
    public ParameterUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final ParameterInfo usedVariable = this.getUsedVariable().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = ParameterUsageInfo.getInstance(usedVariable, reference, assignment,
                usingMethod, fromLine, fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    /**
     * �g�p����Ă��������Ԃ�
     * 
     * @return�@�g�p����Ă������
     */
    public UnresolvedParameterInfo getUsedVariable() {
        return this.usedVariable;
    }

    private UnresolvedParameterInfo usedVariable;
}
