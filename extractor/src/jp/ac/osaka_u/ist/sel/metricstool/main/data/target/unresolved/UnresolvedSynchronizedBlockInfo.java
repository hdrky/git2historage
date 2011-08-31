package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ synchronized ����\���N���X
 * 
 * @author higo
 */
public final class UnresolvedSynchronizedBlockInfo extends
        UnresolvedBlockInfo<SynchronizedBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCsynchronized �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedSynchronizedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * ���̖����� synchronized �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public SynchronizedBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�Nf
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� synchronized �u���b�N�̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new SynchronizedBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
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
    public final void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        final ExpressionInfo synchronizedExpression = this.synchronizedExpression.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setSynchronizedExpression(synchronizedExpression);
    }

    public void setSynchronizedExpression(
            UnresolvedExpressionInfo<? extends ExpressionInfo> synchronizedExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == synchronizedExpression) {
            throw new IllegalArgumentException("synchronizedExpression is null");
        }
        this.synchronizedExpression = synchronizedExpression;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getSynchronizedExpression() {
        return this.synchronizedExpression;
    }

    private UnresolvedExpressionInfo<? extends ExpressionInfo> synchronizedExpression;
}
