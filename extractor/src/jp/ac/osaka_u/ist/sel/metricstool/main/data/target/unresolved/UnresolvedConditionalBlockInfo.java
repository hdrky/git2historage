package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������������t���u���b�N����\���N���X
 * 
 * @author t-miyake, higo
 * @param <T> �����ς݃u���b�N�̌^
 *
 */
public abstract class UnresolvedConditionalBlockInfo<T extends ConditionalBlockInfo> extends
        UnresolvedBlockInfo<T> {

    /**
     * �O���̃u���b�N����^���āC�I�u�W�F�N�g��������
     * 
     * @param outerSpace �O���̃u���b�N���
     */
    public UnresolvedConditionalBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * ��������������Ԃ�
     * @return ������������
     */
    public final UnresolvedConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    /**
     * ��������������ݒ肷��
     * @param conditionalClause ������������
     */
    public final void setConditionalClause(final UnresolvedConditionalClauseInfo conditionalClause) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == conditionalClause) {
            throw new IllegalArgumentException("conditionalClause is null");
        }

        this.conditionalClause = conditionalClause;
    }

    /**
     * ���̃��[�J���̈�̃C���i�[�̈�𖼑O��������
     * 
     * @param usingClass ���̗̈悪���݂��Ă���N���X
     * @param usingMethod ���̗̈悪���݂��Ă��郁�\�b�h
     * @param classInfoManager �N���X�}�l�[�W��
     * @param fieldInfoManager �t�B�[���h�}�l�[�W��
     * @param methodInfoManager ���\�b�h�}�l�[�W��
     */
    @Override
    public void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // �������̉���
        final UnresolvedConditionalClauseInfo unresolvedConditionalClause = this
                .getConditionalClause();
        final ConditionalClauseInfo conditionalClause = unresolvedConditionalClause.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setConditionalClause(conditionalClause);

        // ��������ownerConditionalBlock��ݒ�
        conditionalClause.getCondition().setOwnerConditionalBlock(this.resolvedInfo);
    }

    /**
     * ��������������ۑ����邽�߂̕ϐ�
     */
    private UnresolvedConditionalClauseInfo conditionalClause;
}
