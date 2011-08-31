package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������̏����߂̖���������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedConditionalClauseInfo extends UnresolvedUnitInfo<ConditionalClauseInfo> {

    /**
     * �����߂ɑΉ�����������̖���������^���ď�����
     * 
     * @param ownerConditionalBlockInfo �����߂ɑΉ�����������̖��������
     * @param condition ������
     */
    public UnresolvedConditionalClauseInfo(
            final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerConditionalBlockInfo,
            final UnresolvedConditionInfo<? extends ConditionInfo> condition) {
        super();

        if (null == ownerConditionalBlockInfo) {
            throw new IllegalArgumentException("conditionalBlock is null.");
        }

        this.condition = condition;
        this.ownerConditionalBlock = ownerConditionalBlockInfo;
    }

    @Override
    public ConditionalClauseInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final ConditionalBlockInfo ownerConditionalBlock = this.ownerConditionalBlock.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ConditionInfo condition = null != this.condition ? this.condition.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager) : null;

        this.resolvedInfo = new ConditionalClauseInfo(ownerConditionalBlock, condition, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    public UnresolvedConditionInfo<? extends ConditionInfo> getCondition() {
        return this.condition;
    }

    /**
     * �������̏����߂̖���������\���ϐ�
     */
    private final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerConditionalBlock;

    /**
     * �����߂ɋL�q����Ă�������̖���������\���ϐ�
     */
    private final UnresolvedConditionInfo<? extends ConditionInfo> condition;

}
