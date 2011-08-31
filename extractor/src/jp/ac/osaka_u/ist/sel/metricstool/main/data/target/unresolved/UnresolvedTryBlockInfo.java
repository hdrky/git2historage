package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ try �u���b�N��\���N���X
 * 
 * @author higo
 */
public final class UnresolvedTryBlockInfo extends UnresolvedBlockInfo<TryBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCtry �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedTryBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.sequentCatchBlocks = new HashSet<UnresolvedCatchBlockInfo>();
        this.sequentFinallyBlock = null;
    }

    /**
     * ���̖����� try �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public TryBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� for �G���g���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new TryBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        // �Ή�����finally�߂�����
        if (this.hasFinallyBlock()) {
            final UnresolvedFinallyBlockInfo unresolvedFinallyBlock = this.getSequentFinallyBlock();
            final FinallyBlockInfo finallyBlock = unresolvedFinallyBlock.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.setSequentFinallyBlock(finallyBlock);
        }

        // �Ή�����catch�߂��������C�����ς�try�u���b�N�I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedCatchBlockInfo unresolvedCatchBlock : this.getSequentCatchBlocks()) {
            final CatchBlockInfo catchBlock = unresolvedCatchBlock.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addSequentCatchBlock(catchBlock);
        }

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
    public void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // �Ή�����finally�߂�����
        if (this.hasFinallyBlock()) {
            final UnresolvedFinallyBlockInfo unresolvedFinallyBlock = this.getSequentFinallyBlock();
            unresolvedFinallyBlock.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }

        // �Ή�����catch�߂��������C�����ς�try�u���b�N�I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedCatchBlockInfo unresolvedCatchBlock : this.getSequentCatchBlocks()) {
            unresolvedCatchBlock.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }
    }

    /**
     * �Ή�����catch�u���b�N��ǉ�����
     * @param catchBlock �Ή�����catch�u���b�N
     */
    public void addSequentCatchBlock(final UnresolvedCatchBlockInfo catchBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == catchBlock) {
            throw new IllegalArgumentException("catchBlock is null");
        }

        this.sequentCatchBlocks.add(catchBlock);
    }

    /**
     * �Ή�����catch�u���b�N��Set��Ԃ�
     * @return �Ή�����catch�u���b�N��Set
     */
    public Set<UnresolvedCatchBlockInfo> getSequentCatchBlocks() {
        return this.sequentCatchBlocks;
    }

    /**
     * �Ή�����finally�u���b�N��Ԃ�
     * @return �Ή�����finally�u���b�N�Dfinally�u���b�N���錾����Ă��Ȃ��Ƃ���null
     */
    public UnresolvedFinallyBlockInfo getSequentFinallyBlock() {
        return this.sequentFinallyBlock;
    }

    /**
     * �Ή�����finally�u���b�N���Z�b�g����
     * @param finallyBlock �Ή�����finally�u���b�N
     */
    public void setSequentFinallyBlock(final UnresolvedFinallyBlockInfo finallyBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == finallyBlock) {
            throw new IllegalArgumentException("finallyBlock is null");
        }
        this.sequentFinallyBlock = finallyBlock;
    }

    /**
     * �Ή�����finally�u���b�N�����݂��邩�ǂ����Ԃ�
     * @return �Ή�����finally�u���b�N�����݂���Ȃ�true
     */
    public boolean hasFinallyBlock() {
        return null != this.sequentFinallyBlock;
    }

    /**
     * �Ή�����catch�u���b�N��ۑ�����ϐ�
     */
    private final Set<UnresolvedCatchBlockInfo> sequentCatchBlocks;

    /**
     * �Ή����� finally �u���b�N��ۑ�����ϐ�
     */
    private UnresolvedFinallyBlockInfo sequentFinallyBlock;

}
