package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ catch �u���b�N����\���N���X
 * 
 * @author higo
 */
public final class UnresolvedCatchBlockInfo extends UnresolvedBlockInfo<CatchBlockInfo> implements
        UnresolvedSubsequentialBlockInfo<UnresolvedTryBlockInfo> {

    /**
     * �Ή�����try���ƊO���̃u���b�N��^���� catch �u���b�N��������
     * 
     * @param ownerTryBlock �Ή�����try��
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedCatchBlockInfo(final UnresolvedTryBlockInfo ownerTryBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerTryBlock) {
            throw new IllegalArgumentException("ownerTryBlock is null");
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * ���̖����� catch �߂���������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public CatchBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� catch�u���b�N�̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        //�@�����ς� catch�u���b�N�I�u�W�F�N�g���쐬
        this.resolvedInfo = new CatchBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // ���� catch �߂������� try �����擾
        final UnresolvedTryBlockInfo unresolvedOwnerTryBlock = this.getOwnerBlock();
        final TryBlockInfo ownerTryBlock = unresolvedOwnerTryBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerTryBlock);

        // �O���̃��j�b�g������
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

        // �L���b�`�����O������
        final UnresolvedLocalVariableInfo unresolvedCaughtException = this.getCaughtException();
        final LocalVariableInfo caughtException = unresolvedCaughtException.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setCaughtException(caughtException);

    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * ���̃��\�b�h�͏����p�~�\��ł���C�g�p�͐�������Ȃ�
     * {@link UnresolvedCatchBlockInfo#getOwnerBlock()} ���g�p���ׂ��ł���D
     * 
     * @return �Ή����� try �u���b�N
     * @deprecated
     */
    public UnresolvedTryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    /**
     * �Ή����� try �u���b�N��Ԃ�
     * 
     * @return �Ή����� try �u���b�N
     */
    @Override
    public UnresolvedTryBlockInfo getOwnerBlock() {
        return this.ownerTryBlock;
    }

    public UnresolvedLocalVariableInfo getCaughtException() {
        assert null != this.caughtException : "this.caughtException must not be null!";
        return this.caughtException;
    }

    public void setCaughtException(UnresolvedLocalVariableInfo caughtException) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caughtException) {
            throw new IllegalArgumentException("caughtException is null");
        }
        this.caughtException = caughtException;
    }

    private final UnresolvedTryBlockInfo ownerTryBlock;

    private UnresolvedLocalVariableInfo caughtException;

}
